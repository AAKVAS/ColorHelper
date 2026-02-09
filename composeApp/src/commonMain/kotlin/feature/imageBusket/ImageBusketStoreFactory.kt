package feature.imageBusket

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import feature.imageBusket.data.ImageData
import kotlin.coroutines.CoroutineContext


class ImageBusketStoreFactory(
    private val storeFactory: StoreFactory,
    private val coroutineContext: CoroutineContext,
) {
    fun create(): Store<ImageBusketStore.Intent, ImageBusketStore.State, Nothing> =
        object : Store<ImageBusketStore.Intent, ImageBusketStore.State, Nothing> by storeFactory.create(
            name = "ImageBusketStore",
            initialState = ImageBusketStore.State(),
            executorFactory = ::createExecutor,
            reducer = ReducerImpl()
        ) {}

    private fun createExecutor(): Executor<ImageBusketStore.Intent, Unit, ImageBusketStore.State, ImageBusketStore.Msg, Nothing> =
        ExecutorImpl(coroutineContext)

    private class ExecutorImpl(
        coroutineContext: CoroutineContext
    ) : CoroutineExecutor<ImageBusketStore.Intent, Unit, ImageBusketStore.State, ImageBusketStore.Msg, Nothing>(
        coroutineContext
    ) {
        override fun executeIntent(intent: ImageBusketStore.Intent): Unit =
            when (intent) {
                is ImageBusketStore.Intent.AddImage -> {
                    addImage(intent.imageData)
                }
                is ImageBusketStore.Intent.DeleteImage -> {
                    deleteImage(intent.index)
                }
                is ImageBusketStore.Intent.Clear -> {
                    clear()
                }
                is ImageBusketStore.Intent.ChangeSelectedImageIndex -> {
                    changeSelectedImageIndex(intent.index)
                }
            }

        private fun addImage(imageData: ImageData) {
            dispatch(ImageBusketStore.Msg.ImageAdded(imageData))
        }

        private fun deleteImage(index: Int) {
            dispatch(ImageBusketStore.Msg.ImageDeleted(index))
        }

        private fun clear() {
            dispatch(ImageBusketStore.Msg.Clear)
        }

        private fun changeSelectedImageIndex(index: Int) {
            dispatch(ImageBusketStore.Msg.ChangeSelectedImageIndex(index))
        }
    }

    private class ReducerImpl : Reducer<ImageBusketStore.State, ImageBusketStore.Msg> {
        override fun ImageBusketStore.State.reduce(msg: ImageBusketStore.Msg): ImageBusketStore.State {
            return when(msg) {
                is ImageBusketStore.Msg.ImageAdded -> copy(
                    items = items + msg.imageData,
                    selectedImageIndex = items.size
                )
                is ImageBusketStore.Msg.ImageDeleted -> copy(
                    items = items.filterIndexed { itemIndex, _ ->
                        itemIndex != msg.index
                    },
                    selectedImageIndex = if (
                        selectedImageIndex == msg.index
                    ) {
                        -1
                    } else if (selectedImageIndex > msg.index) {
                        selectedImageIndex - 1
                    } else {
                        selectedImageIndex
                    }
                )
                is ImageBusketStore.Msg.Clear -> copy(
                    items = listOf(),
                    selectedImageIndex = -1
                )
                is ImageBusketStore.Msg.ChangeSelectedImageIndex -> {
                    copy(selectedImageIndex = msg.index)
                }
            }
        }
    }
}