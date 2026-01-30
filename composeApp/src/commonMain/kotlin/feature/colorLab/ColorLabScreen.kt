package feature.colorLab

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.example.Res
import com.example.copied
import com.example.copy
import com.example.floor_color
import com.example.light_color
import com.example.sphere_color
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.zakgof.korender.Korender
import com.zakgof.korender.math.Transform.Companion.scale
import com.zakgof.korender.math.Transform.Companion.translate
import com.zakgof.korender.math.Vec3
import com.zakgof.korender.math.y
import com.zakgof.korender.math.z
import feature.colorLab.model.ColorLabState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import ui.`3d`.camera.OrbitCamera
import ui.composeComponents.ColorPickerUI
import ui.composeComponents.CustomTextField
import ui.composeComponents.TooltipWrapper
import ui.theme.Dimens
import ui.theme.LocalColorProvider
import utils.toColor
import utils.toHex
import utils.toRGB
import utils.toRGBA


@Composable
fun ColorHelperScreen(
    component: ColorLabComponent,
    windowSize: DpSize,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val clipboardManager = LocalClipboardManager.current
    val state = component.state.collectAsState()

    val colorPickerController: MutableState<ColorPickerController?> = remember { mutableStateOf(null) }
    val sphereColorController: MutableState<ColorPickerController?> = remember { mutableStateOf(null) }
    val floorColorController: MutableState<ColorPickerController?> = remember { mutableStateOf(null) }
    val lightColorController: MutableState<ColorPickerController?> = remember { mutableStateOf(null) }

    val isPortrait = remember(windowSize) {
        derivedStateOf {
            windowSize.width < windowSize.height
        }
    }

    val copied = stringResource(Res.string.copied)
    val snackbarHostState = remember { SnackbarHostState() }
    val onCopyTextToClipboard: (String) -> Unit = { text ->
        scope.launch {
            clipboardManager.setText(
                AnnotatedString(text)
            )
            snackbarHostState.showSnackbar(copied)
        }
    }

    LaunchedEffect(
        sphereColorController.value?.selectedColor?.value,
        lightColorController.value?.selectedColor?.value,
        floorColorController.value?.selectedColor?.value
    ) {
        val sphereColor = sphereColorController.value?.selectedColor?.value?.toHex(withAlpha = true)
            ?: component.state.value.sphereColor
        val lightColor = lightColorController.value?.selectedColor?.value?.toHex(withAlpha = false)
            ?: component.state.value.lightColor
        val floorColor = floorColorController.value?.selectedColor?.value?.toHex(withAlpha = true)
            ?: component.state.value.floorColor

        component.updateState(
            ColorLabState(
                sphereColor = sphereColor,
                sphereMetallicFactor = 0.01f,
                lightColor = lightColor,
                floorColor = floorColor
            )
        )
    }

    if (!isPortrait.value) {
        Row(
            modifier = modifier
                .fillMaxSize()
                .background(LocalColorProvider.current.onPrimary)
        ) {
            SettingsUI(
                sphereColorController = sphereColorController,
                colorPickerController = colorPickerController,
                floorColorController = floorColorController,
                lightColorController = lightColorController,
                colorLabState = state.value,
                scope = scope,
                copyTextToClipboard = onCopyTextToClipboard
            )
            Box(modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .background(LocalColorProvider.current.background)
                .padding(Dimens.paddingSmall)
            ) {
                Scene(state)
            }
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(LocalColorProvider.current.onPrimary)
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .background(LocalColorProvider.current.background)
                .padding(Dimens.paddingSmall)
            ) {
                Scene(state)
            }
            SettingsUI(
                sphereColorController = sphereColorController,
                colorPickerController = colorPickerController,
                floorColorController = floorColorController,
                lightColorController = lightColorController,
                colorLabState = state.value,
                scope = scope,
                copyTextToClipboard = onCopyTextToClipboard
            )
        }
    }
}

