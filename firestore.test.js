const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

function pastTimestamp() {
  return new Date(Date.now() - 2000);
}

function validProfilePayload(uid) {
  const ts = pastTimestamp();
  return {
    userId: uid,
    displayName: "Max Mustermann",
    xp: 250,
    streakDays: 3,
    longestStreak: 5,
    dailyGoalMinutes: 15,
    todayMinutes: 10,
    lastActiveDate: "2026-11-14",
    meisterRank: "Lagerhelfer",
    createdAt: ts,
    updatedAt: ts,
  };
}

function validLessonPayload(lessonId) {
  const ts = pastTimestamp();
  return {
    lessonId,
    moduleCode: "BQ",
    topicId: "bq_recht",
    completed: true,
    xpEarned: 50,
    masteryPercent: 90,
    errorCount: 1,
    createdAt: ts,
    updatedAt: ts,
  };
}

function validExamPayload() {
  const ts = pastTimestamp();
  return {
    examMode: "HQ",
    title: "Situationsaufgabe Südwestfalen",
    scenarioSummary: "Optimierung eines Hochregallagers.",
    achievedPoints: 84,
    maxPoints: 100,
    ihkGrade: 2,
    passed: true,
    durationMinutes: 180,
    feedbackSummary: "Starke KLR- und Kanban-Argumentation.",
    createdAt: ts,
    updatedAt: ts,
  };
}

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated user: cannot read or write user profile or lesson progress", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).get());
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).collection("lessonProgress").get());
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).set(validProfilePayload(ALICE_UID)));
});

test("Authenticated user: can create and update own profile, cannot inject ghost field", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const profileRef = aliceDb.collection("users").doc(ALICE_UID);

  await assertSucceeds(profileRef.set(validProfilePayload(ALICE_UID)));
  await assertSucceeds(profileRef.get());
  await assertSucceeds(
    profileRef.update({
      xp: 350,
      meisterRank: "Fachkraft für Lagerlogistik",
      updatedAt: pastTimestamp(),
    })
  );

  // Shadow update with ghost field must fail
  await assertFails(
    profileRef.update({
      xp: 400,
      isAdmin: true,
      updatedAt: pastTimestamp(),
    })
  );

  // Invalid rank enum must fail
  await assertFails(
    profileRef.update({
      meisterRank: "SuperAdmin",
      updatedAt: pastTimestamp(),
    })
  );
});

test("Cross-user isolation: Bob cannot read or write Alice's profile or progress", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();

  await assertSucceeds(aliceDb.collection("users").doc(ALICE_UID).set(validProfilePayload(ALICE_UID)));
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).collection("lessonProgress").doc("bq_recht_01").set(validLessonPayload("bq_recht_01"))
  );

  await assertFails(bobDb.collection("users").doc(ALICE_UID).get());
  await assertFails(bobDb.collection("users").doc(ALICE_UID).collection("lessonProgress").get());
  await assertFails(
    bobDb.collection("users").doc(ALICE_UID).update({ xp: 9999, updatedAt: pastTimestamp() })
  );
});

test("Authenticated user: can create and list own lessonProgress, formulaMastery, and immutable examAttempts", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const userRef = aliceDb.collection("users").doc(ALICE_UID);

  await assertSucceeds(userRef.collection("lessonProgress").doc("bq_recht_01").set(validLessonPayload("bq_recht_01")));
  await assertSucceeds(userRef.collection("lessonProgress").get());

  const ts = pastTimestamp();
  await assertSucceeds(
    userRef.collection("formulaMastery").doc("lagerumschlag").set({
      formulaId: "lagerumschlag",
      formulaName: "Lagerumschlagshäufigkeit",
      category: "HQ",
      solvedCount: 2,
      dojoStreak: 2,
      lastResultCorrect: true,
      createdAt: ts,
      updatedAt: ts,
    })
  );
  await assertSucceeds(userRef.collection("formulaMastery").get());

  const examRef = userRef.collection("examAttempts").doc("attempt_01");
  await assertSucceeds(examRef.set(validExamPayload()));
  await assertSucceeds(userRef.collection("examAttempts").get());

  // Terminal locking: examAttempts cannot be updated once submitted
  await assertFails(examRef.update({ achievedPoints: 100, updatedAt: pastTimestamp() }));
});
