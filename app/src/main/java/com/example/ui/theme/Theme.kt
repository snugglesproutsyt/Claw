package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val ArcadeColorScheme = darkColorScheme(
    primary = ArcadeGold,
    onPrimary = ArcadeBackground,
    primaryContainer = ArcadeSurfaceHighlight,
    onPrimaryContainer = ArcadeGold,
    secondary = CyberCyan,
    onSecondary = ArcadeBackground,
    secondaryContainer = ArcadeSurfaceVariant,
    onSecondaryContainer = CyberCyanLight,
    tertiary = NeonPink,
    onTertiary = ArcadeBackground,
    tertiaryContainer = ArcadeSurfaceVariant,
    onTertiaryContainer = NeonPinkLight,
    background = ArcadeBackground,
    onBackground = TextPrimary,
    surface = ArcadeSurface,
    onSurface = TextPrimary,
    surfaceVariant = ArcadeSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    error = HazardRed,
    onError = TextPrimary
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = ArcadeBackground.toArgb()
                window.navigationBarColor = ArcadeBackground.toArgb()
                val controller = WindowCompat.getInsetsController(window, view)
                controller.isAppearanceLightStatusBars = false
                controller.isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = ArcadeColorScheme,
        typography = Typography,
        content = content
    )
}
