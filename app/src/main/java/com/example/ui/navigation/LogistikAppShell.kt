package com.example.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.core.AdaptiveLearningStore
import com.example.core.BookmarkStore
import com.example.core.VoiceStudyController
import com.example.data.curriculum.ExaminerTipsCatalog
import com.example.data.repository.LearningRepository
import com.example.ui.dashboard.DashboardScreen
import com.example.ui.dashboard.DashboardUiState
import com.example.ui.dashboard.MainViewModel
import com.example.ui.exam.ExamSimulatorScreen
import com.example.ui.exam.ExamViewModel
import com.example.ui.formulas.FormulaDojoScreen
import com.example.ui.formulas.FormulaViewModel
import com.example.ui.learn.LearnPathScreen
import com.example.ui.learn.LearnViewModel
import com.example.ui.theme.AppThemeMode
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.SignalOrange
import com.example.ui.theme.SkyCognitive
import kotlinx.coroutines.launch

object LogistikRoutes {
    const val DASHBOARD = "dashboard"
    const val LEARN = "learn"
    const val FORMULAS = "formulas"
    const val EXAM = "exam"
}

private data class NavDestination(
    val route: String,
    val label: String,
    val icon: ImageVector
)

private val destinations = listOf(
    NavDestination(LogistikRoutes.DASHBOARD, "Dashboard", Icons.Filled.Dashboard),
    NavDestination(LogistikRoutes.LEARN, "Lernen", Icons.Filled.MenuBook),
    NavDestination(LogistikRoutes.FORMULAS, "Formeln", Icons.Filled.Calculate),
    NavDestination(LogistikRoutes.EXAM, "Prüfung", Icons.Filled.School)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogistikAppShell(
    currentUserId: String,
    userDisplayName: String,
    themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    onSelectThemeMode: (AppThemeMode) -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val repository = remember(context) {
        LearningRepository(context)
    }
    val bookmarkStore = remember(currentUserId, context) {
        BookmarkStore(context, currentUserId)
    }
    val bookmarkedIds by bookmarkStore.bookmarkedIds.collectAsStateWithLifecycle()

    val adaptiveLearningStore = remember(currentUserId, context) {
        AdaptiveLearningStore(context, currentUserId)
    }
    val leitnerBoxMap by adaptiveLearningStore.boxMap.collectAsStateWithLifecycle()

    val voiceController = remember(context) {
        VoiceStudyController(context)
    }
    DisposableEffect(voiceController) {
        onDispose { voiceController.release() }
    }

    val mainViewModel: MainViewModel = viewModel(
        key = "main_$currentUserId",
        factory = viewModelFactory {
            initializer { MainViewModel(repository, currentUserId, userDisplayName) }
        }
    )
    val learnViewModel: LearnViewModel = viewModel(
        key = "learn_$currentUserId",
        factory = viewModelFactory {
            initializer { LearnViewModel(repository, adaptiveLearningStore) }
        }
    )
    val formulaViewModel: FormulaViewModel = viewModel(
        key = "formula_$currentUserId",
        factory = viewModelFactory {
            initializer { FormulaViewModel(repository) }
        }
    )
    val examViewModel: ExamViewModel = viewModel(
        key = "exam_$currentUserId",
        factory = viewModelFactory {
            initializer { ExamViewModel(repository) }
        }
    )

    val dashboardState by mainViewModel.uiState.collectAsStateWithLifecycle()
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: LogistikRoutes.DASHBOARD
    var showExaminerTips by remember { mutableStateOf(false) }
    var showEditNameDialog by remember { mutableStateOf(false) }

    fun navigateTo(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    val readyUi = dashboardState as? DashboardUiState.Ready
    val openWeaknessCount = readyUi?.weaknesses?.count { !it.resolved } ?: 0

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(304.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = "Xels Prüfungssimulator",
                            style = MaterialTheme.typography.titleLarge,
                            color = SignalOrange
                        )
                        Text(
                            text = "zum Logistikmeister (SIHK)",
                            style = MaterialTheme.typography.labelLarge
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = readyUi?.profile?.let { "${it.displayName} · ${it.meisterRank}" } ?: userDisplayName,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    NavigationDrawerItem(
                        label = { Text("Übersicht & Statistik") },
                        selected = currentRoute == LogistikRoutes.DASHBOARD,
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            navigateTo(LogistikRoutes.DASHBOARD)
                        },
                        icon = { Icon(Icons.Filled.Dashboard, contentDescription = null) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    NavigationDrawerItem(
                        label = { Text("Handlungsfelder (Im Block)") },
                        selected = currentRoute == LogistikRoutes.LEARN,
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            learnViewModel.closeSession()
                            navigateTo(LogistikRoutes.LEARN)
                        },
                        icon = { Icon(Icons.Filled.MenuBook, contentDescription = null) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    NavigationDrawerItem(
                        label = { Text("Intelligenter Lernplan") },
                        selected = false,
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            learnViewModel.startAdaptivePlan(count = 15)
                            navigateTo(LogistikRoutes.LEARN)
                        },
                        icon = { Icon(Icons.Filled.AutoAwesome, contentDescription = null) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    NavigationDrawerItem(
                        label = { Text("Schnelltest (10 Fragen)") },
                        selected = false,
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            learnViewModel.startQuickTest(count = 10)
                            navigateTo(LogistikRoutes.LEARN)
                        },
                        icon = { Icon(Icons.Filled.Bolt, contentDescription = null) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    NavigationDrawerItem(
                        label = { Text("„Falsch gedacht“") },
                        selected = false,
                        badge = {
                            if (openWeaknessCount > 0) {
                                Badge(containerColor = SignalOrange) {
                                    Text("$openWeaknessCount")
                                }
                            }
                        },
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            learnViewModel.startSmartReview(readyUi?.weaknesses.orEmpty())
                            navigateTo(LogistikRoutes.LEARN)
                        },
                        icon = { Icon(Icons.Filled.Refresh, contentDescription = null) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    NavigationDrawerItem(
                        label = { Text("BQ/HQ-Prüfungsmodus (25 Fr.)") },
                        selected = false,
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            learnViewModel.startMcExamSimulation(25, 30)
                            navigateTo(LogistikRoutes.LEARN)
                        },
                        icon = { Icon(Icons.Filled.Quiz, contentDescription = null) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    NavigationDrawerItem(
                        label = { Text("Karteikarten-Modus") },
                        selected = false,
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            learnViewModel.startFlashcardsSession(count = 15)
                            navigateTo(LogistikRoutes.LEARN)
                        },
                        icon = { Icon(Icons.Filled.Flip, contentDescription = null) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    NavigationDrawerItem(
                        label = { Text("Merkliste") },
                        selected = false,
                        badge = {
                            if (bookmarkedIds.isNotEmpty()) {
                                Badge(containerColor = SkyCognitive) {
                                    Text("${bookmarkedIds.size}")
                                }
                            }
                        },
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            if (bookmarkedIds.isNotEmpty()) {
                                learnViewModel.startBookmarkedSession(bookmarkedIds, asFlashcards = true)
                            } else {
                                learnViewModel.startFlashcardsSession(title = "Karteikarten-Mix", count = 10)
                            }
                            navigateTo(LogistikRoutes.LEARN)
                        },
                        icon = { Icon(Icons.Filled.Bookmark, contentDescription = null) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    NavigationDrawerItem(
                        label = { Text("Formel-Engine & Dojo") },
                        selected = currentRoute == LogistikRoutes.FORMULAS,
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            navigateTo(LogistikRoutes.FORMULAS)
                        },
                        icon = { Icon(Icons.Filled.Calculate, contentDescription = null) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    NavigationDrawerItem(
                        label = { Text("Situationsaufgabe & Ausschuss") },
                        selected = currentRoute == LogistikRoutes.EXAM,
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            navigateTo(LogistikRoutes.EXAM)
                        },
                        icon = { Icon(Icons.Filled.School, contentDescription = null) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                    NavigationDrawerItem(
                        label = { Text("Prüfungstipps vom IHK-Prüfer") },
                        selected = false,
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            showExaminerTips = true
                        },
                        icon = { Icon(Icons.Filled.Lightbulb, contentDescription = null) },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

                    Column(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Darstellung (Hell / Dunkel)",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            AppThemeMode.entries.forEach { mode ->
                                FilterChip(
                                    selected = themeMode == mode,
                                    onClick = { onSelectThemeMode(mode) },
                                    label = { Text(mode.label) }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    NavigationDrawerItem(
                        label = { Text("Kandidatenname ändern") },
                        selected = false,
                        onClick = {
                            coroutineScope.launch { drawerState.close() }
                            showEditNameDialog = true
                        },
                        icon = { Icon(Icons.Filled.Edit, contentDescription = null) },
                        modifier = Modifier
                            .padding(NavigationDrawerItemDefaults.ItemPadding)
                            .testTag("drawer_edit_name")
                    )
                }
            }
        }
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val useRail = maxWidth >= 600.dp

            Scaffold(
                topBar = {
                    TopAppBar(
                        navigationIcon = {
                            IconButton(
                                onClick = { coroutineScope.launch { drawerState.open() } },
                                modifier = Modifier.testTag("open_drawer_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Menu,
                                    contentDescription = "Menü öffnen"
                                )
                            }
                        },
                        title = {
                            Column {
                                Text(
                                    text = "Xels Prüfungssimulator",
                                    style = MaterialTheme.typography.titleLarge,
                                    maxLines = 1
                                )
                                Text(
                                    text = "zum Logistikmeister",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1
                                )
                            }
                        },
                        actions = {
                            IconButton(
                                onClick = {
                                    val next = when (themeMode) {
                                        AppThemeMode.SYSTEM -> AppThemeMode.DARK
                                        AppThemeMode.DARK -> AppThemeMode.LIGHT
                                        AppThemeMode.LIGHT -> AppThemeMode.SYSTEM
                                    }
                                    onSelectThemeMode(next)
                                },
                                modifier = Modifier.testTag("toggle_theme_button")
                            ) {
                                Icon(
                                    imageVector = if (themeMode == AppThemeMode.DARK) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                                    contentDescription = "Hell-/Dunkelmodus wechseln"
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background
                        )
                    )
                },
                bottomBar = {
                    if (!useRail) {
                        NavigationBar {
                            destinations.forEach { dest ->
                                NavigationBarItem(
                                    selected = currentRoute == dest.route,
                                    onClick = { navigateTo(dest.route) },
                                    icon = { Icon(dest.icon, contentDescription = dest.label) },
                                    label = { Text(dest.label) },
                                    modifier = Modifier.testTag("nav_${dest.route}")
                                )
                            }
                        }
                    }
                }
            ) { innerPadding ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    if (useRail) {
                        NavigationRail {
                            destinations.forEach { dest ->
                                NavigationRailItem(
                                    selected = currentRoute == dest.route,
                                    onClick = { navigateTo(dest.route) },
                                    icon = { Icon(dest.icon, contentDescription = dest.label) },
                                    label = { Text(dest.label) },
                                    modifier = Modifier.testTag("rail_${dest.route}")
                                )
                            }
                        }
                    }

                    when (val ui = dashboardState) {
                        is DashboardUiState.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = SignalOrange)
                            }
                        }

                        is DashboardUiState.Error -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = ui.message,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }

                        is DashboardUiState.Ready -> {
                            NavHost(
                                navController = navController,
                                startDestination = LogistikRoutes.DASHBOARD,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                composable(LogistikRoutes.DASHBOARD) {
                                    DashboardScreen(
                                        data = ui,
                                        onStartSmartReview = {
                                            learnViewModel.startSmartReview(ui.weaknesses)
                                            navigateTo(LogistikRoutes.LEARN)
                                        },
                                        onStartQuickTest = {
                                            learnViewModel.startQuickTest(count = 10)
                                            navigateTo(LogistikRoutes.LEARN)
                                        },
                                        onStartMcExam = {
                                            learnViewModel.startMcExamSimulation(25, 30)
                                            navigateTo(LogistikRoutes.LEARN)
                                        },
                                        onStartDailySprint = {
                                            learnViewModel.startDailySprint()
                                            navigateTo(LogistikRoutes.LEARN)
                                        },
                                        onOpenExaminerTips = { showExaminerTips = true },
                                        onUpdateDailyGoalMinutes = mainViewModel::updateDailyGoalMinutes,
                                        onNavigateToFormulas = { navigateTo(LogistikRoutes.FORMULAS) },
                                        onNavigateToExam = { navigateTo(LogistikRoutes.EXAM) }
                                    )
                                }
                                composable(LogistikRoutes.LEARN) {
                                    LearnPathScreen(
                                        learnViewModel = learnViewModel,
                                        lessonProgress = ui.lessonProgress,
                                        weaknesses = ui.weaknesses,
                                        bookmarkedIds = bookmarkedIds,
                                        onToggleBookmark = bookmarkStore::toggleBookmark,
                                        leitnerBoxMap = leitnerBoxMap,
                                        voiceController = voiceController
                                    )
                                }
                                composable(LogistikRoutes.FORMULAS) {
                                    FormulaDojoScreen(
                                        formulaViewModel = formulaViewModel,
                                        formulaMasteryList = ui.formulaMastery
                                    )
                                }
                                composable(LogistikRoutes.EXAM) {
                                    ExamSimulatorScreen(
                                        examViewModel = examViewModel,
                                        examAttempts = ui.examAttempts,
                                        onStartMcExam = {
                                            learnViewModel.startMcExamSimulation(25, 30)
                                            navigateTo(LogistikRoutes.LEARN)
                                        },
                                        voiceController = voiceController
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showExaminerTips) {
        ExaminerTipsDialog(onDismiss = { showExaminerTips = false })
    }

    if (showEditNameDialog) {
        val currentName = readyUi?.profile?.displayName ?: userDisplayName
        EditCandidateNameDialog(
            initialName = currentName,
            onDismiss = { showEditNameDialog = false },
            onSave = { updatedName ->
                mainViewModel.updateDisplayName(updatedName)
                showEditNameDialog = false
            }
        )
    }
}

@Composable
private fun EditCandidateNameDialog(
    initialName: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var nameInput by remember(initialName) { mutableStateOf(initialName) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Kandidatenname anpassen") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Dein Fortschritt wird lokal auf diesem Gerät gespeichert – ganz ohne Anmeldung.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it.take(80) },
                    label = { Text("Name") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("candidate_name_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(nameInput) },
                modifier = Modifier.testTag("save_candidate_name_button")
            ) {
                Text("Speichern")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Abbrechen")
            }
        }
    )
}

@Composable
private fun ExaminerTipsDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Prüfungstipps vom IHK-Prüfer") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ExaminerTipsCatalog.tips.forEach { tip ->
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Surface(
                                    color = SkyCognitive.copy(alpha = 0.16f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = tip.badge,
                                        fontFamily = JetBrainsMonoFontFamily,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SkyCognitive,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Text(text = tip.title, style = MaterialTheme.typography.titleSmall)
                            }
                            Text(
                                text = tip.summary,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            tip.bulletPoints.forEach { bullet ->
                                Text(text = "• $bullet", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                Text(text = "IHK-Operatoren-Kompass", style = MaterialTheme.typography.titleSmall, color = SignalOrange)
                ExaminerTipsCatalog.operators.forEach { op ->
                    Text(
                        text = "• ${op.operator}: ${op.expectation} (${op.tip})",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                Text(text = "Wichtige Gesetze & Normen (Stand 2025/2026)", style = MaterialTheme.typography.titleSmall, color = SkyCognitive)
                ExaminerTipsCatalog.keyNorms.forEach { norm ->
                    Text(
                        text = "• ${norm.norm}: ${norm.scope}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Schließen")
            }
        }
    )
}
