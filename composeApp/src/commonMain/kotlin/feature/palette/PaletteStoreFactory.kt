package feature.palette

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import feature.palette.domain.PaletteRepository
import feature.palette.model.ColorModel
import feature.palette.model.ColorPalette
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.UUID
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
                is PaletteStore.Intent.UpdateSelectedColorUid -> {
                    dispatch(PaletteStore.Msg.UpdateSelectedColorUid(intent.uid))
                }
                is PaletteStore.Intent.ShowDeleteColorDialog -> {
                    publish(PaletteStore.Label.ShowDeleteColorDialog(intent.uid))
                }
                is PaletteStore.Intent.DeleteColor -> {
                    deleteColor(intent.color)
                }
                is PaletteStore.Intent.UpdateColor -> {
                    updateColor(intent.color)
                }
            }

        private fun observePalette() {
            scope.launch(Dispatchers.Main) {
                try {
                    withContext(Dispatchers.IO) {
                        repository.observePalette(state().palette.uid).collect {
                            it?.let { palette ->
                                withContext(Dispatchers.Main) {
                                    dispatch(PaletteStore.Msg.PaletteUpdated(palette))
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    val errMessage = e.message ?: "Unforeseen error"
                    dispatch(PaletteStore.Msg.Error(errMessage))
                    publish(PaletteStore.Label.ShowMessage(errMessage))
                }
            }
        }

        private fun updatePalette(colorPalette: ColorPalette) {
            scope.launch(Dispatchers.Main) {
                try {
                    withContext(Dispatchers.IO) {
                        repository.updatePalette(colorPalette)
                    }
                    dispatch(PaletteStore.Msg.PaletteUpdated(colorPalette))
                } catch (e: Exception) {
                    val errMessage = e.message ?: "Unforeseen error"
                    dispatch(PaletteStore.Msg.Error(errMessage))
                    publish(PaletteStore.Label.ShowMessage(errMessage))
                }
            }
        }

        private fun deletePalette() {
            scope.launch(Dispatchers.Main) {
                try {
                    withContext(Dispatchers.IO) {
                        repository.deletePalette(state().palette.uid)
                    }
                } catch (e: Exception) {
                    val errMessage = e.message ?: "Unforeseen error"
                    dispatch(PaletteStore.Msg.Error(errMessage))
                    publish(PaletteStore.Label.ShowMessage(errMessage))
                }
            }
        }

        private fun addColor(color: String) {
            scope.launch(Dispatchers.Main) {
                try {
                    val palette = state().palette
                    val uid = UUID.randomUUID().toString()
                    val colorModel = ColorModel(
                        uid = uid,
                        paletteUid = palette.uid,
                        value = color,
                        createdAt = System.currentTimeMillis()
                    )
                    withContext(Dispatchers.IO) {
                        repository.addColor(colorModel)
                    }
                    dispatch(PaletteStore.Msg.UpdateSelectedColorUid(uid))
                } catch (e: Exception) {
                    val errMessage = e.message ?: "Unforeseen error"
                    dispatch(PaletteStore.Msg.Error(errMessage))
                    publish(PaletteStore.Label.ShowMessage(errMessage))
                }
            }
        }

        private fun deleteColor(color: ColorModel) {
            scope.launch(Dispatchers.Main) {
                try {
                    val index = state().palette.colors.indexOf(color)
                    withContext(Dispatchers.IO) {
                        repository.deleteColor(color)
                    }
                    val newSelectedUid = state().palette.colors.getOrNull(index)?.uid
                    dispatch(PaletteStore.Msg.UpdateSelectedColorUid(newSelectedUid))
                } catch (e: Exception) {
                    val errMessage = e.message ?: "Unforeseen error"
                    dispatch(PaletteStore.Msg.Error(errMessage))
                    publish(PaletteStore.Label.ShowMessage(errMessage))
                }
            }
        }

        private fun updateColor(color: ColorModel) {
            scope.launch(Dispatchers.Main) {
                try {
                    withContext(Dispatchers.IO) {
                        repository.updateColor(color)
                    }
                } catch (e: Exception) {
                    val errMessage = e.message ?: "Unforeseen error"
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
                is PaletteStore.Msg.UpdateSelectedColorUid -> {
                    copy(selectedColorUid = msg.uid)
                }
            }
        }
    }
}