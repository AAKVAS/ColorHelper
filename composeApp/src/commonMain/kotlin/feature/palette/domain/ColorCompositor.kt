package feature.palette.domain

import androidx.compose.ui.graphics.Color
import feature.palette.model.ColorModel
import utils.hslToColor
import utils.toColor
import utils.toHex
import utils.toHsl
import kotlin.math.abs

class ColorCompositor {
    fun getHarmoniousColors(colors: List<ColorModel>): List<String> {
        val currentColors = colors.map {
            it.value.toColor()
        }

        val harmoniousColors = currentColors.flatMap {
            generateAllColorSchemes(it)
        }.distinct()

        val candidateColors = harmoniousColors.filter { newColor ->
            currentColors.none { existingColor ->
                isSimilarColor(newColor, existingColor)
            }
        }
        .distinct()

        return selectBestColors(currentColors, candidateColors)
        .map{ it.toHex(true) }
    }

    fun generateAllColorSchemes(color: Color): List<Color> {
        val schemes = mutableListOf<Color>()
        schemes.add(getComplementary(color))
        schemes.addAll(generateAnalogous(color))
        schemes.addAll(generateTriadic(color))
        schemes.addAll(generateSplitComplementary(color))

        return schemes
    }

    fun selectBestColors(
        baseColors: List<Color>,
        candidateColors: List<Color>
    ): List<Color> {
        if (candidateColors.size < MAX_COLORS_IN_PALETTE) {
            return candidateColors
        }

        val targetCount = MAX_COLORS_IN_PALETTE
        return when (COLOR_SELECTION_STRATEGY) {
            ColorSelectionStrategy.DIVERSITY -> selectByDiversity(baseColors, candidateColors, targetCount)
            ColorSelectionStrategy.HARMONY -> selectByHarmony(baseColors, candidateColors, targetCount)
            ColorSelectionStrategy.VIBRANCY -> selectByVibrancy(candidateColors, targetCount)
            ColorSelectionStrategy.BALANCED -> selectBalancedColors(baseColors, candidateColors, targetCount)
        }
    }

    private fun selectByDiversity(
        baseColors: List<Color>,
        candidates: List<Color>,
        targetCount: Int
    ): List<Color> {
        val selected = mutableListOf<Color>()

        candidates.sortedByDescending { candidate ->
            baseColors.minOf { base -> getColorDistance(base, candidate) }
        }.forEach { candidate ->
            if (selected.size < targetCount &&
                selected.none { selectedColor -> isSimilarColor(selectedColor, candidate) }) {
                selected.add(candidate)
            }
        }

        return selected
    }

    private fun selectByHarmony(
        baseColors: List<Color>,
        candidates: List<Color>,
        targetCount: Int
    ): List<Color> {
        val scoredCandidates = candidates.map { candidate ->
            val harmonyScore = baseColors.sumOf { base ->
                val distance = getColorDistance(base, candidate)
                1.0 / (distance + 1)
            }
            candidate to harmonyScore
        }

        return scoredCandidates
            .sortedByDescending { it.second }
            .take(targetCount)
            .map { it.first }
    }

    private fun selectByVibrancy(
        candidates: List<Color>,
        targetCount: Int
    ): List<Color> {
        return candidates.sortedByDescending { color ->
            val hsl = color.toHsl()
            hsl.second * hsl.third
        }.take(targetCount)
    }


