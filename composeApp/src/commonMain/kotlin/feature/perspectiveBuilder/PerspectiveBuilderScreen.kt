package feature.perspectiveBuilder

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.example.Res
import com.example.one_point_perspective
import com.example.point
import com.example.ray_count
import com.example.samples
import com.example.show_grid
import com.example.three_point_perspective_bottom
import com.example.three_point_perspective_top
import com.example.two_point_perspective
import feature.perspectiveBuilder.model.PerspectivePoint
import feature.perspectiveBuilder.model.PerspectiveScene
import feature.perspectiveBuilder.model.SceneSamples
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import ui.composeComponents.CustomTextField
import ui.composeComponents.DeleteButton
import ui.composeComponents.SimpleButton
import ui.theme.Dimens
import ui.theme.LocalColorProvider
import utils.HandleClipboardCopy
import utils.distanceToPoint
import utils.rememberIsPortrait
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

@Composable
fun PerspectiveBuilderScreen(
    component: PerspectiveBuilderComponent,
    windowSize: DpSize,
    modifier: Modifier = Modifier
) {
    val isPortrait = rememberIsPortrait(windowSize)
    val state = component.state.collectAsState()
    val gridEnabled = remember { mutableStateOf(true) }
    val graphicsLayer = rememberGraphicsLayer()
    val scope = rememberCoroutineScope()
    var exportBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var usedSample by remember { mutableStateOf(SceneSamples.NONE) }

    HandleClipboardCopy {
        scope.launch {
            exportBitmap = graphicsLayer.toImageBitmap()
        }
    }

    exportBitmap?.let {
        ExportPerspectiveScene(it)
    }

    if (isPortrait.value) {
        Column(modifier = modifier.fillMaxSize()) {
            PerspectiveBuilderSceneView(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens.perspectiveSceneHeight)
                    .drawWithContent {
                        graphicsLayer.record {
                            this@drawWithContent.drawContent()
                        }
                        drawLayer(graphicsLayer)
                    },
                scene = state.value.scene,
                usedSample = usedSample,
                gridEnabled = gridEnabled.value,
                updatePointPosition = { index, pos ->
                    component.updatePointByIndex(index, pos)
                },
                updateScene = { scene ->
                    usedSample = SceneSamples.NONE
                    component.updateScene(scene)
                }
            )
            PerspectiveBuilderSettings(
                modifier = Modifier.fillMaxWidth(),
                state = state.value,
                component = component,
                showGridChanged = { enabled ->
                    gridEnabled.value = enabled
                },
                onExportButtonClick = {
                    scope.launch {
                        exportBitmap = graphicsLayer.toImageBitmap()
                    }
                },
                onSampleClick = { sample ->
                    usedSample = sample
                }
            )
        }
    } else {
        Row(
            modifier = modifier.fillMaxSize(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.Start
        ) {
            PerspectiveBuilderSceneView(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .drawWithContent {
                        graphicsLayer.record {
                            this@drawWithContent.drawContent()
                        }
                        drawLayer(graphicsLayer)
                    },
                scene = state.value.scene,
                usedSample = usedSample,
                gridEnabled = gridEnabled.value,
                updatePointPosition = { index, pos ->
                    component.updatePointByIndex(index, pos)
                },
                updateScene = { scene ->
                    usedSample = SceneSamples.NONE
                    component.updateScene(scene)
                }
            )
            PerspectiveBuilderSettings(
                modifier = Modifier.width(Dimens.perspectiveBuilderSettingsWidth),
                state = state.value,
                component = component,
                showGridChanged = { enabled ->
                    gridEnabled.value = enabled
                },
                onExportButtonClick = {
                    scope.launch {
                        exportBitmap = graphicsLayer.toImageBitmap()
                    }
                },
                onSampleClick = { sample ->
                    usedSample = sample
                }
            )
        }
    }
}

