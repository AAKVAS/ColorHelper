package feature.imageBusket

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import feature.imageBusket.data.ImageData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultImageBusketComponent(
    componentContext: ComponentContext,
    storeFactory: ImageBusketStoreFactory,
    private val onClose: () -> Unit
) : ImageBusketComponent, ComponentContext by componentContext {
    private val _store = instanceKeeper.getStore {
        storeFactory.create()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<ImageBusketStore.State> = _store.stateFlow

    init {
        _store.accept(ImageBusketStore.Intent.LoadState)
        CoroutineScope(Dispatchers.IO).launch {
            state.collect {
                if (it.stateSaved) {
                    closeWindow()
                }
            }
        }
    }


    override fun addImage(image: ImageData) {
        _store.accept(ImageBusketStore.Intent.AddImage(image))
    }

    override fun deleteImage(index: Int) {
        _store.accept(ImageBusketStore.Intent.DeleteImage(index))
    }

    override fun clear() {
        _store.accept(ImageBusketStore.Intent.Clear)
    }

    override fun changeSelectedImageIndex(index: Int) {
        _store.accept(ImageBusketStore.Intent.ChangeSelectedImageIndex(index))
    }

    override fun onWindowClosing(): Boolean {
        if (state.value.items.isEmpty()) {
            return true
        } else {
            _store.accept(ImageBusketStore.Intent.ShowSaveDialog)
            return false
        }
    }

    override fun closeSaveDialog() {
        _store.accept(ImageBusketStore.Intent.CloseSaveDialog)
    }

    override fun closeWithoutSaving() {
        closeWindow()
    }

    override fun closeWithSaving() {
        _store.accept(ImageBusketStore.Intent.SaveState)
    }

    fun closeWindow() {
        onClose()
    }
}