package ui.`3d`.camera

import com.zakgof.korender.CameraDeclaration
import com.zakgof.korender.KeyEvent
import com.zakgof.korender.TouchEvent
import com.zakgof.korender.context.KorenderContext
import com.zakgof.korender.math.Vec3
import com.zakgof.korender.math.y
import kotlin.math.cos
import kotlin.math.sin

class OrbitCamera(
    initialPosition: Vec3,
    private var targetPosition: Vec3 = Vec3.ZERO,
    sphereRadius: Float
) {
    private var deltaX: Float = 0f
    private var deltaY: Float = 0f

    private var startEvent: TouchEvent? = null
    private var startPosition: Vec3? = null
    private var position: Vec3 = initialPosition

    private var thrust = 0.0f
    private val thrustVelocity = 0.01f
    private var rotationSpeed = 0.4f


    private var azimuthAngle: Float = 0f
    private var elevationAngle: Float = 0f
    private var distance: Float = (targetPosition - initialPosition).length()

    private val minDistanceFromSphere: Float = sphereRadius * 1.5f
    private val maxDistanceFromSphere: Float = sphereRadius * 20f

    fun KorenderContext.camera(dt: Float): CameraDeclaration {
        distance -= thrust * thrustVelocity * dt

        distance = distance.coerceIn(minDistanceFromSphere, maxDistanceFromSphere)

        if (startPosition != null) {
            azimuthAngle -= deltaX * rotationSpeed / 100f
            elevationAngle += deltaY * rotationSpeed / 100f

            elevationAngle = elevationAngle.coerceIn(-89f, 89f)

            deltaX = 0f
            deltaY = 0f
        }

        position = calculateCameraPosition()

        val direction = (targetPosition - position).normalize()
        val right = (direction % 1.y).normalize()
        val up = (right % direction).normalize()

        return camera(position, direction, up)
    }

    private fun calculateCameraPosition(): Vec3 {
        val x = distance * cos(elevationAngle.degreesToRadians) * sin(azimuthAngle.degreesToRadians)
        val y = distance * sin(elevationAngle.degreesToRadians)
        val z = distance * cos(elevationAngle.degreesToRadians) * cos(azimuthAngle.degreesToRadians)

        return targetPosition + Vec3(x, y, z)
    }

    private val Float.degreesToRadians: Float
        get() = this * (Math.PI.toFloat() / 180f)

    fun touch(touchEvent: TouchEvent) {
        when (touchEvent.type) {
            TouchEvent.Type.DOWN -> {
                startEvent = touchEvent
                startPosition = position
            }
            TouchEvent.Type.UP -> {
                startEvent = null
                startPosition = null
                deltaX = 0f
                deltaY = 0f
            }
            TouchEvent.Type.MOVE -> {
                if (startEvent != null) {
                    deltaX = touchEvent.x - startEvent!!.x
                    deltaY = touchEvent.y - startEvent!!.y
                }
            }
        }
    }

    fun handle(keyEvent: KeyEvent) {
        when (keyEvent.key) {
            "W" if keyEvent.type == KeyEvent.Type.DOWN -> {
                thrust = 1.0f
            }
            "W" if keyEvent.type == KeyEvent.Type.UP -> {
                thrust = 0.0f
            }
            "S" if keyEvent.type == KeyEvent.Type.DOWN -> {
                thrust = -1.0f
            }
            "S" if keyEvent.type == KeyEvent.Type.UP -> {
                thrust = 0.0f
            }
            "A" if keyEvent.type == KeyEvent.Type.DOWN -> {
                azimuthAngle += 1f
            }
            "D" if keyEvent.type == KeyEvent.Type.DOWN -> {
                azimuthAngle -= 1f
            }
        }
    }
}