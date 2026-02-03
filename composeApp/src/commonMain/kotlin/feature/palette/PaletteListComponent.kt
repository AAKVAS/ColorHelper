package feature.palette

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import feature.palette.model.ColorPalette
import feature.palette.photoPicker.PhotoPickerComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface PaletteListComponent {
    val stack: Value<ChildStack<*, Child>>
    val state: StateFlow<PaletteListStore.State>
    val labels: Flow<PaletteListStore.Label>
    val modalChild: Value<ModalChild>

    fun showEditComponent(colorPalette: ColorPalette)
    fun showDeleteMessage(colorPalette: ColorPalette)
    fun onAddButtonClicked()
    fun deletePalette(colorPalette: ColorPalette)
    fun showExtractPaletteComponent()
    fun closeExtractPaletteComponent()

    fun onBackClicked(toIndex: Int)

    sealed class Child {
        object NoChild: Child()
        class PaletteChild(val component: PaletteComponent): Child()
    }


    sealed class ModalChild {
        object NoModal : ModalChild()
        class PhotoPickerChild(val component: PhotoPickerComponent): ModalChild()
    }
}