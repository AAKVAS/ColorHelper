package feature.imageBusket


import feature.imageBusket.data.ImageData
import kotlinx.coroutines.flow.StateFlow

interface ImageBusketComponent {
    val state: StateFlow<ImageBusketStore.State>

    fun addImage(image: ImageData)
    fun deleteImage(index: Int)
    fun changeSelectedImageIndex(index: Int)
    fun clear()
}