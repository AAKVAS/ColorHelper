package core.ui.composeComponents


import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import core.model.ImageSource

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
actual fun ImagePicker(onImagePicked: (ImageSource?) -> Unit) {
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            val source = uri?.toString()?.let {
                ImageSource.Path(it)
            }
            onImagePicked(source)
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
    onImageDropped: (ImageSource) -> Unit,
) {

}
