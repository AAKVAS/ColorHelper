package feature.perspectiveBuilder.domain

data class GrayImage(
    val image: IntArray,
    val height: Int,
    val width: Int,
)

data class BlackWhiteImage(
    val image: BooleanArray,
    val height: Int,
    val width: Int,
)