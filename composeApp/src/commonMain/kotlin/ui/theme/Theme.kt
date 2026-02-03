package ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class ColorTheme(
    val primary: Color,
    val primaryVariant: Color,
    val primaryWeak: Color,
    val secondary: Color,
    val background: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onSurface: Color,
    val onBackground: Color,
)

val darkColorTheme = ColorTheme(
    primary = md_theme_dark_primary,
    primaryVariant = md_theme_dark_primary_variant,
    primaryWeak = md_theme_dark_primary_weak,
    secondary = md_theme_dark_secondary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    background = md_theme_dark_background,
    onSurface = md_theme_dark_onSurface,
    onBackground = md_theme_dark_onBackground
)

val LocalColorProvider = staticCompositionLocalOf<ColorTheme> {
    error("No default implementation")
}

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalColorProvider provides darkColorTheme
    ) {
        content.invoke()
    }
}