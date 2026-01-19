package feature.palette

import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import feature.palette.PaletteListStore.Intent
import feature.palette.PaletteListStore.Msg
import feature.palette.domain.PaletteRepository
import feature.palette.model.ColorPalette
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.UUID
import kotlin.coroutines.CoroutineContext

class PaletteListStoreFactory(
    private val storeFactory: StoreFactory,
    private val coroutineContext: CoroutineContext,
): KoinComponent {
    private val repository by inject<PaletteRepository>()

    fun create(): Store<Intent, PaletteListStore.State, PaletteListStore.Label> =
        object : Store<Intent, PaletteListStore.State, PaletteListStore.Label> by storeFactory.create(
            name = "PaletteListStore",
            initialState = PaletteListStore.State(),
            executorFactory = ::createExecutor,
            reducer = ReducerImpl()
        ) {}

    private fun createExecutor(): Executor<Intent, Unit, PaletteListStore.State, Msg, PaletteListStore.Label> =
        ExecutorImpl(repository, coroutineContext)

    private class ExecutorImpl(
        private val repository: PaletteRepository,
        coroutineContext: CoroutineContext
    ) : CoroutineExecutor<Intent, Unit, PaletteListStore.State, Msg, PaletteListStore.Label>(
        coroutineContext
    ) {
        override fun executeIntent(intent: Intent): Unit =
            when (intent) {
                is Intent.LoadPalettes -> {
                    loadPalettes()
                }
                is Intent.AddPalette -> {
                    addPalette()
                }
                is Intent.DeletePalette -> {
                    deletePalette(intent.id)
                }
                is Intent.ShowEditPage -> {
                    dispatch(Msg.EditPalettePageOpened(intent.colorPalette))
                }
                is Intent.EditPalette -> {
                    editPalette(intent.colorPalette)
                }
                is Intent.ShowDeleteMessage -> {
                    publish(PaletteListStore.Label.ShowDeleteDialog(intent.colorPalette))
                }
            }

        private fun loadPalettes() {
            dispatch(Msg.LoadingStarted)
            scope.launch {
                try {
                    val palettes = repository.getPalettes()
                    dispatch(Msg.PalettesLoaded(palettes))

                    repository.observePalettes().collect {
                        dispatch(Msg.PalettesLoaded(it))
                    }
                } catch (e: Exception) {
                    val errMessage = e.message ?: "Непредвиденная ошибка"
                    dispatch(Msg.Error(errMessage))
                    publish(PaletteListStore.Label.ShowMessage(errMessage))
                } finally {
                    dispatch(Msg.LoadingFinished)
                }
            }
        }

        private fun addPalette() {
            scope.launch {
                try {
                    val newPalette = ColorPalette(
                        id = UUID.randomUUID().toString(),
                        name = "New palette",
                        colors = listOf()
                    )
                    repository.savePalette(newPalette)
                    dispatch(Msg.EditPalettePageOpened(newPalette))
                    publish(PaletteListStore.Label.ShowEditPage(newPalette))
                } catch (e: Exception) {
                    val errMessage = e.message ?: "Непредвиденная ошибка"
                    dispatch(Msg.Error(errMessage))
                    publish(PaletteListStore.Label.ShowMessage(errMessage))
                }
            }
        }

        private fun deletePalette(id: String) {
            scope.launch {
                try {
                    repository.deletePalette(id)
                    dispatch(Msg.PaletteDeleted(id))
                } catch (e: Exception) {
                    val errMessage = e.message ?: "Непредвиденная ошибка"
                    dispatch(Msg.Error(errMessage))
                    publish(PaletteListStore.Label.ShowMessage(errMessage))
                }
            }
        }

        private fun editPalette(colorPalette: ColorPalette) {
            scope.launch {
                try {
                    repository.updatePalette(colorPalette)
                } catch (e: Exception) {
                    val errMessage = e.message ?: "Непредвиденная ошибка"
                    dispatch(Msg.Error(errMessage))
                    publish(PaletteListStore.Label.ShowMessage(errMessage))
                }
            }
        }
    }

    private class ReducerImpl : Reducer<PaletteListStore.State, Msg> {
        override fun PaletteListStore.State.reduce(msg: Msg): PaletteListStore.State {
            return when(msg) {
                is Msg.LoadingStarted -> copy(isLoading = true, error = null)
                is Msg.LoadingFinished -> copy(isLoading = false)
                is Msg.PalettesLoaded -> copy(items = msg.palettes)
                is Msg.Error -> copy(error = msg.message)
                is Msg.PaletteAdded -> copy(items = items + msg.palette)
                is Msg.PaletteDeleted -> copy(items = items.filter { it.id != msg.id })
                is Msg.PaletteUpdated -> {
                    copy(
                        items = items.map {
                            if (it.id == msg.palette.id) msg.palette else it
                        },
                        editedPalette = null
                    )
                }
                is Msg.EditPalettePageOpened -> copy(editedPalette = msg.palette)
            }
        }
    }
}
