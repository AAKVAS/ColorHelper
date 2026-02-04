package feature.palette.photoPicker.domain

import androidx.compose.ui.graphics.Color
import feature.palette.photoPicker.model.RGBPixel

class MedianCutPaletteExtractor : PaletteExtractor() {
    override suspend fun getPalette(pixels: List<RGBPixel>, k: Int, maxIterations: Int): List<Color> {
        if (pixels.isEmpty() || k <= 0) return emptyList()

        val uniquePixels = pixels.distinct()
        if (uniquePixels.size <= k) {
            return uniquePixels.take(k).map { it.toColor() }
        }

        val blocks = mutableListOf(MedianCutBlock(pixels))

        while (blocks.size < k) {
            val blockIndex = findBlockWithMaxRange(blocks)
            val blockToSplit = blocks[blockIndex]

            if (blockToSplit.range <= 0) break

            val (leftBlock, rightBlock) = blockToSplit.split()

            blocks.removeAt(blockIndex)
            blocks.add(leftBlock)
            blocks.add(rightBlock)
        }

        return blocks.map { it.averageColor() }
    }

    private fun findBlockWithMaxRange(blocks: List<MedianCutBlock>): Int {
        var maxRange = -1
        var maxIndex = 0

        for (i in blocks.indices) {
            if (blocks[i].range > maxRange) {
                maxRange = blocks[i].range
                maxIndex = i
            }
        }

        return maxIndex
    }

    private inner class MedianCutBlock(
        private val pixels: List<RGBPixel>,
        private val rangeInfo: ColorRangeInfo
    ) {
        val range: Int = maxOf(
            rangeInfo.maxR - rangeInfo.minR,
            rangeInfo.maxG - rangeInfo.minG,
            rangeInfo.maxB - rangeInfo.minB
        )

        constructor(pixels: List<RGBPixel>) : this(pixels, calculateRangeInfo(pixels))

        fun split(): Pair<MedianCutBlock, MedianCutBlock> {
            val sortedPixels = when (rangeInfo.widestChannel) {
                Channel.RED -> pixels.sortedBy { it.r }
                Channel.GREEN -> pixels.sortedBy { it.g }
                Channel.BLUE -> pixels.sortedBy { it.b }
            }

            val mid = sortedPixels.size / 2

            val leftPixels = sortedPixels.subList(0, mid)
            val rightPixels = sortedPixels.subList(mid, sortedPixels.size)

            return Pair(
                MedianCutBlock(leftPixels),
                MedianCutBlock(rightPixels)
            )
        }

        fun averageColor(): Color {
            var sumR = 0L
            var sumG = 0L
            var sumB = 0L

            for (pixel in pixels) {
                sumR += pixel.r
                sumG += pixel.g
                sumB += pixel.b
            }

            val count = pixels.size
            return RGBPixel(
                (sumR / count).toInt(),
                (sumG / count).toInt(),
                (sumB / count).toInt()
            ).toColor()
        }
    }

    private fun calculateRangeInfo(pixels: List<RGBPixel>): ColorRangeInfo {
        if (pixels.isEmpty()) return ColorRangeInfo(Channel.RED)

        val (minR, maxR) = pixels.minMaxOf { it.r }
        val (minG, maxG) = pixels.minMaxOf { it.g }
        val (minB, maxB) = pixels.minMaxOf { it.b }

        val ranges = mapOf(
            Channel.RED to (maxR - minR),
            Channel.GREEN to (maxG - minG),
            Channel.BLUE to (maxB - minB)
        )

        val widestChannel = ranges.maxByOrNull { it.value }?.key ?: Channel.RED

        return ColorRangeInfo(widestChannel, minR, maxR, minG, maxG, minB, maxB)
    }

    private fun <T> Iterable<T>.minMaxOf(selector: (T) -> Int): Pair<Int, Int> {
        var min = Int.MAX_VALUE
        var max = Int.MIN_VALUE
        for (item in this) {
            val value = selector(item)
            if (value < min) min = value
            if (value > max) max = value
        }
        return min to max
    }

    private enum class Channel { RED, GREEN, BLUE }

    private data class ColorRangeInfo(
        val widestChannel: Channel,
        val minR: Int = 0,
        val maxR: Int = 0,
        val minG: Int = 0,
        val maxG: Int = 0,
        val minB: Int = 0,
        val maxB: Int = 0,
    )
}