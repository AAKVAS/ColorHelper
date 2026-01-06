package ui.`3d`

import com.zakgof.korender.context.FrameContext
import com.zakgof.korender.math.Transform
import com.zakgof.korender.math.Vec3

fun FrameContext.InstancedFenceView(
    positions: List<Vec3>,
    halfSide: Float,
    bufferCount: Int
) {
    Renderable(
        base(color = Colors.fenceColor),
        mesh = cube(halfSide),
        instancing = instancing(
            id = "fences",
            count = bufferCount,
            dynamic = true
        ) {
            positions.forEach { pos ->
                Instance(Transform().translate(pos))
            }
        }
    )
}