package feature.palette

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import feature.palette.model.ColorModel
import feature.palette.model.ColorPalette
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow


class DefaultPaletteComponent(
    componentContext: ComponentContext,
    storeFactory: PaletteStoreFactory,
    colorPalette: ColorPalette,
    val onDelete: () -> Unit,
    val onNavigateBack: () -> Unit
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

    override fun addColor(color: String) {
        _store.accept(PaletteStore.Intent.AddColor(color))
    }

    override fun updateColor(color: ColorModel) {
        _store.accept(PaletteStore.Intent.UpdateColor(color))
    }

    override fun updateSelectedColorUid(uid: String) {
        _store.accept(PaletteStore.Intent.UpdateSelectedColorUid(uid))
    }

    override fun showDeleteColorDialog(uid: String) {
        _store.accept(PaletteStore.Intent.ShowDeleteColorDialog(uid))
    }

    override fun deleteColor(color: ColorModel) {
        _store.accept(PaletteStore.Intent.DeleteColor(color))
    }

    override fun deletePalette() {
        onDelete()
    }

    override fun updatePalette(colorPalette: ColorPalette) {
        _store.accept(PaletteStore.Intent.UpdatePalette(colorPalette))
    }

    override fun navigateBack() {
        onNavigateBack()
    }
}