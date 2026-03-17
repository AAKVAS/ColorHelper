package feature.perspectiveBuilder

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import core.model.Image
import feature.perspectiveBuilder.model.PerspectivePoint
import feature.perspectiveBuilder.model.PerspectiveScene
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
}