package feature.palette.photoPicker

import com.arkivanov.mvikotlin.core.store.Store
import core.model.Image
import feature.palette.model.ColorPalette
import feature.palette.photoPicker.domain.ExtractionMethod

interface PhotoPickerStore : Store<PhotoPickerStore.Intent, PhotoPickerStore.State, Nothing> {
    sealed class Intent {
        data class ExtractPalette(
            val image: Image,
            val colorCount: Int,
            val extractionMethod: ExtractionMethod
        ): Intent()
        data object CancelExtraction: Intent()
        data class SavePaletteFromExtraction(val palette: ColorPalette): Intent()
        data object LoadImage: Intent()
        data object ImageNotLoaded: Intent()
    }

    data class State(
        val isLoading: Boolean = false,
        val selectedImagePath: String? = null,
        val extractedPalette: ColorPalette? = null,
        val error: String? = null,
        val navigateToPalettePage: Boolean = false,
        val loadImage: Boolean = false
    )

    sealed class Msg {
        data object LoadImage: Msg()
        data object PaletteExtractionStarted : Msg()
        data class PaletteExtracted(val palette: ColorPalette) : Msg()
        data object ImageNotLoaded: Msg()
        data object ExtractionCanceled : Msg()
        data class Error(val message: String) : Msg()
        data object Loading : Msg()
        data object NavigateToSavedPalettePage: Msg()
    }
}