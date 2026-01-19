package feature.palette

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import feature.palette.model.ColorPalette
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow


class DefaultPaletteComponent(
    componentContext: ComponentContext,
    storeFactory: PaletteStoreFactory,
    colorPalette: ColorPalette,
    val onDelete: () -> Unit
) : PaletteComponent, ComponentContext by componentContext {
    private val _store = instanceKeeper.getStore {
        storeFactory.create(colorPalette)
    }

    init {
        _store.accept(PaletteStore.Intent.ObservePalette)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<PaletteStore.State> = _store.stateFlow

    override val labels: Flow<PaletteStore.Label> = _store.labels

    override fun selectHarmoniousColor(color: String) {
        _store.accept(PaletteStore.Intent.SelectHarmoniousColor(color))
    }

    override fun showDeleteDialog() {
        _store.accept(PaletteStore.Intent.ShowDeleteDialog)
    }

    override fun updatePalette(palette: ColorPalette) {
        _store.accept(PaletteStore.Intent.UpdatePalette(palette))
    }

    override fun addColor(color: String) {
        _store.accept(PaletteStore.Intent.AddColor(color))
    }

    override fun updateColor(index: Int, color: String) {
        val colors = _store.state.palette.colors.mapIndexed { i, currentColor ->
            if (i == index) {
                color
            } else {
                currentColor
            }
        }
        updatePalette(_store.state.palette.copy(colors = colors))
    }

    override fun updateSelectedColorIndex(index: Int) {
        _store.accept(PaletteStore.Intent.UpdateSelectedColorIndex(index))
    }

    override fun showDeleteColorDialog(index: Int) {
        _store.accept(PaletteStore.Intent.ShowDeleteColorDialog(index))
    }

    override fun deleteColorByIndex(index: Int) {
        val colors = _store.state.palette.colors.filterIndexed { i, _ ->
            i != index
        }
        val palette = _store.state.palette.copy(colors = colors)
        _store.accept(PaletteStore.Intent.UpdatePalette(palette))
    }

    override fun deletePalette() {
        onDelete()
    }
}