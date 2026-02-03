package feature.palette.photoPicker.paletteExtractor

import androidx.compose.ui.graphics.Color

class MedianCutPaletteExtractor : PaletteExtractor() {
    override suspend fun getPalette(pixels: List<RGBPixel>, k: Int, maxIterations: Int): List<Color> {
        if (pixels.isEmpty() || k <= 0) return emptyList()

        // Начинаем с одного блока, содержащего все пиксели
        val blocks = mutableListOf(pixels.toMutableList())

        // Рекурсивно делим блоки пока не получим k блоков
        while (blocks.size < k) {
            // Находим блок с наибольшим диапазоном цветов
            val blockToSplit = blocks.maxByOrNull { colorRange(it) } ?: break
            blocks.remove(blockToSplit)

            // Находим канал (R, G или B) с наибольшим диапазоном
            val channel = findWidestChannel(blockToSplit)

            // Сортируем пиксели по этому каналу
            blockToSplit.sortBy { it.getChannel(channel) }

            // Делим пополам
            val mid = blockToSplit.size / 2
            val left = blockToSplit.subList(0, mid).toMutableList()
            val right = blockToSplit.subList(mid, blockToSplit.size).toMutableList()

            blocks.add(left)
            blocks.add(right)
        }

        // Для каждого блока вычисляем средний цвет
        return blocks.map { block ->
            averageColor(block)
        }
    }

    private fun colorRange(pixels: List<RGBPixel>): Int {
        val minR = pixels.minOf { it.r }
        val maxR = pixels.maxOf { it.r }
        val minG = pixels.minOf { it.g }
        val maxG = pixels.maxOf { it.g }
        val minB = pixels.minOf { it.b }
        val maxB = pixels.maxOf { it.b }

        val rangeR = maxR - minR
        val rangeG = maxG - minG
        val rangeB = maxB - minB

        return maxOf(rangeR, rangeG, rangeB)
    }

    private fun findWidestChannel(pixels: List<RGBPixel>): Channel {
        val minR = pixels.minOf { it.r }
        val maxR = pixels.maxOf { it.r }
        val minG = pixels.minOf { it.g }
        val maxG = pixels.maxOf { it.g }
        val minB = pixels.minOf { it.b }
        val maxB = pixels.maxOf { it.b }

        val rangeR = maxR - minR
        val rangeG = maxG - minG
        val rangeB = maxB - minB

        return when (maxOf(rangeR, rangeG, rangeB)) {
            rangeR -> Channel.RED
            rangeG -> Channel.GREEN
            else -> Channel.BLUE
        }
    }

    private fun averageColor(pixels: List<RGBPixel>): Color {
        if (pixels.isEmpty()) return Color.Black

        val avgR = pixels.map { it.r }.average().toInt()
        val avgG = pixels.map { it.g }.average().toInt()
        val avgB = pixels.map { it.b }.average().toInt()

        return RGBPixel(avgR, avgG, avgB).toColor()
    }

    private fun RGBPixel.getChannel(channel: Channel): Int {
        return when (channel) {
            Channel.RED -> r
            Channel.GREEN -> g
            Channel.BLUE -> b
        }
    }

    private enum class Channel { RED, GREEN, BLUE }
}