package core.model

import androidx.compose.ui.graphics.ImageBitmap

sealed interface ImageSource {
    data class Path(val value: String): ImageSource
    data class BitmapSource(val value: ImageBitmap): ImageSource
}