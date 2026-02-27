package feature.perspectiveBuilder

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import feature.perspectiveBuilder.model.PerspectiveScene
import kotlin.coroutines.CoroutineContext

class PerspectiveBuilderStoreFactory (
    private val storeFactory: StoreFactory,
    private val coroutineContext: CoroutineContext,
) {
    fun create(): Store<PerspectiveBuilderStore.Intent, PerspectiveBuilderStore.State, PerspectiveBuilderStore.Label> =
        object : Store<PerspectiveBuilderStore.Intent, PerspectiveBuilderStore.State, PerspectiveBuilderStore.Label> by storeFactory.create(
            name = "PerspectiveBuilderStore",
            initialState = PerspectiveBuilderStore.State(),
            executorFactory = ::createExecutor,
            reducer = ReducerImpl()
        ) {}

    private fun createExecutor(): Executor<PerspectiveBuilderStore.Intent, Unit, PerspectiveBuilderStore.State, PerspectiveBuilderStore.Msg, PerspectiveBuilderStore.Label> =
        ExecutorImpl(coroutineContext)

    private class ExecutorImpl(
        coroutineContext: CoroutineContext
    ) : CoroutineExecutor<PerspectiveBuilderStore.Intent, Unit, PerspectiveBuilderStore.State, PerspectiveBuilderStore.Msg, PerspectiveBuilderStore.Label>(
        coroutineContext
    ) {
        override fun executeIntent(intent: PerspectiveBuilderStore.Intent): Unit =
            when (intent) {
                is PerspectiveBuilderStore.Intent.UpdateScene -> {
                    updateScene(intent.scene)
                }
                is PerspectiveBuilderStore.Intent.ChangeSelectedPointIndex -> {
                    changeSelectedPointIndex(intent.index)
                }
            }

        private fun updateScene(scene: PerspectiveScene) {
            dispatch(PerspectiveBuilderStore.Msg.SceneUpdated(scene))
        }

        private fun changeSelectedPointIndex(index: Int) {
            dispatch(PerspectiveBuilderStore.Msg.ChangeSelectedPointIndex(index))
        }
    }

    private class ReducerImpl : Reducer<PerspectiveBuilderStore.State, PerspectiveBuilderStore.Msg> {
        override fun PerspectiveBuilderStore.State.reduce(msg: PerspectiveBuilderStore.Msg): PerspectiveBuilderStore.State {
            return when (msg) {
                is PerspectiveBuilderStore.Msg.SceneUpdated -> copy(
                    isLoading = false,
                    scene = msg.scene,
                    selectedPointIndex = if (this.selectedPointIndex >= msg.scene.points.size) {
                        msg.scene.points.size - 1
                    } else {
                        this.selectedPointIndex
                    },
                )
                is PerspectiveBuilderStore.Msg.Error -> copy(isLoading = false, error = msg.message)
                is PerspectiveBuilderStore.Msg.ChangeSelectedPointIndex -> copy(isLoading = false, selectedPointIndex = msg.index)
            }
        }
    }
}