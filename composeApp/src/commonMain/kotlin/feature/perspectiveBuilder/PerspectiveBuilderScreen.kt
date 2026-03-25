package feature.perspectiveBuilder

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.aakvas.Res
import com.aakvas.one_point_perspective
import com.aakvas.point
import com.aakvas.ray_count
import com.aakvas.samples
import com.aakvas.selected_photo
import com.aakvas.show_grid
import com.aakvas.three_point_perspective_bottom
import com.aakvas.three_point_perspective_top
import com.aakvas.two_point_perspective
import core.model.Image
import core.model.ImageSource
import core.ui.composeComponents.CameraIconButton
import core.ui.composeComponents.CustomTextField
import core.ui.composeComponents.DeleteButton
import core.ui.composeComponents.DisplayImage
import core.ui.composeComponents.ImagePicker
import core.ui.composeComponents.PhotoInputArea
import core.ui.composeComponents.SimpleButton
import core.ui.composeComponents.VisibilityButton
import core.ui.theme.Dimens
import core.ui.theme.LocalColorProvider
import core.utils.GetImageBySource
import core.utils.HandleClipboardCopy
import core.utils.HandleClipboardPaste
import core.utils.distanceToPoint
import core.utils.pasteImageFromClipboard
import core.utils.rememberIsPortrait
import feature.perspectiveBuilder.model.PerspectivePoint
import feature.perspectiveBuilder.model.PerspectiveScene
import feature.perspectiveBuilder.model.SceneSamples
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun PerspectiveBuilderScreen(
    component: PerspectiveBuilderComponent,
    windowSize: DpSize,
    modifier: Modifier = Modifier
) {
    val isPortrait = rememberIsPortrait(windowSize)
    val state = component.state.collectAsState()
    val graphicsLayer = rememberGraphicsLayer()
    val scope = rememberCoroutineScope()
    var exportBitmap by remember { mutableStateOf<ImageBitmap?>(null) }

    HandleClipboardCopy {
        scope.launch {
            try {
                exportBitmap = graphicsLayer.toImageBitmap()
            } catch (_: Exception) {
                
            }
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
                    .height(Dimens.perspectiveSceneHeight),
                imageSource = state.value.imageSource,
                updateImageSource = { newImageSource ->
                    component.updateImageSource(newImageSource)
                },
                graphicsLayer = graphicsLayer,
                state = state.value,
                updatePointPosition = { index, pos ->
                    component.updatePointByIndex(index, pos)
                },
                activatePhotoInput = { component.changePhotoInputVisibility(true) },
                generateSceneFromImage = { image ->
                    component.generateSceneFromImage(image)
                },
                cancelGeneration = {
                    component.cancelGeneration()
                },
                onPointSelected = { index ->
                    component.changeSelectedPointIndex(index)
                }
            )
            PerspectiveBuilderSettings(
                modifier = Modifier.fillMaxWidth(),
                state = state.value,
                component = component,
                showGridChanged = { enabled ->
                    component.changeGridVisibility(enabled)
                },
                onExportButtonClick = {
                    scope.launch {
                        exportBitmap = graphicsLayer.toImageBitmap()
                    }
                },
                onSampleClick = { sample ->
                    component.useSample(sample)
                },
                updateImageSource = { newImageSource ->
                    component.updateImageSource(newImageSource)
                },
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
                    .weight(1f),
                graphicsLayer = graphicsLayer,
                imageSource = state.value.imageSource,
                state = state.value,
                updatePointPosition = { index, pos ->
                    component.updatePointByIndex(index, pos)
                },
                activatePhotoInput = { component.changePhotoInputVisibility(true) },
                generateSceneFromImage = { image ->
                    component.generateSceneFromImage(image)
                },
                cancelGeneration = {
                    component.cancelGeneration()
                },
                onPointSelected = { index ->
                    component.changeSelectedPointIndex(index)
                },
                updateImageSource = { newImageSource ->
                    component.updateImageSource(newImageSource)
                },
            )
            PerspectiveBuilderSettings(
                modifier = Modifier.width(Dimens.perspectiveBuilderSettingsWidth),
                state = state.value,
                component = component,
                showGridChanged = { enabled ->
                    component.changeGridVisibility(enabled)
                },
                onExportButtonClick = {
                    scope.launch {
                        exportBitmap = graphicsLayer.toImageBitmap()
                    }
                },
                onSampleClick = { sample ->
                    component.useSample(sample)
                },
                updateImageSource = { newImageSource ->
                    component.updateImageSource(newImageSource)
                }
            )
        }
    }
}

