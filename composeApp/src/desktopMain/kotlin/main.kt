import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import di.initKoin
import feature.home.DefaultRootComponent
import utils.runOnUiThread

fun main() {
    initKoin()
    val lifecycle = LifecycleRegistry()
    val root =
        runOnUiThread {
            DefaultRootComponent(
                componentContext = DefaultComponentContext(lifecycle = lifecycle),
            )
        }

    application {
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
            App(
                rootComponent = root,
                windowSize =  windowsSize.value,
            )
        }
    }
}