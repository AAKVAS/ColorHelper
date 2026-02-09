package feature.imageBusket

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import feature.imageBusket.data.ImageData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

class DefaultImageBusketComponent(
    componentContext: ComponentContext,
    storeFactory: ImageBusketStoreFactory
) : ImageBusketComponent, ComponentContext by componentContext {
    private val _store = instanceKeeper.getStore {
        storeFactory.create()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<ImageBusketStore.State> = _store.stateFlow

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
}