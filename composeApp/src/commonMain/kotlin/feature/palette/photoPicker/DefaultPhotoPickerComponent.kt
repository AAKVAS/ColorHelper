package feature.palette.photoPicker

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import feature.palette.model.ColorPalette
import feature.palette.photoPicker.domain.ExtractionMethod
import feature.palette.photoPicker.model.Image
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow

class DefaultPhotoPickerComponent(
    componentContext: ComponentContext,
    storeFactory: PhotoPickerStoreFactory,
    private val navToPalette: (ColorPalette) -> Unit,
    private val onExtractionCancel: () -> Unit
) : PhotoPickerComponent, ComponentContext by componentContext {
    private val _store = instanceKeeper.getStore(key = INSTANCE_KEEPER_KEY) {
        storeFactory.create()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<PhotoPickerStore.State> = _store.stateFlow

    override fun loadImage() {
        _store.accept(PhotoPickerStore.Intent.LoadImage)
    }

    override fun onSavePalette() {
        _store.state.extractedPalette?.let {
            _store.accept(PhotoPickerStore.Intent.SavePaletteFromExtraction(it))
            resetStore()
            navToPalette(it)
        }
    }

    override fun onCancel() {
        _store.accept(PhotoPickerStore.Intent.CancelExtraction)
        onExtractionCancel()
    }

    override fun extractImage(
        image: Image,
        colorCount: Int,
        method: ExtractionMethod
    ) {
        _store.accept(PhotoPickerStore.Intent.ExtractPalette(
            image = image,
            colorCount = colorCount,
            extractionMethod = method
        ))
    }

    override fun imageNotLoaded() {
        _store.accept(PhotoPickerStore.Intent.ImageNotLoaded)
    }

    private fun resetStore() {
        instanceKeeper.remove(key = INSTANCE_KEEPER_KEY)
    }

    companion object  {
        const val INSTANCE_KEEPER_KEY = "PhotoPickerStore"
    }
}