package feature.palette

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import feature.palette.model.ColorPalette
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface PaletteListComponent {
    val stack: Value<ChildStack<*, Child>>
    val state: StateFlow<PaletteListStore.State>
    val labels: Flow<PaletteListStore.Label>

    fun showEditComponent(colorPalette: ColorPalette)
    fun showDeleteMessage(colorPalette: ColorPalette)
    fun onAddButtonClicked()
    fun deletePalette(colorPalette: ColorPalette)

    fun onBackClicked(toIndex: Int)

    sealed class Child {
        object NoChild: Child()
        class PaletteChild(val component: PaletteComponent): Child()
    }
}