@Composable
fun PerspectiveBuilderSceneView(
    modifier: Modifier = Modifier,
    scene: PerspectiveScene,
    gridEnabled: Boolean,
    usedSample: SceneSamples,
    updateScene: (PerspectiveScene) -> Unit,
    updatePointPosition: (Int, PerspectivePoint) -> Unit,
) {
    var draggingPoint by remember { mutableStateOf<Pair<Int, PerspectivePoint>?>(null) }
    val dragThresholdDp = 18f

    Canvas(modifier = modifier
        .padding(Dimens.paddingRegular)
        .background(Color.White)
        .clipToBounds()
        .pointerInput(scene) {
            val thresholdPx = dragThresholdDp.dp.toPx()
            val thresholdSquared = thresholdPx * thresholdPx

            detectDragGestures(
                onDragStart = { offset ->
                    val draggingPointIndex = scene.points.indexOfFirst { point ->
                        val poweredDistance =
                            (point.x - offset.x).pow(2) + (point.y - offset.y).pow(2)
                        poweredDistance < thresholdSquared
                    }
                    if (draggingPointIndex != -1) {
                        val point = scene.points[draggingPointIndex]
                        draggingPoint = draggingPointIndex to point.copy()
                    }
                },
                onDrag = { change, dragAmount ->
                    draggingPoint?.let { (index, point) ->
                        val newPoint = point.copy(
                            x = point.x + dragAmount.x,
                            y = point.y + dragAmount.y
                        )
                        draggingPoint = index to newPoint
                        change.consume()
                    }
                },
                onDragEnd = {
                    draggingPoint?.let {
                        updatePointPosition(
                            it.first,
                            it.second
                        )
                        draggingPoint = null
                    }
                },
                onDragCancel = {
                    draggingPoint = null
                }
            )
        }
    ) {
        if (usedSample == SceneSamples.NONE) {
            scene.apply {
                val pointsForDrawing = if (draggingPoint == null) {
                    points
                } else {
                    points.mapIndexed { index, point ->
                        if (draggingPoint!!.first == index) {
                            draggingPoint!!.second
                        } else {
                            point
                        }
                    }
                }

                drawScene(pointsForDrawing, rayCount, gridEnabled)
            }
        }
        else {
            val centerX = size.width / 2
            val centerY = size.height / 2
            val leftX = -size.width
            val rightX = size.width * 2
            val topY = size.height * 3
            val downY = size.height * -2

            val points = when(usedSample) {
                SceneSamples.ONE_POINT -> {
                    listOf(PerspectivePoint(centerX, centerY))
                }
                SceneSamples.TWO_POINT -> {
                    listOf(
                        PerspectivePoint(leftX, centerY),
                        PerspectivePoint(rightX, centerY),
                    )
                }
                SceneSamples.THREE_POINT_TOP -> {
                    listOf(
                        PerspectivePoint(leftX, centerY * 0.75f),
                        PerspectivePoint(rightX, centerY * 0.75f),
                        PerspectivePoint(centerX, topY),
                    )
                }
                SceneSamples.THREE_POINT_DOWN -> {
                    listOf(
                        PerspectivePoint(leftX, centerY * 1.75f),
                        PerspectivePoint(rightX, centerY * 1.75f),
                        PerspectivePoint(centerX, downY),
                    )
                }
            }
            updateScene(scene.copy(points = points))
        }
    }
}

fun DrawScope.drawScene(
    points: List<PerspectivePoint>,
    rayCount: Int,
    gridEnabled: Boolean
) {
    drawRect(color = Color.White, size = this.size)

    if (gridEnabled) {
        drawGrid()
    }
    points.forEach { point ->
        drawPoint(point)
        drawRaysFromPoint(point, rayCount)
    }
}

fun DrawScope.drawPoint(point: PerspectivePoint) {
    drawCircle(
        color = Color.Blue,
        radius = 8f,
        center = Offset(point.x, point.y),
        style = Stroke(width = 2f)
    )

    drawCircle(
        color = Color.White,
        radius = 6f,
        center = Offset(point.x, point.y),
    )

    drawCircle(
        color = Color.Blue,
        radius = 2f,
        center = Offset(point.x, point.y),
    )
}

