package feature.palette.photoPicker
import android.graphics.Color
import coil3.Bitmap
import feature.palette.photoPicker.model.Image
import feature.palette.photoPicker.model.RGBPixel


fun Bitmap.toImage(path: String, sampleStep: Int = 4): Image {
    val pixels = mutableListOf<RGBPixel>()
    val width = this.width
    val height = this.height

    for (x in 0 until width step sampleStep) {
        for (y in 0 until height step sampleStep) {
            val color = getPixel(x, y)
            pixels.add(
                RGBPixel(
                    r = Color.red(color),
                    g = Color.green(color),
                    b = Color.blue(color)
                )
            )
        }
    }

    return Image(path, width, height, pixels)
}
