package feature.imageBusket

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import feature.imageBusket.data.ImageData
import feature.imageBusket.domain.ImageBusketRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.CoroutineContext


class ImageBusketStoreFactory(
    private val storeFactory: StoreFactory,
    private val coroutineContext: CoroutineContext,
) : KoinComponent {
    private val repository by inject<ImageBusketRepository>()

    fun create(): Store<ImageBusketStore.Intent, ImageBusketStore.State, Nothing> =
        object : Store<ImageBusketStore.Intent, ImageBusketStore.State, Nothing> by storeFactory.create(
            name = "ImageBusketStore",
            initialState = ImageBusketStore.State(),
            executorFactory = ::createExecutor,
            reducer = ReducerImpl()
        ) {}

    private fun createExecutor(): Executor<ImageBusketStore.Intent, Unit, ImageBusketStore.State, ImageBusketStore.Msg, Nothing> =
        ExecutorImpl(coroutineContext, repository)

    private class ExecutorImpl(
        coroutineContext: CoroutineContext,
        private val repository: ImageBusketRepository
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
                is ImageBusketStore.Intent.ShowSaveDialog -> {
                    showSaveDialog()
                }
                is ImageBusketStore.Intent.LoadState -> {
                    loadState()
                }
                is ImageBusketStore.Intent.SaveState -> {
                    saveState()
                }
                is ImageBusketStore.Intent.CloseSaveDialog -> {
                    closeSaveDialog()
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

        private fun showSaveDialog() {
            dispatch(ImageBusketStore.Msg.ShowSaveDialog)
        }

        private fun closeSaveDialog() {
            dispatch(ImageBusketStore.Msg.CloseSaveDialog)
        }

        private fun saveState() {
            scope.launch(Dispatchers.Main) {
                withContext(Dispatchers.IO) {
                    repository.saveImages(state().items)
                }
                dispatch(ImageBusketStore.Msg.StateSaved)
            }
        }

        private fun loadState() {
            scope.launch(Dispatchers.Main) {
                val images = withContext(Dispatchers.IO) {
                    repository.getImages()
                }
                dispatch(ImageBusketStore.Msg.StateLoaded(images))
                withContext(Dispatchers.IO) {
                    repository.deleteImages()
                }
            }
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
                is ImageBusketStore.Msg.ShowSaveDialog -> {
                    copy(showSaveDialog = true)
                }
                is ImageBusketStore.Msg.CloseSaveDialog -> {
                    copy(showSaveDialog = false)
                }
                is ImageBusketStore.Msg.StateLoaded -> {
                    copy(items = msg.items)
                }
                is ImageBusketStore.Msg.StateSaved -> {
                    copy(stateSaved = true)
                }
            }
        }
    }
}