fun DrawScope.drawRaysFromPoint(point: PerspectivePoint, rayCount: Int) {
    if (rayCount < 2) return

    val angleStep = 360f / rayCount

    val maxDistance = maxOf(
        distanceToPoint(point.x, point.y, 0f, 0f),
        distanceToPoint(point.x, point.y, size.width, 0f),
        distanceToPoint(point.x, point.y, 0f, size.height),
        distanceToPoint(point.x, point.y, size.width, size.height)
    )

    val rayLength = maxDistance * 1.1f

    for (i in 0 until rayCount) {
        val angle = Math.toRadians((angleStep * i).toDouble())
        val endX = point.x + (cos(angle) * rayLength).toFloat()
        val endY = point.y + (sin(angle) * rayLength).toFloat()

        drawLine(
            color = Color.Blue.copy(alpha = 0.3f),
            start = Offset(point.x, point.y),
            end = Offset(endX, endY),
            strokeWidth = 1f
        )
    }
}


fun DrawScope.drawGrid() {
    val step = 50f
    val startX = 0f
    val startY = 0f
    val endX = size.width
    val endY = size.height

    var x = startX
    while (x <= endX) {
        drawLine(
            color = Color.LightGray.copy(alpha = 0.7f),
            start = Offset(x, startY),
            end = Offset(x, endY),
            strokeWidth = 1f
        )
        x += step
    }

    var y = startY
    while (y <= endY) {
        drawLine(
            color = Color.LightGray.copy(alpha = 0.7f),
            start = Offset(startX, y),
            end = Offset(endX, y),
            strokeWidth = 1f
        )
        y += step
    }
}

@Composable
fun PerspectiveBuilderSettings(
    modifier: Modifier = Modifier,
    state: PerspectiveBuilderStore.State,
    component: PerspectiveBuilderComponent,
    showGridChanged: (Boolean) -> Unit,
    onSampleClick: (SceneSamples) -> Unit,
    onExportButtonClick: () -> Unit
) {
    val selectedPointIndex = state.selectedPointIndex
    val scrollState = rememberScrollState(0)
    Column(
        modifier = modifier
            .padding(horizontal = Dimens.paddingSmall)
            .wrapContentHeight()
            .verticalScroll(scrollState)
    ) {
        GridCheckbox(showGridChanged)

        RayCountController(
            rayCount = state.scene.rayCount,
            onRayCountChange = { count ->
                component.changeRayCount(count)
            }
        )

        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(Dimens.paddingSmall),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            PerspectivePoints(
                points = state.scene.points,
                selectedIndex = selectedPointIndex,
                onSelected = { index ->
                    component.changeSelectedPointIndex(index)
                },
                deletePoint = { index ->
                    component.removePointByIndex(index)
                }
            )
            if (selectedPointIndex != -1) {
                CoordinatesInput(point = state.scene.points[selectedPointIndex]) { newPoint ->
                    component.updatePointByIndex(selectedPointIndex, newPoint)
                }
            }
        }

        var expanded by remember { mutableStateOf(false) }

        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(Dimens.paddingSmall),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SimpleButton(text = "+") {
                component.addPoint(PerspectivePoint(20f, 20f))
            }

            Box(
                modifier = Modifier.wrapContentSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                SimpleButton(
                    text = stringResource(Res.string.samples)
                ) {
                    expanded = true
                }
                SamplesDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    onSampleClick = onSampleClick
                )
            }

            ExportButton(modifier = Modifier.padding(top = Dimens.paddingXSmall)) {
                onExportButtonClick()
            }
        }
    }
}

@Composable
fun SamplesDropdownMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onSampleClick: (SceneSamples) -> Unit
) {
    val items = mapOf(
        Res.string.one_point_perspective to SceneSamples.ONE_POINT,
        Res.string.two_point_perspective to SceneSamples.TWO_POINT,
        Res.string.three_point_perspective_top to SceneSamples.THREE_POINT_TOP,
        Res.string.three_point_perspective_bottom to SceneSamples.THREE_POINT_DOWN
    )
    DropdownMenu(
        modifier = modifier,
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        items.forEach { item ->
            DropdownMenuItem(
                text = { Text(stringResource(item.key)) },
                onClick = {
                    onSampleClick(item.value)
                    onDismissRequest()
                }
            )
        }
    }
}