@Composable
fun PerspectiveBuilderSceneView(
    modifier: Modifier = Modifier,
    imageSource: ImageSource?,
    updateImageSource: (ImageSource?) -> Unit,
    graphicsLayer: GraphicsLayer,
    state: PerspectiveBuilderStore.State,
    updatePointPosition: (Int, PerspectivePoint) -> Unit,
    activatePhotoInput: () -> Unit,
    generateSceneFromImage: (Image) -> Unit,
    onPointSelected: (Int) -> Unit,
    cancelGeneration: () -> Unit
) {
    imageSource?.let {
        GetImageBySource(it) { newImage ->
            newImage?.let { image ->
                generateSceneFromImage(image)
            }
        }
    }

    HandleClipboardPaste {
        pasteImageFromClipboard()?.let {
            activatePhotoInput()
            updateImageSource(ImageSource.BitmapSource(it))
        }
    }

    PhotoInputArea(
        imageSource = imageSource,
        onImageDropped = { source ->
            updateImageSource(source)
        },
        onCloseImageButtonClick = {
            updateImageSource(null)
            cancelGeneration()
        },
        modifier = modifier,
        emptySourceBlock = {
            PerspectiveSceneCanvas(
                modifier = Modifier.fillMaxSize(),
                scene = state.scene,
                gridEnabled = state.scene.gridEnabled,
                updatePointPosition = updatePointPosition,
                withBackground = true,
                onPointSelected = onPointSelected
            )
        }
    ) { source ->
        Box(modifier = Modifier
            .drawWithContent {
                graphicsLayer.record {
                    this@drawWithContent.drawContent()
                }
                drawLayer(graphicsLayer)
            }
        ) {
            DisplayImage(
                source = source,
                contentDescription = stringResource(Res.string.selected_photo),
                modifier = Modifier.fillMaxSize()
            )
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(Dimens.circularProgressIndicatorSize)
                        .align(Alignment.Center),
                    strokeWidth = Dimens.paddingXXSmall
                )
            } else {
                PerspectiveSceneCanvas(
                    modifier = Modifier.fillMaxSize(),
                    scene = state.scene,
                    gridEnabled = state.scene.gridEnabled,
                    updatePointPosition = updatePointPosition,
                    withBackground = false,
                    onPointSelected = onPointSelected
                )
            }
        }
    }
}

