package ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.key
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
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.zakgof.korender.Korender
import com.zakgof.korender.math.ColorRGB
import com.zakgof.korender.math.ColorRGBA
import com.zakgof.korender.math.Transform.Companion.scale
import com.zakgof.korender.math.Transform.Companion.translate
import com.zakgof.korender.math.Vec3
import com.zakgof.korender.math.y
import com.zakgof.korender.math.z
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import ui.`3d`.camera.OrbitCamera
import ui.components.CustomTextField
import ui.theme.Dimens
import ui.theme.LocalColorProvider
import kotlin.math.roundToInt


@Composable
fun ColorHelperScreen(
    windowSize: DpSize,
    modifier: Modifier = Modifier
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val clipboardManager = LocalClipboardManager.current

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
    val onCopyTextToClipboard: (String) -> Unit = { text ->
        scope.launch {
            clipboardManager.setText(
                AnnotatedString(text)
            )
            scaffoldState.snackbarHostState.showSnackbar(copied)
        }
    }

    val sceneUIState = remember {
        derivedStateOf {
            val sphereColor = (sphereColorController.value?.selectedColor?.value?.toRGBA() ?: ColorRGBA.White)
            val lightColor = lightColorController.value?.selectedColor?.value?.toRGB() ?: ColorRGB.White
            val floorColor = floorColorController.value?.selectedColor?.value?.toRGBA() ?: ColorRGBA.White

            SceneUIState(
                sphereColor = sphereColor,
                sphereMetallicColor = 0.01f,
                lightColor = lightColor,
                floorColor = floorColor
            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        snackbarHost = { SnackbarHost(hostState = scaffoldState.snackbarHostState) },
        contentWindowInsets = WindowInsets.systemBars
    ) { innerPadding ->
        if (!isPortrait.value) {
            Row(
                modifier = modifier
                    .fillMaxSize()
                    .background(LocalColorProvider.current.onPrimary)
                    .padding(top = innerPadding.calculateTopPadding())
            ) {
                SettingsUI(
                    sphereColorController = sphereColorController,
                    colorPickerController = colorPickerController,
                    floorColorController = floorColorController,
                    lightColorController = lightColorController,
                    scope = scope,
                    copyTextToClipboard = onCopyTextToClipboard
                )
                Box(modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .background(LocalColorProvider.current.background)
                    .padding(Dimens.paddingSmall)
                ) {
                    Scene(sceneUIState)
                }
            }
        } else {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(LocalColorProvider.current.onPrimary)
                    .padding(top = innerPadding.calculateTopPadding())
            ) {
                Box(modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .background(LocalColorProvider.current.background)
                    .padding(Dimens.paddingSmall)
                ) {
                    Scene(sceneUIState)
                }
                SettingsUI(
                    sphereColorController = sphereColorController,
                    colorPickerController = colorPickerController,
                    floorColorController = floorColorController,
                    lightColorController = lightColorController,
                    scope = scope,
                    copyTextToClipboard = onCopyTextToClipboard
                )
            }
        }
    }
}

@Composable
fun SettingsUI(
    sphereColorController: MutableState<ColorPickerController?>,
    colorPickerController: MutableState<ColorPickerController?>,
    floorColorController: MutableState<ColorPickerController?>,
    lightColorController: MutableState<ColorPickerController?>,
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
            onTextCopied = copyTextToClipboard
        )
        colorPickerController.value?.let {
            ColorPickerUI(
                colorController = it
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
    onTextCopied: (text: String) -> Unit
) {
    val selectedColor = controller?.selectedColor?.value ?: Color.White

    Row(
        modifier = modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = LocalColorProvider.current.onSurface
        )
        SelectionContainer {
            TooltipArea(
                tooltip = {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        elevation = 4.dp
                    ) {
                        Text(
                            modifier = Modifier.padding(4.dp),
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
                .padding(start = 8.dp)
                .size(18.dp)
                .clip(RoundedCornerShape(2.dp))
                .clickable {
                    onColorTileClick()
                },
            controller = controller,
            selectedColor = Color.White
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ColorPickerUI(
    colorController: ColorPickerController
) {
    Column(
        modifier = Modifier
            .background(LocalColorProvider.current.onPrimary)
            .wrapContentHeight()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        key(colorController.hashCode()) {
            if (colorController.selectedColor.value == Color.Transparent) {
                colorController.selectByColor(Color.White, false)
            }

            Row(
                modifier = Modifier.wrapContentWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center
            ) {
                HsvColorPicker(
                    modifier = Modifier
                        .width(150.dp)
                        .height(150.dp)
                        .padding(10.dp),
                    controller = colorController,
                    initialColor = colorController.selectedColor.value
                )
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                        .wrapContentSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val pattern = remember { Regex("^#[0-9A-Fa-f]{0,8}\$") }
                    val hexColorString = remember(colorController.selectedColor.value) { mutableStateOf(colorController.selectedColor.value.toHex(true)) }
                    CustomTextField(
                        text = hexColorString.value,
                        modifier = Modifier.width(90.dp)
                    ) {
                        if (it.matches(pattern)) {
                            hexColorString.value = it
                            if (it.length == 9) {
                                colorController.selectByColor(it.toColor(), false)
                            }
                        }
                    }
                    Row(
                        modifier = Modifier
                        .wrapContentSize()
                        .padding(4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        RGBInputs(controller = colorController)
                        AlphaTile(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(30.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            controller = colorController,
                            selectedColor = colorController.selectedColor.value
                        )
                    }
                }
            }

            AlphaSlider(
                modifier = Modifier
                    .width(320.dp)
                    .padding(4.dp)
                    .height(24.dp),
                controller = colorController,
                initialColor = colorController.selectedColor.value
            )

            BrightnessSlider(
                modifier = Modifier
                    .width(320.dp)
                    .padding(4.dp)
                    .height(24.dp),
                controller = colorController,
                initialColor = colorController.selectedColor.value
            )
        }
    }
}

@Composable
fun RGBInputs(
    modifier: Modifier = Modifier,
    controller: ColorPickerController
) {
    val color = controller.selectedColor.value.toRGB()

    Column(
        modifier = modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RGBCharacteristic(text = "r: ", (color.r * 255).roundToInt()) { r ->
            val newColor = controller.selectedColor.value.copy(red = r / 255f)
            controller.selectByColor(newColor, false)
        }
        RGBCharacteristic(text = "g: ", (color.g * 255).roundToInt()) { g ->
            val newColor = controller.selectedColor.value.copy(green = g / 255f)
            controller.selectByColor(newColor, false)
        }
        RGBCharacteristic(text = "b: ", (color.b * 255).roundToInt()) { b ->
            val newColor = controller.selectedColor.value.copy(blue = b / 255f)
            controller.selectByColor(newColor, false)
        }
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
        .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            color = LocalColorProvider.current.onSurface
        )
        Spacer(Modifier.width(4.dp).height(2.dp))
        CustomTextField(colorValue.toString()) {
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
fun Scene(sceneUIState: State<SceneUIState>) =
    Korender(appResourceLoader = { Res.readBytes(it) }) {
        val sphereRadius = 6f
        val freeOrbitCamera = OrbitCamera(20.z, 2.y, sphereRadius)
        OnTouch { freeOrbitCamera.touch(it) }
        OnKey { freeOrbitCamera.handle(it)  }

        Frame {
            projection = projection(width = 3f * width / height, height = 3f, near = 3f, far = 1000f)
            camera = freeOrbitCamera.run { camera(frameInfo.time) }

            DirectionalLight(Vec3(1f, -1f, -1f), sceneUIState.value.lightColor) {
                Cascade(
                    mapSize = 2048,     // Размер карты теней (чем больше, тем качественнее)
                    near = 1f,          // Ближняя плоскость для этой каскады
                    far = 20f,          // Дальняя плоскость для этой каскады
                    algorithm = softwarePcf(
                        samples = 32,    // Количество сэмплов для мягких теней
                        blurRadius = 0.02f // Радиус размытия теней
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
                    color = sceneUIState.value.floorColor,
                    metallicFactor = 0.0f,
                    roughnessFactor = 0.6f
                ),
                mesh = cube(1f),
                transform = scale(1000f, 0.3f, 1000f)
                    .translate((-7).y)
            )

            Renderable(
                base(
                    color = sceneUIState.value.sphereColor,
                    metallicFactor = sceneUIState.value.sphereMetallicColor,
                    roughnessFactor = 0.3f
                ),
                mesh = sphere(sphereRadius),
                transform = translate(1.y)
            )
        }
    }


data class SceneUIState(
    val sphereColor: ColorRGBA,
    val sphereMetallicColor: Float,
    val lightColor: ColorRGB,
    val floorColor: ColorRGBA,
)

fun Color.toRGBA(): ColorRGBA {
    return ColorRGBA(
        r = this.red,
        g = this.green,
        b = this.blue,
        a = this.alpha
    )
}

fun Color.toRGB(): ColorRGB {
    return ColorRGB(
        r = this.red,
        g = this.green,
        b = this.blue
    )
}

/**
 * Converts this [Color] to a HEX strings.xml.
 *
 * @param withAlpha If true, returns `#AARRGGBB`, otherwise returns `#RRGGBB`.
 */
fun Color.toHex(withAlpha: Boolean = false): String {
    val r = (red * 255).roundToInt().coerceIn(0, 255)
    val g = (green * 255).roundToInt().coerceIn(0, 255)
    val b = (blue * 255).roundToInt().coerceIn(0, 255)
    val a = (alpha * 255).roundToInt().coerceIn(0, 255)

    return if (withAlpha) {
        String.format("#%02X%02X%02X%02X", a, r, g, b)
    } else {
        String.format("#%02X%02X%02X", r, g, b)
    }
}

fun String.toColor(): Color {
    val hexColor = this.substring(1)

    val alpha = hexColor.take(2).toInt(16)
    val red = hexColor.substring(2, 4).toInt(16)
    val green = hexColor.substring(4, 6).toInt(16)
    val blue = hexColor.substring(6, 8).toInt(16)
    return Color(red, green, blue, alpha)
}


