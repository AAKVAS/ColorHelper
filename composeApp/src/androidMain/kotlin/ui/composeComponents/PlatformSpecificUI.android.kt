package ui.composeComponents


import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
actual fun TooltipWrapper(
    modifier: Modifier,
    tooltip: @Composable (() -> Unit),
    content: @Composable (() -> Unit)
) {
    content()
}

@Composable
actual fun BackHandlerWrapper(
    enabled: Boolean,
    callback: () -> Unit
) {
    BackHandler(enabled = enabled) {
        callback()
    }
}

@Composable
actual fun SetupStatusBar() {
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Black,
            darkIcons = false
        )
    }
}