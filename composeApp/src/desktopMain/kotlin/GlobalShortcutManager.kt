import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class GlobalShortcutManager {
    private val _pasteEvents = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    private val _copyEvents = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    val pasteEvents: SharedFlow<Unit> = _pasteEvents.asSharedFlow()
    val copyEvents: SharedFlow<Unit> = _copyEvents.asSharedFlow()

    suspend fun triggerPaste() {
        _pasteEvents.emit(Unit)
    }

    suspend fun triggerCopy() {
        _copyEvents.emit(Unit)
    }
}

val LocalShortcutManager = staticCompositionLocalOf<GlobalShortcutManager> {
    error("No GlobalShortcutManager provided")
}