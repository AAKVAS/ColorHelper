package feature.palette

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import feature.palette.domain.PaletteRepository
import feature.palette.model.ColorPalette
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.CoroutineContext

class PaletteStoreFactory(
    private val storeFactory: StoreFactory,
    private val coroutineContext: CoroutineContext,
): KoinComponent {
    private val repository by inject<PaletteRepository>()

    fun create(palette: ColorPalette): Store<PaletteStore.Intent, PaletteStore.State, PaletteStore.Label> =
        object : Store<PaletteStore.Intent, PaletteStore.State, PaletteStore.Label> by storeFactory.create(
            name = "PaletteStore",
            initialState = PaletteStore.State(palette),
            executorFactory = ::createExecutor,
            reducer = ReducerImpl()
        ) {}

    private fun createExecutor(): Executor<PaletteStore.Intent, Unit, PaletteStore.State, PaletteStore.Msg, PaletteStore.Label> =
        ExecutorImpl(repository, coroutineContext)

    private class ExecutorImpl(
        private val repository: PaletteRepository,
        coroutineContext: CoroutineContext
    ) : CoroutineExecutor<PaletteStore.Intent, Unit, PaletteStore.State, PaletteStore.Msg, PaletteStore.Label>(
        coroutineContext
    ) {
        override fun executeIntent(intent: PaletteStore.Intent): Unit =
            when (intent) {
                is PaletteStore.Intent.ObservePalette -> {
                    observePalette()
                }
                is PaletteStore.Intent.UpdatePalette -> {
                    updatePalette(intent.colorPalette)
                }
                is PaletteStore.Intent.DeletePalette -> {
                    deletePalette()
                }
                is PaletteStore.Intent.ShowDeleteDialog -> {
                    publish(PaletteStore.Label.ShowDeletePaletteDialog)
                }
                is PaletteStore.Intent.SelectHarmoniousColor -> {
                    selectHarmoniousColors(intent.color)
                }
                is PaletteStore.Intent.UpdateHarmoniousColors -> {
                    refillHarmoniousColors()
                }
                is PaletteStore.Intent.AddColor -> {
                    addColor(intent.color)
                }
                is PaletteStore.Intent.UpdateSelectedColorIndex -> {
                    dispatch(PaletteStore.Msg.UpdateSelectedColorIndex(intent.index))
                }
                is PaletteStore.Intent.ShowDeleteColorDialog -> {
                    publish(PaletteStore.Label.ShowDeleteColorDialog(intent.index))
                }
            }

        private fun observePalette() {
            scope.launch {
                try {
                    repository.observePalette(state().palette.id).collect {
                        it?.let { palette ->
                            dispatch(PaletteStore.Msg.PaletteUpdated(palette))
                        }
                    }
                } catch (e: Exception) {
                    val errMessage = e.message ?: "Непредвиденная ошибка"
                    dispatch(PaletteStore.Msg.Error(errMessage))
                    publish(PaletteStore.Label.ShowMessage(errMessage))
                }
            }
        }

        private fun updatePalette(palette: ColorPalette) {
            scope.launch {
                try {
                    repository.updatePalette(palette)
                } catch (e: Exception) {
                    val errMessage = e.message ?: "Непредвиденная ошибка"
                    dispatch(PaletteStore.Msg.Error(errMessage))
                    publish(PaletteStore.Label.ShowMessage(errMessage))
                }
            }
        }

        private fun deletePalette() {
            scope.launch {
                try {
                    repository.deletePalette(state().palette.id)
                } catch (e: Exception) {
                    val errMessage = e.message ?: "Непредвиденная ошибка"
                    dispatch(PaletteStore.Msg.Error(errMessage))
                    publish(PaletteStore.Label.ShowMessage(errMessage))
                }
            }
        }

        private fun addColor(color: String) {
            scope.launch {
                try {
                    val colors = state().palette.colors + color
                    val newPalette = state().palette.copy(colors = colors)
                    repository.updatePalette(newPalette)
                    dispatch(PaletteStore.Msg.UpdateSelectedColorIndex(colors.size - 1))
                } catch (e: Exception) {
                    val errMessage = e.message ?: "Непредвиденная ошибка"
                    dispatch(PaletteStore.Msg.Error(errMessage))
                    publish(PaletteStore.Label.ShowMessage(errMessage))
                }
            }

        }

        private fun selectHarmoniousColors(color: String) {
            val harmoniousColors = state().harmoniousColors.filter { it != color }
            addColor(color)
            dispatch(PaletteStore.Msg.UpdateHarmoniousColors(harmoniousColors))
            refillHarmoniousColors()
        }

        private fun refillHarmoniousColors() {
            //TODO very complex logic
        }
    }

    private class ReducerImpl : Reducer<PaletteStore.State, PaletteStore.Msg> {
        override fun PaletteStore.State.reduce(msg: PaletteStore.Msg): PaletteStore.State {
            return when(msg) {
                is PaletteStore.Msg.PaletteUpdated -> copy(palette = msg.palette)
                is PaletteStore.Msg.Error -> copy(error = msg.message)
                is PaletteStore.Msg.UpdateHarmoniousColors -> copy(harmoniousColors = msg.colors)
                is PaletteStore.Msg.UpdateSelectedColorIndex -> {
                    copy(selectedColorIndex = msg.index)
                }
            }
        }
    }
}