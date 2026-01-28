package feature.palette

import com.arkivanov.mvikotlin.core.store.Store
import feature.palette.model.ColorPalette


interface PaletteListStore : Store<PaletteListStore.Intent, PaletteListStore.State, PaletteListStore.Label>
{
    sealed class Intent {
        object LoadPalettes: Intent()
        object AddPalette: Intent()
        data class DeletePalette(val id: String): Intent()
        data class ShowEditPage(val colorPalette: ColorPalette): Intent()
        data class ShowDeleteMessage(val colorPalette: ColorPalette): Intent()
    }

    data class State(
        val items: List<ColorPalette> = emptyList(),
        val isLoading: Boolean = false,
        val error: String? = null,
        val editedPalette: ColorPalette? = null
    )

    sealed class Label {
        data class ShowMessage(val message: String) : Label()
        data class ShowEditPage(val palette: ColorPalette): Label()
        data class ShowDeleteDialog(val palette: ColorPalette) : Label()
    }

    sealed class Msg {
        data object LoadingStarted : Msg()
        data object LoadingFinished : Msg()
        data class PalettesLoaded(val palettes: List<ColorPalette>) : Msg()
        data class Error(val message: String) : Msg()
        data class PaletteAdded(val palette: ColorPalette) : Msg()
        data class PaletteDeleted(val id: String) : Msg()
        data class PaletteUpdated(val palette: ColorPalette) : Msg()
        data class EditPalettePageOpened(val palette: ColorPalette) : Msg()
    }
}