package feature.perspectiveBuilder

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import feature.perspectiveBuilder.model.PerspectivePoint
import feature.perspectiveBuilder.model.PerspectiveScene
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

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
        val newPoints = scene.points.mapIndexed { pIndex, p ->
            if (pIndex == index) {
                point
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
}