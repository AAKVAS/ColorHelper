package feature.perspectiveBuilder

import com.arkivanov.mvikotlin.core.store.Store
import feature.perspectiveBuilder.model.PerspectiveScene

interface PerspectiveBuilderStore
    : Store<PerspectiveBuilderStore.Intent, PerspectiveBuilderStore.State, PerspectiveBuilderStore.Label> {
    sealed class Intent {
        data class UpdateScene(val scene: PerspectiveScene): Intent()
        data class ChangeSelectedPointIndex(val index: Int): Intent()
    }

    data class State(
        val scene: PerspectiveScene = PerspectiveScene(),
        val isLoading: Boolean = false,
        val error: String? = null,
        val selectedPointIndex: Int = -1
    )

    sealed class Label {
        data class ShowMessage(val message: String) : Label()
    }

    sealed class Msg {
        data class Error(val message: String) : Msg()
        data class SceneUpdated(val scene: PerspectiveScene) : Msg()
        data class ChangeSelectedPointIndex(val index: Int): Msg()
    }
}