package feature.perspectiveBuilder.model

data class PerspectiveScene(
    val points: List<PerspectivePoint> = emptyList(),
    val rayCount: Int = 17,
    val sample: SceneSamples = SceneSamples.NONE
)

data class PerspectivePoint(
    val x: Float,
    val y: Float
)

enum class SceneSamples {
    NONE,
    ONE_POINT,
    TWO_POINT,
    THREE_POINT_TOP,
    THREE_POINT_DOWN
}