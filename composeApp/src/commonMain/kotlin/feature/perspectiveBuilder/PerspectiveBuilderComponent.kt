package feature.perspectiveBuilder

import feature.perspectiveBuilder.model.PerspectivePoint
import feature.perspectiveBuilder.model.PerspectiveScene
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface PerspectiveBuilderComponent {
    val state: StateFlow<PerspectiveBuilderStore.State>
    val labels: Flow<PerspectiveBuilderStore.Label>

    fun addPoint(point: PerspectivePoint)
    fun updateScene(scene: PerspectiveScene)
    fun removePointByIndex(index: Int)
    fun updatePointByIndex(index: Int, point: PerspectivePoint)
    fun changeRayCount(count: Int)
    fun changeSelectedPointIndex(index: Int)
}