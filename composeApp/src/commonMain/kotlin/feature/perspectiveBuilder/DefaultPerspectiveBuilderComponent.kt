package feature.perspectiveBuilder

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import core.model.Image
import core.model.ImageSource
import feature.perspectiveBuilder.model.PerspectivePoint
import feature.perspectiveBuilder.model.PerspectiveScene
import feature.perspectiveBuilder.model.SceneSamples
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.abs

class DefaultPerspectiveBuilderComponent(
    componentContext: ComponentContext,
    storeFactory: PerspectiveBuilderStoreFactory,
) : PerspectiveBuilderComponent, ComponentContext by componentContext {
    private val _store = instanceKeeper.getStore {
        storeFactory.create()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<PerspectiveBuilderStore.State>
        get() = _store.stateFlow

    override val labels: Flow<PerspectiveBuilderStore.Label>
        get() = _store.labels

    override fun addPoint(point: PerspectivePoint) {
        val scene = _store.state.scene
        val newScene = scene.copy(points = scene.points + point)
        _store.accept(PerspectiveBuilderStore.Intent.UpdateScene(newScene))
    }

    override fun updateScene(scene: PerspectiveScene) {
        _store.accept(PerspectiveBuilderStore.Intent.UpdateScene(scene))
    }

    override fun removePointByIndex(index: Int) {
        val scene = _store.state.scene
        val newPoints = scene.points.filterIndexed { pIndex, _ ->
            pIndex != index
        }
        val newScene = scene.copy(points = newPoints)
        _store.accept(PerspectiveBuilderStore.Intent.UpdateScene(newScene))
    }

    override fun updatePointByIndex(
        index: Int,
        point: PerspectivePoint
    ) {
        val scene = _store.state.scene
        val newPoint = if (point.isFinite && abs(point.x) > scene.width * 5) {
            PerspectivePoint.horizontal(point.y)
        } else if (point.isFinite && abs(point.y) > scene.height * 5) {
            PerspectivePoint.horizontal(point.x)
        } else {
            point
        }

        val newPoints = scene.points.mapIndexed { pIndex, p ->
            if (pIndex == index) {
                newPoint
            } else {
                p
            }
        }
        val newScene = scene.copy(points = newPoints)
        _store.accept(PerspectiveBuilderStore.Intent.UpdateScene(newScene))
    }

    override fun changeRayCount(count: Int) {
        val scene = _store.state.scene
        val newScene = scene.copy(rayCount = count)
        _store.accept(PerspectiveBuilderStore.Intent.UpdateScene(newScene))
    }

    override fun changeSelectedPointIndex(index: Int) {
        _store.accept(PerspectiveBuilderStore.Intent.ChangeSelectedPointIndex(index))
    }

    override fun generateSceneFromImage(image: Image) {
        _store.accept(PerspectiveBuilderStore.Intent.GenerateSceneFromImage(image))
    }

    override fun cancelGeneration() {
        _store.accept(PerspectiveBuilderStore.Intent.CancelGeneration)
    }

    override fun changeGridVisibility(visible: Boolean) {
        val scene = _store.state.scene
        val newScene = scene.copy(gridEnabled = visible)
        _store.accept(PerspectiveBuilderStore.Intent.UpdateScene(newScene))
    }

    override fun changePhotoInputVisibility(visible: Boolean) {
        val scene = _store.state.scene
        val newScene = scene.copy(showPhotoInput = visible)
        _store.accept(PerspectiveBuilderStore.Intent.UpdateScene(newScene))
    }

    override fun useSample(sample: SceneSamples) {
        val scene = _store.state.scene

        val centerX = scene.width / 2f
        val centerY = scene.height / 2f
        val leftX = -1f * scene.width
        val rightX = scene.width * 2f
        val topY = scene.height * 3f
        val downY = scene.height * -2f

        val points = when (sample) {
            SceneSamples.ONE_POINT -> {
                listOf(PerspectivePoint(centerX, centerY))
            }

            SceneSamples.TWO_POINT -> {
                listOf(
                    PerspectivePoint(leftX, centerY),
                    PerspectivePoint(rightX, centerY),
                )
            }

            SceneSamples.THREE_POINT_TOP -> {
                listOf(
                    PerspectivePoint(leftX, centerY * 0.75f),
                    PerspectivePoint(rightX, centerY * 0.75f),
                    PerspectivePoint(centerX, topY),
                )
            }

            SceneSamples.THREE_POINT_DOWN -> {
                listOf(
                    PerspectivePoint(leftX, centerY * 1.75f),
                    PerspectivePoint(rightX, centerY * 1.75f),
                    PerspectivePoint(centerX, downY),
                )
            }

            else -> {
                listOf()
            }
        }
        updateScene(scene.copy(points = points))
    }

    override fun updateImageSource(imageSource: ImageSource?) {
        _store.accept(PerspectiveBuilderStore.Intent.UpdateImageSource(imageSource))
    }
}