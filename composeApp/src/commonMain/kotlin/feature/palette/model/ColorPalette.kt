package feature.palette.model

import kotlinx.serialization.Serializable

@Serializable
data class ColorPalette(
    val uid: String,
    val name: String,
    val colors: List<ColorModel>,
    val createdAt: Long
)
