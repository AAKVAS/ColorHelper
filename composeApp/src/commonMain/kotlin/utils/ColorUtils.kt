package utils

import androidx.compose.ui.graphics.Color
import com.zakgof.korender.math.ColorRGB
import com.zakgof.korender.math.ColorRGBA
import kotlin.math.roundToInt


fun Color.toRGBA(): ColorRGBA {
    return ColorRGBA(
        r = this.red,
        g = this.green,
        b = this.blue,
        a = this.alpha
    )
}

fun Color.toRGB(): ColorRGB {
    return ColorRGB(
        r = this.red,
        g = this.green,
        b = this.blue
    )
}

fun ColorRGBA.toHex(): String {
    val red = (this.r * 255).roundToInt().coerceIn(0, 255)
    val green = (this.g * 255).roundToInt().coerceIn(0, 255)
    val blue = (this.b * 255).roundToInt().coerceIn(0, 255)
    val alpha = (this.a * 255).roundToInt().coerceIn(0, 255)

    return String.format("#%02X%02X%02X%02X", alpha, red, green, blue)
}

fun Color.toHex(withAlpha: Boolean = false): String {
    val r = (red * 255).roundToInt().coerceIn(0, 255)
    val g = (green * 255).roundToInt().coerceIn(0, 255)
    val b = (blue * 255).roundToInt().coerceIn(0, 255)
    val a = (alpha * 255).roundToInt().coerceIn(0, 255)

    return if (withAlpha) {
        String.format("#%02X%02X%02X%02X", a, r, g, b)
    } else {
        String.format("#%02X%02X%02X", r, g, b)
    }
}

fun String.toColor(): Color {
    val hexColor = this.substring(1)

    if (this.length == 9) {
        val alpha = hexColor.take(2).toInt(16)
        val red = hexColor.substring(2, 4).toInt(16)
        val green = hexColor.substring(4, 6).toInt(16)
        val blue = hexColor.substring(6, 8).toInt(16)
        return Color(red, green, blue, alpha)
    } else {
        val red = hexColor.take(2).toInt(16)
        val green = hexColor.substring(2, 4).toInt(16)
        val blue = hexColor.substring(4, 6).toInt(16)
        return Color(red, green, blue)
    }
}

