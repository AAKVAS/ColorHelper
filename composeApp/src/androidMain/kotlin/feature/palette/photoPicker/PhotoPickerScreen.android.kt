package feature.palette.photoPicker
import android.graphics.Color
import coil3.Bitmap
import core.model.Image
import core.model.RGBPixel


fun Bitmap.toImage(path: String, maxDimension: Int = 2000): Image {
    val originalWidth = this.width
    val originalHeight = this.height

    val sampleStep = if (originalWidth > maxDimension || originalHeight > maxDimension) {
        (maxOf(originalWidth, originalHeight) / maxDimension).coerceAtLeast(1)
    } else {
        1
    }

    val pixels = mutableListOf<RGBPixel>()

    val newWidth = originalWidth / sampleStep
    val newHeight = originalHeight / sampleStep

    for (x in 0 until originalWidth step sampleStep) {
        for (y in 0 until originalHeight step sampleStep) {
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
