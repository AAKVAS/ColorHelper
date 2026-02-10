package ui.composeComponents

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.background
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.awtTransferable
import androidx.compose.ui.draw.clip
import com.example.Res
import com.example.select_photo
import feature.palette.photoPicker.ImageSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.swing.Swing
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource
import ui.theme.Dimens
import ui.theme.LocalColorProvider
import java.awt.FileDialog
import java.awt.Frame
import java.awt.datatransfer.DataFlavor
import java.io.File


@OptIn(ExperimentalFoundationApi::class)
@Composable
actual fun TooltipWrapper(
    modifier: Modifier,
    tooltip: @Composable (() -> Unit),
    content: @Composable (() -> Unit)
) {
    TooltipArea(
        tooltip = tooltip,
        modifier = modifier
    ) {
        content()
    }
}


@Composable
actual fun BackHandlerWrapper(
    enabled: Boolean,
    callback: () -> Unit
) {

}


@Composable
actual fun SetupStatusBar() {

}

@Composable
actual fun ImagePicker(onImagePicked: (ImageSource?) -> Unit) {
    val title = stringResource(Res.string.select_photo)
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val fileDialog = FileDialog(
                null as Frame?,
                title,
                FileDialog.LOAD
            )

            fileDialog.setFilenameFilter { _, name ->
                name.orEmpty().lowercase().let { lowerName ->
                    lowerName.endsWith(".png") ||
                            lowerName.endsWith(".jpg") ||
                            lowerName.endsWith(".jpeg") ||
                            lowerName.endsWith(".gif") ||
                            lowerName.endsWith(".bmp") ||
                            lowerName.endsWith(".webp")
                }
            }

            withContext(Dispatchers.Swing) {
                fileDialog.isVisible = true
            }

            fileDialog.file?.let { fileName ->
                val file = File(fileDialog.directory, fileName)
                withContext(Dispatchers.Main) {
                    onImagePicked(ImageSource.Path(file.absolutePath))
                }
            } ?: run {
                withContext(Dispatchers.Main) {
                    onImagePicked(null)
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun PhotoInputBox(
    modifier: Modifier,
    onImageDropped: (ImageSource) -> Unit,
    onPickButtonClick: () -> Unit
) {
    var isDraggingOver by remember { mutableStateOf(false) }
    val dragAndDropTarget = remember {
        object : DragAndDropTarget {
            override fun onStarted(event: DragAndDropEvent) {
                super.onStarted(event)
                isDraggingOver = true
            }

            override fun onEnded(event: DragAndDropEvent) {
                super.onEnded(event)
                isDraggingOver = false
            }

            override fun onDrop(event: DragAndDropEvent): Boolean {
                val transferable = event.awtTransferable
                if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    try {
                        val data = transferable.getTransferData(DataFlavor.javaFileListFlavor)
                        val fileList = data as? List<*>

                        fileList?.filterIsInstance<File>()?.forEach { file ->
                            val name = file.name.lowercase()
                            if (name.endsWith(".png") || name.endsWith(".jpg") ||
                                name.endsWith(".jpeg") || name.endsWith(".gif") ||
                                name.endsWith(".bmp") || name.endsWith(".webp")) {

                                onImageDropped(ImageSource.Path(file.absolutePath))
                                return true
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        return false
                    }
                }

                return false
            }
        }
    }

    Box(
        modifier = modifier
            .padding(Dimens.paddingSmall)
            .clip(RoundedCornerShape(Dimens.roundedCornerShapeSize))
            .background(LocalColorProvider.current.onBackground)
            .padding(Dimens.paddingXXSmall)
            .clip(RoundedCornerShape(Dimens.roundedCornerShapeSize))
            .background(
                if (isDraggingOver)
                    LocalColorProvider.current.primary.copy(alpha = 0.1f)
                else
                    LocalColorProvider.current.primaryContainer
            )
            .height(Dimens.photoPickerHeight)
            .dragAndDropTarget(
                shouldStartDragAndDrop = { true },
                target = dragAndDropTarget
            )
    ) {
        PickPhotoButton(modifier = Modifier.align(Alignment.Center)) {
            onPickButtonClick()
        }
    }
}
