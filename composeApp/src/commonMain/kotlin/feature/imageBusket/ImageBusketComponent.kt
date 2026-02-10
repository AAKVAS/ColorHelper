package feature.imageBusket


import feature.home.CloseHandler
import feature.imageBusket.data.ImageData
import kotlinx.coroutines.flow.StateFlow

interface ImageBusketComponent : CloseHandler {
    val state: StateFlow<ImageBusketStore.State>

    fun addImage(image: ImageData)
    fun deleteImage(index: Int)
    fun changeSelectedImageIndex(index: Int)
    fun clear()
    fun closeSaveDialog()
    fun closeWithSaving()
    fun closeWithoutSaving()
}