@Composable
fun PerspectiveSceneCanvas(
    modifier: Modifier = Modifier,
    scene: PerspectiveScene,
    gridEnabled: Boolean,
    updatePointPosition: (Int, PerspectivePoint) -> Unit,
    withBackground: Boolean,
    onPointSelected: (Int) -> Unit
) {
    var draggingPoint by remember { mutableStateOf<Pair<Int, PerspectivePoint>?>(null) }
    val dragThresholdDp = 18f

    val sceneColor = LocalColorProvider.current.primaryVariant
    val backgroundColor = LocalColorProvider.current.primaryContainer

    BoxWithConstraints(
        modifier = modifier
    ) {
        val canvasWidth = constraints.maxWidth.toFloat()
        val canvasHeight = constraints.maxHeight.toFloat()

        val transformer = rememberCoordinateTransformer(scene, canvasWidth, canvasHeight)

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
                .pointerInput(scene, canvasWidth, canvasWidth) {
                    val thresholdPx = dragThresholdDp.dp.toPx()
                    val thresholdSquared = thresholdPx * thresholdPx

                    detectDragGestures(
                        onDragStart = { offset ->
                            val draggingPointIndex = scene.points.indexOfFirst { point ->
                                val screenPoint = transformer.toScreen(point.x, point.y)
                                val poweredDistance = (screenPoint.x - offset.x).pow(2) + (screenPoint.y - offset.y).pow(2)
                                poweredDistance < thresholdSquared
                            }
                            if (draggingPointIndex != -1) {
                                val point = scene.points[draggingPointIndex]
                                draggingPoint = draggingPointIndex to point.copy()
                                onPointSelected(draggingPointIndex)
                            }
                        },
                        onDrag = { change, dragAmount ->
                            draggingPoint?.let { (index, point) ->
                                val screenPoint = transformer.toScreen(point.x, point.y)

                                val newScenePoint = transformer.toScene(
                                    screenX = screenPoint.x + dragAmount.x,
                                    screenY = screenPoint.y + dragAmount.y
                                )

                                val newPoint = point.copy(
                                    x = newScenePoint.x,
                                    y = newScenePoint.y
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

                drawScene(
                    points = pointsForDrawing,
                    transformer = transformer,
                    rayCount = rayCount,
                    withBackground = withBackground,
                    gridEnabled = gridEnabled,
                    backgroundColor = backgroundColor,
                    sceneColor = sceneColor
                )
            }
        }
    }
}

fun DrawScope.drawScene(
    points: List<PerspectivePoint>,
    transformer: CoordinateTransformer,
    rayCount: Int,
    withBackground: Boolean,
    gridEnabled: Boolean,
    backgroundColor: Color,
    sceneColor: Color
) {
    if (withBackground) {
        drawRect(color = backgroundColor, size = this.size)
    }

    if (gridEnabled) {
        drawGrid()
    }

    points.forEach { point ->
        if (point.isVisible) {
            if (point.isFinite) {
                val pointPos = transformer.toScreen(point.x, point.y)
                drawRaysFromPoint(sceneColor, pointPos, rayCount)
            } else {
                drawParallelLines(sceneColor, point, rayCount)
            }
        }
    }
    points.filter {
        it.isVisible && it.isFinite
    }.forEach {  point ->
        val pointPos = transformer.toScreen(point.x, point.y)
        drawPoint(sceneColor, pointPos)
    }
}

fun DrawScope.drawPoint(
    pointColor: Color,
    pointPos: Offset
) {
    drawCircle(
        color = pointColor,
        radius = 8f,
        center = pointPos,
        style = Stroke(width = 2f)
    )

    drawCircle(
        color = Color.White,
        radius = 6f,
        center = pointPos,
    )

    drawCircle(
        color = pointColor,
        radius = 2f,
        center = pointPos,
    )
}

fun DrawScope.drawRaysFromPoint(
    lineColor: Color,
    pointPos: Offset,
    rayCount: Int
) {
    if (rayCount < 2) return

    val angleStep = 360f / rayCount

    val maxDistance = maxOf(
        distanceToPoint(pointPos.x, pointPos.y, 0f, 0f),
        distanceToPoint(pointPos.x, pointPos.y, size.width, 0f),
        distanceToPoint(pointPos.x, pointPos.y, 0f, size.height),
        distanceToPoint(pointPos.x, pointPos.y, size.width, size.height)
    )

    val rayLength = maxDistance * 1.1f

    for (i in 0 until rayCount) {
        val angle = Math.toRadians((angleStep * i).toDouble())
        val endX = pointPos.x + (cos(angle) * rayLength).toFloat()
        val endY = pointPos.y + (sin(angle) * rayLength).toFloat()

        drawLine(
            color = lineColor.copy(alpha = 0.5f),
            start = Offset(pointPos.x, pointPos.y),
            end = Offset(endX, endY),
            strokeWidth = 1f
        )
    }
}

private fun DrawScope.drawParallelLines(
    lineColor: Color,
    point: PerspectivePoint,
    rayCount: Int
) {
    val direction = point.direction ?: return
    val rad = Math.toRadians(direction.toDouble())

    val lineDx = cos(rad).toFloat()
    val lineDy = sin(rad).toFloat()

    val perpDx = -lineDy
    val perpDy = lineDx


    val diagonal = sqrt(size.width * size.width + size.height * size.height)
    val step = diagonal / rayCount.toFloat()

    val centerX = size.width / 2f
    val centerY = size.height / 2f

    for (i in -rayCount/2 .. rayCount/2) {
        val offsetX = perpDx * i * step
        val offsetY = perpDy * i * step

        val x1 = centerX + offsetX - lineDx * diagonal
        val y1 = centerY + offsetY - lineDy * diagonal
        val x2 = centerX + offsetX + lineDx * diagonal
        val y2 = centerY + offsetY + lineDy * diagonal

        drawLine(
            color = lineColor.copy(alpha = 0.5f),
            start = Offset(x1, y1),
            end = Offset(x2, y2),
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
    onExportButtonClick: () -> Unit,
    updateImageSource: (ImageSource?) -> Unit,
) {
    val selectedPointIndex = state.selectedPointIndex
    val scrollState = rememberScrollState(0)
    var expanded by remember { mutableStateOf(false) }
    var showImagePicker by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(horizontal = Dimens.paddingSmall)
            .wrapContentHeight()
            .verticalScroll(scrollState)
    ) {
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            GridCheckbox(showGridChanged)

            CameraIconButton(
                modifier = Modifier.padding(vertical = Dimens.paddingXSmall)
            ) {
                showImagePicker = !showImagePicker
            }
        }

        Box(
            modifier = Modifier.wrapContentSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            SimpleButton(
                modifier = Modifier.padding(vertical = Dimens.paddingXXSmall),
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
                },
                updatePointByIndex = { index, point ->
                    component.updatePointByIndex(index, point)
                }
            )
            if (selectedPointIndex != -1) {
                CoordinatesInput(point = state.scene.points[selectedPointIndex]) { newPoint ->
                    component.updatePointByIndex(selectedPointIndex, newPoint)
                }
            }
        }

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

            ExportButton(modifier = Modifier.padding(top = Dimens.paddingXSmall)) {
                onExportButtonClick()
            }
        }
    }

    if (showImagePicker) {
        ImagePicker { source ->
            updateImageSource(source)
            showImagePicker = false
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
        modifier = modifier.background(LocalColorProvider.current.onBackground),
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        items.forEach { item ->
            DropdownMenuItem(
                text = { Text(stringResource(item.key)) },
                onClick = {
                    onSampleClick(item.value)
                    onDismissRequest()
                },
                modifier = Modifier.background(LocalColorProvider.current.onBackground)
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
    if (point.isFinite) {
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
    } else {
        NumberInput(text = "∠°: ", value = point.direction!!.toInt()) { newAngle ->
            coordinatesChange(point.copy(direction = (newAngle % 360).toFloat()))
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
    deletePoint: (Int) -> Unit,
    updatePointByIndex: (Int, PerspectivePoint) -> Unit
) {
    Column(
        modifier = modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        points.forEachIndexed { index, point ->
            PointRow(
                index = index,
                isSelected = index == selectedIndex,
                onSelected = {
                    onSelected(index)
                },
                onDelete = {
                    deletePoint(index)
                },
                isVisible = point.isVisible,
                visibilityChanged = {
                    updatePointByIndex(index, point.copy(isVisible = !point.isVisible))
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
    isVisible: Boolean,
    visibilityChanged: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = modifier
            .wrapContentSize()
            .then(
        if (isSelected) {
                    Modifier.border(
                        width = Dimens.smallestPadding,
                        color = LocalColorProvider.current.primary,
                        shape = RoundedCornerShape(Dimens.roundedCornerShapeSize)
                    )
                } else {
                    Modifier
                }
            )
            .clickable {
                onSelected()
            }
            .padding(Dimens.paddingXSmall),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        VisibilityButton(
            modifier = Modifier.padding(
                vertical = Dimens.paddingXXSmall,
                horizontal =  Dimens.paddingXSmall
            ),
            visible = isVisible,
            onClick = visibilityChanged,
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

