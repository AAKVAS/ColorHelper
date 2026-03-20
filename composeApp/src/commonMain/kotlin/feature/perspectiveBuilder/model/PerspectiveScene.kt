package feature.perspectiveBuilder.model

import kotlin.contracts.ExperimentalContracts

data class PerspectiveScene(
    val points: List<PerspectivePoint> = emptyList(),
    val rayCount: Int = 17,
    val width: Int = 1000,
    val height: Int = 1000,
    val gridEnabled: Boolean = true,
    val showPhotoInput: Boolean = false
)

@OptIn(ExperimentalContracts::class)
data class PerspectivePoint(
    val x: Float,
    val y: Float,
    val direction: Float? = null,
    val isVisible: Boolean = true,
) {
    val isFinite: Boolean get() =
        x != Float.MAX_VALUE && y != Float.MAX_VALUE

    companion object {
        fun infinite(directionDegrees: Float) =
            PerspectivePoint(Float.MAX_VALUE, Float.MAX_VALUE, directionDegrees)

        fun horizontal(y: Float) =
            PerspectivePoint(Float.MAX_VALUE, y, direction = 0f)

        fun vertical(x: Float) =
            PerspectivePoint(x, Float.MAX_VALUE, direction = 90f)
    }
}

enum class SceneSamples {
    NONE,
    ONE_POINT,
    TWO_POINT,
    THREE_POINT_TOP,
    THREE_POINT_DOWN
}