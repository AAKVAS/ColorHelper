package feature.perspectiveBuilder

import core.model.Image
import core.model.ImageSource
import feature.perspectiveBuilder.model.PerspectivePoint
import feature.perspectiveBuilder.model.PerspectiveScene
import feature.perspectiveBuilder.model.SceneSamples
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
    fun generateSceneFromImage(image: Image)
    fun cancelGeneration()
    fun changePhotoInputVisibility(visible: Boolean)
    fun changeGridVisibility(visible: Boolean)
    fun useSample(sample: SceneSamples)
    fun updateImageSource(imageSource: ImageSource?)
}