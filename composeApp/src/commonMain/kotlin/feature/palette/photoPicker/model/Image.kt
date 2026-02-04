package feature.palette.photoPicker.model


data class Image(
    val path: String,
    val width: Int,
    val height: Int,
    val pixels: List<RGBPixel>
)
