package feature.palette.photoPicker.model

import androidx.compose.ui.graphics.Color

data class RGBPixel(
    val r: Int,
    val g: Int,
    val b: Int
) {
    fun toColor(): Color = Color(r / 255f, g / 255f, b / 255f)
}
