package ui.composeComponents

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorPickerController
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import feature.colorLab.RGBCharacteristic
import ui.theme.Dimens
import ui.theme.LocalColorProvider
import utils.toColor
import utils.toHex
import utils.toRGB
import kotlin.math.roundToInt


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ColorPickerUI(
    colorController: ColorPickerController,
    defaultColor: Color,
    onCopyTextToClipboard: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .background(LocalColorProvider.current.onPrimary)
            .wrapContentSize()
            .padding(Dimens.paddingRegular),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        key(colorController.hashCode()) {
            if (colorController.selectedColor.value == Color.Transparent) {
                colorController.selectByColor(defaultColor, false)
            }

            Row(
                modifier = Modifier.wrapContentWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center
            ) {
                HsvColorPicker(
                    modifier = Modifier
                        .width(Dimens.hsvColorPickerSize)
                        .height(Dimens.hsvColorPickerSize)
                        .padding(Dimens.paddingRegular),
                    controller = colorController,
                    initialColor = defaultColor
                )
                Column(
                    modifier = Modifier
                        .padding(Dimens.paddingRegular)
                        .padding(Dimens.paddingXXSmall)
                        .wrapContentHeight()
                        .width(IntrinsicSize.Max),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ) {
                    val pattern = remember { Regex("^#[0-9A-Fa-f]{0,8}\$") }
                    val hexColorString = remember(colorController.selectedColor.value) { mutableStateOf(colorController.selectedColor.value.toHex(true)) }
                    CustomTextField(
                        text = hexColorString.value,
                        modifier = Modifier
                            .padding(start = Dimens.paddingLarge)
                            .width(Dimens.hexTextFieldWidth)
                    ) {
                        if (it.matches(pattern)) {
                            hexColorString.value = it
                            if (it.length == 9) {
                                colorController.selectByColor(it.toColor(), true)
                            }
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(Dimens.paddingXSmall),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        RGBInputs(controller = colorController)
                        Column(
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .wrapContentSize()
                                    .clip(RoundedCornerShape(Dimens.alphaTileRoundedCornerSize))
                                    .background(LocalColorProvider.current.primaryContainer)
                                    .padding(Dimens.smallestPadding)
                            ) {
                                AlphaTile(
                                    modifier = Modifier.size(Dimens.tileSize),
                                    controller = colorController,
                                    selectedColor = defaultColor
                                )
                            }
                            CopyButton(
                                modifier = Modifier.padding(top = Dimens.paddingXSmall)
                            ) {
                                onCopyTextToClipboard(
                                    colorController.selectedColor.value.toHex(withAlpha = false)
                                )
                            }
                        }
                    }
                }
            }

            AlphaSlider(
                modifier = Modifier
                    .width(Dimens.alphaSliderWidth)
                    .padding(Dimens.paddingXSmall)
                    .height(Dimens.alphaSliderHeight),
                controller = colorController,
                initialColor = defaultColor
            )

            BrightnessSlider(
                modifier = Modifier
                    .width(Dimens.alphaSliderWidth)
                    .padding(Dimens.paddingXSmall)
                    .height(Dimens.alphaSliderHeight),
                controller = colorController,
                initialColor = defaultColor
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
            controller.selectByColor(newColor, true)
        }
        RGBCharacteristic(text = "g: ", (color.g * 255).roundToInt()) { g ->
            val newColor = controller.selectedColor.value.copy(green = g / 255f)
            controller.selectByColor(newColor, true)
        }
        RGBCharacteristic(text = "b: ", (color.b * 255).roundToInt()) { b ->
            val newColor = controller.selectedColor.value.copy(blue = b / 255f)
            controller.selectByColor(newColor, true)
        }
    }
}