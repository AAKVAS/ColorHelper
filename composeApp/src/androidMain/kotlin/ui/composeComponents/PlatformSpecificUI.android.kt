package ui.composeComponents


import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import ui.theme.Dimens
import ui.theme.LocalColorProvider

@Composable
actual fun TooltipWrapper(
    modifier: Modifier,
    tooltip: @Composable (() -> Unit),
    content: @Composable (() -> Unit)
) {
    content()
}

@Composable
actual fun BackHandlerWrapper(
    enabled: Boolean,
    callback: () -> Unit
) {
    BackHandler(enabled = enabled) {
        callback()
    }
}

@Composable
actual fun SetupStatusBar() {
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Black,
            darkIcons = false
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
actual fun ImagePicker(onImagePicked: (String?) -> Unit) {
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            onImagePicked(uri?.toString())
        }
    )

    LaunchedEffect(Unit) {
        galleryLauncher.launch("image/*")
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun PhotoInputBox(
    modifier: Modifier,
    onImageDropped: (String) -> Unit,
    onPickButtonClick: () -> Unit
) {
    Box(
        modifier = modifier
            .padding(Dimens.paddingSmall)
            .clip(RoundedCornerShape(Dimens.roundedCornerShapeSize))
            .background(LocalColorProvider.current.onBackground)
            .padding(Dimens.paddingXXSmall)
            .clip(RoundedCornerShape(Dimens.roundedCornerShapeSize))
            .background(LocalColorProvider.current.primaryContainer)
            .height(Dimens.photoPickerHeight)
    ) {
        PickPhotoButton(modifier = Modifier.align(Alignment.Center)) {
            onPickButtonClick()
        }
    }
}
