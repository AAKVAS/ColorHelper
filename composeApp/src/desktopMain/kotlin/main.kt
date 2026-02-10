import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.type
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import di.initKoin
import feature.home.DefaultRootComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import utils.runOnUiThread

fun main() {
    initKoin()
    val lifecycle = LifecycleRegistry()
    val root =
        runOnUiThread {
            DefaultRootComponent(
                componentContext = DefaultComponentContext(lifecycle = lifecycle),
                withImageBusket = true
            )
        }

    application {
        val state = rememberWindowState()
        val windowsSize = remember { mutableStateOf(state.size) }
        val shortcutManager = remember { GlobalShortcutManager() }

        Window(
            onCloseRequest = {
                if (root.onWindowClosing()) {
                    exitApplication()
                }
            },
            state = state,
            title = "ColorHelper",
            onKeyEvent = { keyEvent ->
                if (keyEvent.type == KeyEventType.KeyDown) {
                    when {
                        (keyEvent.key == Key.V && (keyEvent.isCtrlPressed || keyEvent.isMetaPressed)) -> {
                            CoroutineScope(Dispatchers.Main).launch {
                                shortcutManager.triggerPaste()
                            }
                            true
                        }
                        (keyEvent.key == Key.C && (keyEvent.isCtrlPressed || keyEvent.isMetaPressed)) -> {
                            CoroutineScope(Dispatchers.Main).launch {
                                shortcutManager.triggerCopy()
                            }
                            true
                        }
                        else -> false
                    }
                } else false
            }
        ) {
            LaunchedEffect(state.size) {
                windowsSize.value = state.size
            }
            LaunchedEffect(Unit) {
                root.closeConfirmed.collect { close ->
                    if (close) {
                        exitApplication()
                    }
                }
            }
            CompositionLocalProvider(LocalShortcutManager provides shortcutManager) {
                App(
                    rootComponent = root,
                    windowSize = windowsSize.value,
                )
            }
        }
    }
}

