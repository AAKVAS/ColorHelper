package feature.palette.photoPicker
import android.graphics.Color
import coil3.Bitmap
import core.model.Image
import core.model.RGBPixel


fun Bitmap.toImage(path: String, sampleStep: Int = 4): Image {
    val pixels = mutableListOf<RGBPixel>()

    val originalWidth = this.width
    val originalHeight = this.height

    val newWidth = (originalWidth + sampleStep - 1) / sampleStep
    val newHeight = (originalHeight + sampleStep - 1) / sampleStep

    for (y in 0 until originalHeight step sampleStep) {
        for (x in 0 until originalWidth step sampleStep) {
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

    return Image(
        path = path,
        width = newWidth,
        height = newHeight,
        pixels = pixels
    )
}
