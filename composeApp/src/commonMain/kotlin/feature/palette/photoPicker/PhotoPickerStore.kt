package feature.palette.photoPicker

import com.arkivanov.mvikotlin.core.store.Store
import feature.palette.model.ColorPalette
import feature.palette.photoPicker.paletteExtractor.ExtractionMethod
import feature.palette.photoPicker.paletteExtractor.Image

interface PhotoPickerStore : Store<PhotoPickerStore.Intent, PhotoPickerStore.State, Nothing> {
    sealed class Intent {
        data class ExtractPalette(
            val image: Image,
            val colorCount: Int,
            val extractionMethod: ExtractionMethod
        ): Intent()
        object CancelExtraction: Intent()
        data class SavePaletteFromExtraction(val palette: ColorPalette): Intent()
        object LoadImage: Intent()
        object ImageNotLoaded: Intent()
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
        object LoadImage: Msg()
        data class PaletteExtractionStarted(val selectedImagePath: String) : Msg()
        data class PaletteExtracted(val palette: ColorPalette) : Msg()
        object ImageNotLoaded: Msg()
        object ExtractionCanceled : Msg()
        data class Error(val message: String) : Msg()
        object Loading : Msg()
        object NavigateToSavedPalettePage: Msg()
    }
}