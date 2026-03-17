package feature.perspectiveBuilder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import feature.perspectiveBuilder.model.PerspectiveScene


class CoordinateTransformer(
    sceneWidth: Float = 0f,
    sceneHeight: Float = 0f,
    canvasWidth: Float,
    canvasHeight: Float
) {
    private val sceneRect: Rect = Rect(0f, 0f, sceneWidth, sceneHeight)

    private val canvasRect: Rect = calculateCanvasRect(sceneWidth, sceneHeight, canvasWidth, canvasHeight)

    private val scaleX = canvasRect.width / sceneRect.width
    private val scaleY = canvasRect.height / sceneRect.height

    fun toScreen(x: Float, y: Float): Offset {
        return Offset(
            x = canvasRect.left + (x - sceneRect.left) * scaleX,
            y = canvasRect.top + (y - sceneRect.top) * scaleY
        )
    }

    fun toScene(screenX: Float, screenY: Float): Offset {
        return Offset(
            x = sceneRect.left + (screenX - canvasRect.left) / scaleX,
            y = sceneRect.top + (screenY - canvasRect.top) / scaleY
        )
    }

    private fun calculateCanvasRect(
        sceneWidth: Float,
        sceneHeight: Float,
        canvasWidth: Float,
        canvasHeight: Float
    ): Rect {
        val scale = minOf(canvasWidth / sceneWidth, canvasHeight / sceneHeight)
        val scaledWidth = sceneWidth * scale
        val scaledHeight = sceneHeight * scale
        val offsetX = (canvasWidth - scaledWidth) / 2f
        val offsetY = (canvasHeight - scaledHeight) / 2f
        return Rect(offsetX, offsetY, offsetX + scaledWidth, offsetY + scaledHeight)
    }
}

@Composable
fun rememberCoordinateTransformer(
    scene: PerspectiveScene,
    canvasWidth: Float,
    canvasHeight: Float
): CoordinateTransformer {
    return remember(scene.width, scene.height, canvasWidth, canvasHeight) {
        CoordinateTransformer(
            sceneWidth = scene.width.toFloat(),
            sceneHeight = scene.height.toFloat(),
            canvasWidth = canvasWidth,
            canvasHeight = canvasHeight
        )
    }
}