package feature.imageBusket

import com.arkivanov.mvikotlin.core.store.Store
import feature.imageBusket.data.ImageData

interface ImageBusketStore : Store<ImageBusketStore.Intent, ImageBusketStore.State, Nothing> {
    sealed class Intent {
        data class AddImage(val imageData: ImageData): Intent()
        data class DeleteImage(val index: Int): Intent()
        data class ChangeSelectedImageIndex(val index: Int): Intent()
        object Clear: Intent()
    }

    data class State(
        val items: List<ImageData> = emptyList(),
        val selectedImageIndex: Int = -1
    )

    sealed class Msg {
        data class ImageAdded(val imageData: ImageData): Msg()
        data class ImageDeleted(val index: Int): Msg()
        data class ChangeSelectedImageIndex(val index: Int): Msg()
        object Clear: Msg()
    }
}