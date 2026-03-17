package feature.palette.photoPicker

import core.model.Image
import feature.palette.photoPicker.domain.ExtractionMethod
import kotlinx.coroutines.flow.StateFlow

interface PhotoPickerComponent {
    val state: StateFlow<PhotoPickerStore.State>

    fun loadImage()
    fun extractImage(image: Image, colorCount: Int, method: ExtractionMethod)
    fun imageNotLoaded()
    fun onSavePalette()
    fun onCancel()
}