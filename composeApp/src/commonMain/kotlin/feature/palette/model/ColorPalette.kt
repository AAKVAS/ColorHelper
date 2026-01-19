package feature.palette.model

import kotlinx.serialization.Serializable

@Serializable
data class ColorPalette(
    val id: String,
    val name: String,
    val colors: List<String>
)
