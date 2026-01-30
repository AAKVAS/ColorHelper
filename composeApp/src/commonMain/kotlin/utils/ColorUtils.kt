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

fun Color.toHsl(): Triple<Float, Float, Float> {
    val max = maxOf(this.red, this.green, this.blue)
    val min = minOf(this.red, this.green, this.blue)
    var h: Float
    val s: Float
    val l = (max + min) / 2f

    if (max == min) {
        h = 0f
        s = 0f
    } else {
        val d = max - min
        s = if (l > 0.5f) d / (2f - max - min) else d / (max + min)

        h = when (max) {
            this.red -> (this.green - this.blue) / d + (if (this.green < this.blue) 6f else 0f)
            this.green -> (this.blue - this.red) / d + 2f
            else -> (this.red - this.green) / d + 4f
        }
        h /= 6f
    }

    return Triple(h, s, l)
}

fun hslToColor(hsl: Triple<Float, Float, Float>): Color {
    var (h, s, l) = hsl

    h = h.coerceIn(0f, 1f)
    s = s.coerceIn(0f, 1f)
    l = l.coerceIn(0f, 1f)

    return if (s <= 0.001f) {
        Color(red = l, green = l, blue = l)
    } else {
        val q = if (l < 0.5f) l * (1f + s) else l + s - l * s
        val p = 2f * l - q

        fun hueToRgb(t: Float): Float {
            var tempT = t
            if (tempT < 0f) tempT += 1f
            if (tempT > 1f) tempT -= 1f

            return when {
                tempT < 1f/6f -> p + (q - p) * 6f * tempT
                tempT < 0.5f -> q
                tempT < 2f/3f -> p + (q - p) * (2f/3f - tempT) * 6f
                else -> p
            }
        }

        Color(
            red = hueToRgb(h + 1f/3f),
            green = hueToRgb(h),
            blue = hueToRgb(h - 1f/3f)
        )
    }
}

fun hslToHex(hsl: Triple<Float, Float, Float>): String {
    val (h, s, l) = hsl

    val q = if (l < 0.5f) l * (1 + s) else l + s - l * s
    val p = 2 * l - q

    val r = hueToRgb(p, q, h + 1f/3f)
    val g = hueToRgb(p, q, h)
    val b = hueToRgb(p, q, h - 1f/3f)

    return String.format("#FF%02X%02X%02X",
        (r * 255).toInt(),
        (g * 255).toInt(),
        (b * 255).toInt()
    )
}


fun hueToRgb(p: Float, q: Float, t: Float): Float {
    var tempT = t
    if (tempT < 0f) tempT += 1f
    if (tempT > 1f) tempT -= 1f

    return when {
        tempT < 1f/6f -> p + (q - p) * 6f * tempT
        tempT < 0.5f -> q
        tempT < 2f/3f -> p + (q - p) * (2f/3f - tempT) * 6f
        else -> p
    }
}
