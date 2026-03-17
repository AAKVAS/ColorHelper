package core.model

import androidx.compose.ui.graphics.Color

data class Image(
    val path: String,
    val width: Int,
    val height: Int,
    val pixels: List<RGBPixel>
)

fun RGBPixel.toColor(): Color = Color(r / 255f, g / 255f, b / 255f, a / 255f)