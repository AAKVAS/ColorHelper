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
    private val sphereRadius: Float
) {

    private var deltaX: Float = 0f
    private var deltaY: Float = 0f

    private var startEvent: TouchEvent? = null
    private var startPosition: Vec3? = null
    private var position: Vec3 = initialPosition

    private var thrust = 0.0f
    private val thrustVelocity = 0.01f
    private var rotationSpeed = 0.1f // Скорость вращения

    // Углы вращения вокруг цели
    private var azimuthAngle: Float = 0f // Угол по горизонтали (вокруг оси Y)
    private var elevationAngle: Float = 0f // Угол по вертикали (от -90 до 90)
    private var distance: Float = (targetPosition - initialPosition).length() // Дистанция до цели

    // Настройки ограничений расстояния
    private val minDistanceFromSphere: Float = sphereRadius * 1.5f
    private val maxDistanceFromSphere: Float = sphereRadius * 20f

    fun KorenderContext.camera(dt: Float): CameraDeclaration {
        // Обработка движения камеры (W/S - приближение/отдаление)
        distance -= thrust * thrustVelocity * dt

        distance = distance.coerceIn(minDistanceFromSphere, maxDistanceFromSphere)

        // Обработка вращения камеры (тач события)
        if (startPosition != null) {
            // Меняем углы на основе движения пальца
            azimuthAngle -= deltaX * rotationSpeed / 100f
            elevationAngle += deltaY * rotationSpeed / 100f

            // Ограничиваем вертикальный угол, чтобы не перевернуть камеру
            elevationAngle = elevationAngle.coerceIn(-89f, 89f)

            // Сбрасываем дельты после обработки
            deltaX = 0f
            deltaY = 0f
        }

        // Вычисляем новую позицию камеры на основе углов и дистанции
        position = calculateCameraPosition()

        // Направление от камеры к цели
        val direction = (targetPosition - position).normalize()
        // Вектор "вправо" (перпендикулярно направлению и вертикали)
        val right = (direction % 1.y).normalize()
        // Вектор "вверх" (перпендикулярно направлению и вектору вправо)
        val up = (right % direction).normalize()

        return camera(position, direction, up)
    }

    private fun calculateCameraPosition(): Vec3 {
        // Преобразуем сферические координаты в декартовы
        val x = distance * cos(elevationAngle.degreesToRadians) * sin(azimuthAngle.degreesToRadians)
        val y = distance * sin(elevationAngle.degreesToRadians)
        val z = distance * cos(elevationAngle.degreesToRadians) * cos(azimuthAngle.degreesToRadians)

        return targetPosition + Vec3(x, y, z)
    }

    // Расширение для преобразования градусов в радианы
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
        when {
            keyEvent.key == "W" && keyEvent.type == KeyEvent.Type.DOWN -> {
                thrust = 1.0f // Приближение
            }
            keyEvent.key == "W" && keyEvent.type == KeyEvent.Type.UP -> {
                thrust = 0.0f
            }
            keyEvent.key == "S" && keyEvent.type == KeyEvent.Type.DOWN -> {
                thrust = -1.0f // Отдаление
            }
            keyEvent.key == "S" && keyEvent.type == KeyEvent.Type.UP -> {
                thrust = 0.0f
            }
            // Опционально: добавить вращение клавишами A/D
            keyEvent.key == "A" && keyEvent.type == KeyEvent.Type.DOWN -> {
                azimuthAngle += 1f // Вращение влево
            }
            keyEvent.key == "D" && keyEvent.type == KeyEvent.Type.DOWN -> {
                azimuthAngle -= 1f // Вращение вправо
            }
        }
    }

    private val Float.radiansToDegrees: Float
        get() = this * (180f / Math.PI.toFloat())
}