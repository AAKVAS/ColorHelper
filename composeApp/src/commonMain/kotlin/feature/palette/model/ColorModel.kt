package feature.palette.model

import kotlinx.serialization.Serializable

@Serializable
data class ColorModel(
    val uid: String,
    val paletteUid: String,
    val value: String,
    val createdAt: Long
)
