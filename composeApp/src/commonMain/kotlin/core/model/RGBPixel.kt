package core.model

@JvmInline
value class RGBPixel(private val packed: Int) {
    constructor(r: Int, g: Int, b: Int, a: Int = 255) : this(
        ((a and 0xFF) shl 24) or
                ((r and 0xFF) shl 16) or
                ((g and 0xFF) shl 8) or
                (b and 0xFF)
    )

    val a: Int get() = (packed ushr 24) and 0xFF
    val r: Int get() = (packed shr 16) and 0xFF
    val g: Int get() = (packed shr 8) and 0xFF
    val b: Int get() = packed and 0xFF

    val hasAlpha: Boolean get() = a < 255

    override fun toString(): String = "RGBPixel(r=$r, g=$g, b=$b)"

    companion object {
        val BLACK = RGBPixel(0)
        val WHITE = RGBPixel(r = 255, g = 255, b = 255)
    }
}