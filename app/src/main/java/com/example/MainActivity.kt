package com.example

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.core.configureAppCheck
import com.example.data.repository.LearningRepository
import com.example.ui.navigation.LogistikAppShell
import com.example.ui.theme.AppThemeMode
import com.example.ui.theme.LogistikMeisterTheme

private const val PREFS_UI = "xels_logistik_ui_prefs"
private const val KEY_THEME_MODE = "theme_mode"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mandatory Step 1: Configure App Check with launch intent before AI ViewModels run
        configureAppCheck(this, intent)

        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val prefs = remember(context) {
                context.getSharedPreferences(PREFS_UI, Context.MODE_PRIVATE)
            }
            var themeMode by remember {
                val saved = prefs.getString(KEY_THEME_MODE, AppThemeMode.SYSTEM.name)
                mutableStateOf(
                    runCatching { AppThemeMode.valueOf(saved ?: AppThemeMode.SYSTEM.name) }
                        .getOrDefault(AppThemeMode.SYSTEM)
                )
            }

            LogistikMeisterTheme(themeMode = themeMode) {
                AppNavigation(
                    themeMode = themeMode,
                    onSelectThemeMode = { next ->
                        themeMode = next
                        prefs.edit().putString(KEY_THEME_MODE, next.name).apply()
                    }
                )
            }
        }
    }
}

@Composable
fun AppNavigation(
    themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    onSelectThemeMode: (AppThemeMode) -> Unit = {}
) {
    LogistikAppShell(
        currentUserId = LearningRepository.DEFAULT_LOCAL_USER_ID,
        userDisplayName = LearningRepository.DEFAULT_DISPLAY_NAME,
        themeMode = themeMode,
        onSelectThemeMode = onSelectThemeMode
    )
}