@Composable
fun GridCheckbox(
    onCheckChange: (Boolean) -> Unit
) {
    var checked by remember { mutableStateOf(true) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable {
            checked = !checked
            onCheckChange(checked)
        }.padding(vertical = Dimens.paddingXSmall)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = null
        )
        Text(
            text = stringResource(Res.string.show_grid),
            modifier = Modifier.padding(start = Dimens.paddingSmall),
            color = LocalColorProvider.current.onSurface
        )
    }
}

@Composable
fun CoordinatesInput(
    modifier: Modifier = Modifier,
    point: PerspectivePoint,
    coordinatesChange: (PerspectivePoint) -> Unit
) {
    Column(
        modifier = modifier.wrapContentSize(),
        horizontalAlignment = Alignment.Start
    ) {
        NumberInput(text = "x: ", value = point.x.toInt()) { newX ->
            coordinatesChange(point.copy(x = newX.toFloat()))
        }
        NumberInput(text = "y: ", value = point.y.toInt()) { newY ->
            coordinatesChange(point.copy(y = newY.toFloat()))
        }
    }
}

@Composable
fun NumberInput(
    modifier: Modifier = Modifier,
    text: String,
    value: Int,
    onValueChanged: (Int) -> Unit
) {
    val pattern = remember { Regex("^-?\\d+\$") }
    Row(
        modifier = modifier.padding(vertical = Dimens.paddingXSmall),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            color = LocalColorProvider.current.onSurface
        )
        CustomTextField(
            value.toString(),
            modifier = Modifier
                .width(Dimens.coordinateTextFieldWidth)
                .height(Dimens.smallNumberTextFieldHeight)
        ) {
            if (it.matches(pattern)) {
                try {
                    val num = it.toInt()
                    onValueChanged(num)
                } catch (e: NumberFormatException) {

                }
            }
        }
    }
}

@Composable
fun PerspectivePoints(
    modifier: Modifier = Modifier,
    points: List<PerspectivePoint>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
    deletePoint: (Int) -> Unit
) {
    Column(
        modifier = modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        points.forEachIndexed { index, _ ->
            PointRow(
                index = index,
                isSelected = index == selectedIndex,
                onSelected = {
                    onSelected(index)
                },
                onDelete = {
                    deletePoint(index)
                }
            )
        }
    }
}

@Composable
fun PointRow(
    modifier: Modifier = Modifier,
    index: Int,
    isSelected: Boolean,
    onSelected: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = modifier.wrapContentSize().clickable {
            onSelected()
        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        RadioButton(
            modifier = Modifier
                .padding(Dimens.paddingXSmall)
                .size(Dimens.radioButtonSize),
            selected = isSelected,
            onClick = onSelected,
        )
        Text(
            text = stringResource(Res.string.point, index + 1),
            color = LocalColorProvider.current.onSurface
        )
        DeleteButton {
            onDelete()
        }
    }
}

@Composable
fun RayCountController(
    modifier: Modifier = Modifier,
    rayCount: Int,
    onRayCountChange: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .wrapContentSize()
            .padding(horizontal = Dimens.paddingSmall),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.ray_count),
            color = LocalColorProvider.current.onSurface,
            fontSize = Dimens.smallTextSize
        )
        Slider(
            value = rayCount.toFloat(),
            onValueChange = { onRayCountChange(it.toInt()) },
            valueRange = 4f..299f,
            steps = 47,
            colors = SliderDefaults.colors(
                thumbColor = LocalColorProvider.current.primary,
                activeTrackColor = LocalColorProvider.current.primary,
                inactiveTrackColor = LocalColorProvider.current.primaryVariant,
                inactiveTickColor = LocalColorProvider.current.primaryWeak,
                activeTickColor = LocalColorProvider.current.primaryVariant
            ),
            modifier = Modifier
                .height(Dimens.sliderHeight)
                .fillMaxWidth()
        )
    }
}

@Composable
expect fun ExportButton(modifier: Modifier, onClick: () -> Unit)

@Composable
expect fun ExportPerspectiveScene(bitmap: ImageBitmap)