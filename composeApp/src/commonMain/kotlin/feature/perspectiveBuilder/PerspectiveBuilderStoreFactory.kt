package feature.perspectiveBuilder

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import core.model.Image
import core.model.ImageSource
import feature.perspectiveBuilder.domain.PerspectiveSceneExtractor
import feature.perspectiveBuilder.model.PerspectiveScene
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.CoroutineContext

class PerspectiveBuilderStoreFactory (
    private val storeFactory: StoreFactory,
    private val coroutineContext: CoroutineContext,
): KoinComponent {
    private val perspectiveSceneExtractor by inject<PerspectiveSceneExtractor>()

    fun create(): Store<PerspectiveBuilderStore.Intent, PerspectiveBuilderStore.State, PerspectiveBuilderStore.Label> =
        object : Store<PerspectiveBuilderStore.Intent, PerspectiveBuilderStore.State, PerspectiveBuilderStore.Label> by storeFactory.create(
            name = "PerspectiveBuilderStore",
            initialState = PerspectiveBuilderStore.State(),
            executorFactory = ::createExecutor,
            reducer = ReducerImpl()
        ) {}

    private fun createExecutor(): Executor<PerspectiveBuilderStore.Intent, Unit, PerspectiveBuilderStore.State, PerspectiveBuilderStore.Msg, PerspectiveBuilderStore.Label> =
        ExecutorImpl(coroutineContext, perspectiveSceneExtractor)

    private class ExecutorImpl(
        coroutineContext: CoroutineContext,
        private val perspectiveSceneExtractor: PerspectiveSceneExtractor
    ) : CoroutineExecutor<PerspectiveBuilderStore.Intent, Unit, PerspectiveBuilderStore.State, PerspectiveBuilderStore.Msg, PerspectiveBuilderStore.Label>(
        coroutineContext
    ) {
        private var generationJob: Job? = null

        override fun executeIntent(intent: PerspectiveBuilderStore.Intent): Unit =
            when (intent) {
                is PerspectiveBuilderStore.Intent.UpdateScene -> {
                    updateScene(intent.scene)
                }
                is PerspectiveBuilderStore.Intent.ChangeSelectedPointIndex -> {
                    changeSelectedPointIndex(intent.index)
                }
                is PerspectiveBuilderStore.Intent.GenerateSceneFromImage -> {
                    generateSceneFromImage(intent.image)
                }
                is PerspectiveBuilderStore.Intent.CancelGeneration -> {
                    cancelGeneration()
                }
                is PerspectiveBuilderStore.Intent.UpdateImageSource -> {
                    updateImageSource(intent.imageSource)
                }
            }

        private fun updateScene(scene: PerspectiveScene) {
            dispatch(PerspectiveBuilderStore.Msg.SceneUpdated(scene))
        }

        private fun changeSelectedPointIndex(index: Int) {
            dispatch(PerspectiveBuilderStore.Msg.ChangeSelectedPointIndex(index))
        }

        private fun generateSceneFromImage(image: Image) {
            if (image.pixels.hashCode() == state().imageKey) {
                return
            }
            generationJob = scope.launch(Dispatchers.Main) {
                dispatch(PerspectiveBuilderStore.Msg.StartGeneratingSceneFromImage(image))
                val points = withContext(Dispatchers.Default) {
                    perspectiveSceneExtractor.extractPerspectiveScene(image)
                }
                dispatch(PerspectiveBuilderStore.Msg.SceneGenerated(image, points))
            }
        }

        private fun cancelGeneration() {
            generationJob?.cancel()
            dispatch(PerspectiveBuilderStore.Msg.CancelGeneration)
        }

        private fun updateImageSource(imageSource: ImageSource?) {
            dispatch(PerspectiveBuilderStore.Msg.ImageSourceUpdated(imageSource))
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
                is PerspectiveBuilderStore.Msg.StartGeneratingSceneFromImage -> copy(
                    scene = scene.copy(points = emptyList()),
                    isLoading = true,
                    selectedPointIndex = -1,
                    imageKey = msg.image.pixels.hashCode()
                )
                is PerspectiveBuilderStore.Msg.SceneGenerated -> copy(
                    scene = scene.copy(
                        points = msg.points,
                        width = msg.image.width,
                        height = msg.image.height,
                    ),
                    isLoading = false,
                )
                is PerspectiveBuilderStore.Msg.CancelGeneration -> copy(
                    isLoading = false,
                    imageKey = null
                )
                is PerspectiveBuilderStore.Msg.ImageSourceUpdated -> copy(
                    imageSource = msg.imageSource
                )
            }
        }
    }
}