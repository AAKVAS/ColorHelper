package feature.palette.photoPicker

import feature.palette.photoPicker.paletteExtractor.ExtractionMethod
import feature.palette.photoPicker.paletteExtractor.Image
import kotlinx.coroutines.flow.StateFlow

interface PhotoPickerComponent {
    val state: StateFlow<PhotoPickerStore.State>

    fun loadImage()
    fun extractImage(image: Image, colorCount: Int, method: ExtractionMethod)
    fun imageNotLoaded()
    fun onSavePalette()
    fun onCancel()
}