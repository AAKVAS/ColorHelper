import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.DpSize
import ui.screens.ColorHelperScreen
import ui.theme.AppTheme

@Composable
fun App(windowSize: DpSize) {
    AppTheme {
        ColorHelperScreen(windowSize)
    }
}