@Composable
fun SettingsUI(
    sphereColorController: MutableState<ColorPickerController?>,
    colorPickerController: MutableState<ColorPickerController?>,
    floorColorController: MutableState<ColorPickerController?>,
    lightColorController: MutableState<ColorPickerController?>,
    colorLabState: ColorLabState,
    scope: CoroutineScope,
    copyTextToClipboard: (text: String) -> Unit
) {
    Column {
        ColorCharacteristic(
            text = stringResource(Res.string.sphere_color),
            controller = sphereColorController.value,
            onColorTileClick = {
                if (sphereColorController.value == null) {
                    sphereColorController.value = ColorPickerController(scope)
                }
                if (colorPickerController.value != sphereColorController.value) {
                    colorPickerController.value = sphereColorController.value
                } else {
                    colorPickerController.value = null
                }
            },
            defaultColor = colorLabState.sphereColor.toColor(),
            onTextCopied = copyTextToClipboard
        )
        ColorCharacteristic(
            text = stringResource(Res.string.floor_color),
            controller = floorColorController.value ,
            onColorTileClick = {
                if (floorColorController.value == null) {
                    floorColorController.value = ColorPickerController(scope)
                }
                if (colorPickerController.value != floorColorController.value) {
                    colorPickerController.value = floorColorController.value
                } else {
                    colorPickerController.value = null
                }
            },
            defaultColor = colorLabState.floorColor.toColor(),
            onTextCopied = copyTextToClipboard
        )
        ColorCharacteristic(
            text = stringResource(Res.string.light_color),
            controller = lightColorController.value,
            onColorTileClick = {
                if (lightColorController.value == null) {
                    lightColorController.value = ColorPickerController(scope)
                }
                if (colorPickerController.value != lightColorController.value) {
                    colorPickerController.value = lightColorController.value
                } else {
                    colorPickerController.value = null
                }
            },
            defaultColor = colorLabState.lightColor.toColor(),
            onTextCopied = copyTextToClipboard
        )
        colorPickerController.value?.let {
            val defaultColor = when {
                (it == lightColorController.value) -> colorLabState.lightColor.toColor()
                (it == floorColorController.value) -> colorLabState.floorColor.toColor()
                (it == sphereColorController.value) -> colorLabState.sphereColor.toColor()
                else -> Color.White
            }
            ColorPickerUI(
                colorController = it,
                defaultColor = defaultColor,
                onCopyTextToClipboard = copyTextToClipboard
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ColorCharacteristic(
    modifier: Modifier = Modifier,
    controller: ColorPickerController?,
    text: String,
    onColorTileClick: () -> Unit,
    defaultColor: Color,
    onTextCopied: (text: String) -> Unit
) {
    val selectedColor = controller?.selectedColor?.value ?: defaultColor

    Row(
        modifier = modifier.padding(Dimens.paddingXSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = LocalColorProvider.current.onSurface
        )
        SelectionContainer {
            TooltipWrapper(
                tooltip = {
                    Surface(shape = RoundedCornerShape(Dimens.roundedCornerShapeSize)) {
                        Text(
                            modifier = Modifier.padding(Dimens.paddingXSmall),
                            text = stringResource(Res.string.copy)
                        )
                    }
                }
            ) {
                Text(
                    modifier = Modifier.clickable {
                        onTextCopied(selectedColor.toHex(true))
                    },
                    text = selectedColor.toHex(true),
                    color = LocalColorProvider.current.onSurface
                )
            }
        }
        AlphaTile(
            modifier = Modifier
                .padding(start = Dimens.paddingSmall)
                .size(Dimens.alphaTileSize)
                .clip(RoundedCornerShape(Dimens.smallRoundedCornerShapeSize))
                .clickable {
                    onColorTileClick()
                },
            controller = controller,
            selectedColor = defaultColor
        )
    }
}


@Composable
fun RGBCharacteristic(
    text: String,
    colorValue: Int,
    onValueChanged: (Int) -> Unit
) {
    val pattern = remember { Regex("^\\d{0,3}\$") }
    Row(
        modifier = Modifier
        .wrapContentSize()
        .padding(
            start = Dimens.paddingXSmall,
            end = Dimens.paddingXSmall,
            top = 0.dp,
            bottom = Dimens.paddingXSmall
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = LocalColorProvider.current.onSurface
        )
        Spacer(Modifier.width(Dimens.paddingXSmall).height(Dimens.paddingXXSmall))
        CustomTextField(
            colorValue.toString(),
            modifier = Modifier
                .width(Dimens.smallNumberTextFieldWidth)
                .height(Dimens.smallNumberTextFieldHeight)
        ) {
            if (it.isEmpty() || it.matches(pattern)) {
                if (it.isEmpty()) {
                    onValueChanged(0)
                } else {
                    val num = it.toInt()
                    if (num in 0..255) {
                        onValueChanged(num)
                    }
                }
            }
        }
    }
}

@Composable
fun Scene(colorLabState: State<ColorLabState>) =
    Korender(appResourceLoader = { Res.readBytes(it) }) {
        val sphereRadius = 6f
        val freeOrbitCamera = OrbitCamera(20.z, 2.y, sphereRadius)
        OnTouch { freeOrbitCamera.touch(it) }
        OnKey { freeOrbitCamera.handle(it)  }

        Frame {
            projection = projection(width = 3f * width / height, height = 3f, near = 3f, far = 1000f)
            camera = freeOrbitCamera.run { camera(frameInfo.time) }

            val lightColor = colorLabState.value.lightColor.toColor().toRGB()
            DirectionalLight(Vec3(1f, -1f, -1f), lightColor) {
                Cascade(
                    mapSize = 2048,
                    near = 1f,
                    far = 20f,
                    algorithm = softwarePcf(
                        samples = 32,
                        blurRadius = 0.02f
                    )
                )

                Cascade(
                    mapSize = 2048,
                    near = 15f,
                    far = 50f,
                    algorithm = softwarePcf(
                        samples = 16,
                        blurRadius = 0.03f
                    )
                )

                Cascade(
                    mapSize = 2048,
                    near = 40f,
                    far = 200f,
                    algorithm = softwarePcf(
                        samples = 8,
                        blurRadius = 0.05f
                    )
                )

            }

            Renderable(
                base(
                    color = colorLabState.value.floorColor.toColor().toRGBA(),
                    metallicFactor = 0.0f,
                    roughnessFactor = 0.6f
                ),
                mesh = cube(1f),
                transform = scale(1000f, 0.3f, 1000f)
                    .translate((-7).y)
            )

            Renderable(
                base(
                    color = colorLabState.value.sphereColor.toColor().toRGBA(),
                    metallicFactor = colorLabState.value.sphereMetallicFactor,
                    roughnessFactor = 0.3f
                ),
                mesh = sphere(sphereRadius),
                transform = translate(1.y)
            )
        }
    }