package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
    primary = SignalOrange,
    onPrimary = Color.White,
    primaryContainer = Color(0xFF431E07),
    onPrimaryContainer = SignalOrangeLight,
    secondary = EmeraldMastery,
    onSecondary = Navy950,
    secondaryContainer = Color(0xFF064E3B),
    onSecondaryContainer = EmeraldLight,
    tertiary = SkyCognitive,
    onTertiary = Navy950,
    tertiaryContainer = Color(0xFF0C4A6E),
    onTertiaryContainer = SkyLight,
    background = Navy950,
    onBackground = Slate50,
    surface = Navy900,
    onSurface = Slate50,
    surfaceVariant = Slate800,
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = Slate700,
    error = ErrorCrimson,
    onError = Color.White,
    errorContainer = Color(0xFF450A0A),
    onErrorContainer = ErrorLight
)

private val LightColorScheme = lightColorScheme(
    primary = SignalOrange,
    onPrimary = Color.White,
    primaryContainer = SignalOrangeLight,
    onPrimaryContainer = SignalOrangeDark,
    secondary = EmeraldMastery,
    onSecondary = Color.White,
    secondaryContainer = EmeraldLight,
    onSecondaryContainer = EmeraldDark,
    tertiary = SkyCognitive,
    onTertiary = Color.White,
    tertiaryContainer = SkyLight,
    onTertiaryContainer = Navy900,
    background = Slate50,
    onBackground = Navy900,
    surface = Color.White,
    onSurface = Navy900,
    surfaceVariant = Slate100,
    onSurfaceVariant = Slate700,
    outline = Slate200,
    error = ErrorCrimson,
    onError = Color.White,
    errorContainer = ErrorLight,
    onErrorContainer = Color(0xFF991B1B)
)

val LogistikShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

enum class AppThemeMode(val label: String) {
    SYSTEM("System"),
    LIGHT("Hell"),
    DARK("Dunkel")
}

@Composable
fun LogistikMeisterTheme(
    themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    darkTheme: Boolean = when (themeMode) {
        AppThemeMode.SYSTEM -> isSystemInDarkTheme()
        AppThemeMode.LIGHT -> false
        AppThemeMode.DARK -> true
    },
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = LogistikTypography,
        shapes = LogistikShapes,
        content = content
    )
}