    private fun selectBalancedColors(
        baseColors: List<Color>,
        candidates: List<Color>,
        targetCount: Int
    ): List<Color> {
        if (candidates.isEmpty()) return emptyList()

        val hueGroups = candidates.groupBy { color ->
            val hue = color.toHsl().first
            ((hue * 12).toInt() % 12)
        }

        val selected = mutableListOf<Color>()

        hueGroups.values.forEach { group ->
            val bestFromGroup = group.maxByOrNull { color ->
                val hsl = color.toHsl()
                val contrastScore = if (baseColors.isNotEmpty()) {
                    baseColors.maxOf { base -> getColorDistance(base, color) }
                } else {
                    1.0
                }
                hsl.second * contrastScore
            }

            bestFromGroup?.let {
                if (selected.size < targetCount &&
                    selected.none { selectedColor -> isSimilarColor(selectedColor, it) }) {
                    selected.add(it)
                }
            }
        }

        if (selected.size < targetCount) {
            val remaining = candidates.filter { it !in selected }
                .sortedByDescending { candidate ->
                    baseColors.sumOf { base ->
                        1.0 / (getColorDistance(base, candidate) + 1)
                    }
                }

            remaining.forEach { candidate ->
                if (selected.size < targetCount &&
                    selected.none { selectedColor -> isSimilarColor(selectedColor, candidate) }) {
                    selected.add(candidate)
                }
            }
        }

        return selected
    }

    private fun getColorDistance(color1: Color, color2: Color): Double {
        val hsl1 = color1.toHsl()
        val hsl2 = color2.toHsl()

        val hueDiff = minOf(
            abs(hsl1.first - hsl2.first),
            1 - abs(hsl1.first - hsl2.first)
        )

        val satDiff = abs(hsl1.second - hsl2.second)
        val lightDiff = abs(hsl1.third - hsl2.third)

        return hueDiff * 2.0 + satDiff * 1.0 + lightDiff * 1.5
    }

    fun getComplementary(color: Color): Color {
        val hsl = color.toHsl()

        val complementaryHsl = Triple(
            (hsl.first + 0.5f) % 1.0f,
            hsl.second,
            hsl.third
        )

        return hslToColor(complementaryHsl)
    }

    fun generateAnalogous(color: Color): List<Color> {
        val hsl = color.toHsl()
        val (h, s, l) = hsl

        val leftHue = (h - ANALOGOUS_OFFSET + 1f) % 1f
        val rightHue = (h + ANALOGOUS_OFFSET) % 1f

        return listOf(
            hslToColor(Triple(leftHue, s, l)),
            hslToColor(Triple(rightHue, s, l))
        )
    }

    fun generateTriadic(color: Color): List<Color> {
        val hsl = color.toHsl()
        val (h, s, l) = hsl

        val triad1Hue = (h + 1f/3f) % 1f
        val triad2Hue = (h + 2f/3f) % 1f

        return listOf(
            hslToColor(Triple(triad1Hue, s, l)),
            hslToColor(Triple(triad2Hue, s, l))
        )
    }

    fun generateSplitComplementary(color: Color): List<Color> {
        val hsl = color.toHsl()
        val (h, s, l) = hsl

        val complementaryHue = (h + 0.5f) % 1f

        val leftHue = (complementaryHue - ANALOGOUS_OFFSET + 1f) % 1f
        val rightHue = (complementaryHue + ANALOGOUS_OFFSET) % 1f

        return listOf(
            hslToColor(Triple(leftHue, s, l)),
            hslToColor(Triple(rightHue, s, l))
        )
    }

    private fun isSimilarColor(color1: Color, color2: Color): Boolean {
        val r1 = (color1.red * 255).toInt()
        val g1 = (color1.green * 255).toInt()
        val b1 = (color1.blue * 255).toInt()

        val r2 = (color2.red * 255).toInt()
        val g2 = (color2.green * 255).toInt()
        val b2 = (color2.blue * 255).toInt()

        val totalDiff = abs(r1 - r2) + abs(g1 - g2) + abs(b1 - b2)
        return totalDiff <= COLOR_SIMILARITY_THRESHOLD
    }

    companion object {
        private const val ANALOGOUS_OFFSET = 30f / 360f
        private const val COLOR_SIMILARITY_THRESHOLD = 6
        private const val MAX_COLORS_IN_PALETTE = 8
        private val COLOR_SELECTION_STRATEGY = ColorSelectionStrategy.BALANCED
    }

    enum class ColorSelectionStrategy {
        DIVERSITY,
        HARMONY,
        VIBRANCY,
        BALANCED
    }
}