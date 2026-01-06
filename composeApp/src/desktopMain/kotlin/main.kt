import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    val state = rememberWindowState()
    val windowsSize = remember { mutableStateOf(state.size) }

    Window(
        onCloseRequest = ::exitApplication,
        state = state,
        title = "ColorHelper"
    ) {
        LaunchedEffect(state.size) {
            windowsSize.value = state.size
        }
        App(windowsSize.value)
    }
}