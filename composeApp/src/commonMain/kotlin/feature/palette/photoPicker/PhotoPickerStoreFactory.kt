package feature.palette.photoPicker


import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import feature.palette.domain.PaletteRepository
import feature.palette.model.ColorPalette
import feature.palette.photoPicker.domain.ColorThiefPaletteExtractor
import feature.palette.photoPicker.domain.DominantColorsPaletteExtractor
import feature.palette.photoPicker.domain.ExtractionMethod
import feature.palette.photoPicker.domain.KMeansPaletteExtractor
import feature.palette.photoPicker.domain.MedianCutPaletteExtractor
import feature.palette.photoPicker.domain.PaletteExtractor
import feature.palette.photoPicker.model.Image
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.CoroutineContext

class PhotoPickerStoreFactory (
    private val storeFactory: StoreFactory,
    private val coroutineContext: CoroutineContext,
): KoinComponent {
    private val repository by inject<PaletteRepository>()

    fun create(): Store<PhotoPickerStore.Intent, PhotoPickerStore.State, Nothing> =
        object : Store<PhotoPickerStore.Intent, PhotoPickerStore.State, Nothing> by storeFactory.create(
            name = "PhotoPickerStore",
            initialState = PhotoPickerStore.State(),
            executorFactory = ::createExecutor,
            reducer = ReducerImpl()
        ) {}

    private fun createExecutor(): Executor<PhotoPickerStore.Intent, Unit, PhotoPickerStore.State, PhotoPickerStore.Msg, Nothing> =
        ExecutorImpl(repository, coroutineContext)

    private class ExecutorImpl(
        private val repository: PaletteRepository,
        coroutineContext: CoroutineContext
    ) : CoroutineExecutor<PhotoPickerStore.Intent, Unit, PhotoPickerStore.State, PhotoPickerStore.Msg, Nothing>(
        coroutineContext
    ) {
        private var _extractionJob: Job? = null

        override fun executeIntent(intent: PhotoPickerStore.Intent): Unit =
            when (intent) {
                is PhotoPickerStore.Intent.ExtractPalette -> {
                    extractPalette(intent.image, intent.colorCount, intent.extractionMethod)
                }
                is PhotoPickerStore.Intent.CancelExtraction -> {
                    cancelExtraction()
                }
                is PhotoPickerStore.Intent.SavePaletteFromExtraction -> {
                    savePalette(intent.palette)
                }
                is PhotoPickerStore.Intent.LoadImage -> {
                    dispatch(PhotoPickerStore.Msg.LoadImage)
                }
                is PhotoPickerStore.Intent.ImageNotLoaded -> {
                    dispatch(PhotoPickerStore.Msg.ImageNotLoaded)
                }
            }

        private fun extractPalette(
            image: Image,
            colorCount: Int,
            extractionMethod: ExtractionMethod
        ) {
            dispatch(PhotoPickerStore.Msg.PaletteExtractionStarted(image.path))
            val extractor = getExtractor(extractionMethod)

            _extractionJob = scope.launch(Dispatchers.Main) {
                try {
                    val palette = withContext(Dispatchers.IO) {
                        extractor.extractFromImage(image, colorCount)
                    }
                    dispatch(PhotoPickerStore.Msg.PaletteExtracted(palette))
                } catch (e: Exception) {
                    val errMessage = e.message ?: "Unforeseen error"
                    dispatch(PhotoPickerStore.Msg.Error(errMessage))
                }
            }
        }

        private fun cancelExtraction() {
            _extractionJob?.let {
                it.cancel()
                dispatch(PhotoPickerStore.Msg.ExtractionCanceled)
            }
        }

        private fun savePalette(palette: ColorPalette) {
            dispatch(PhotoPickerStore.Msg.Loading)
            scope.launch(Dispatchers.Main) {
                try {
                    withContext(Dispatchers.IO) {
                        repository.savePalette(palette)
                    }
                    dispatch(PhotoPickerStore.Msg.NavigateToSavedPalettePage)
                } catch (e: Exception) {
                    val errMessage = e.message ?: "Unforeseen error"
                    dispatch(PhotoPickerStore.Msg.Error(errMessage))
                }
            }
        }

        private fun getExtractor(extractionMethod: ExtractionMethod): PaletteExtractor {
            return when(extractionMethod) {
                ExtractionMethod.DOMINANT_COLORS -> DominantColorsPaletteExtractor()
                ExtractionMethod.K_MEANS -> KMeansPaletteExtractor()
                ExtractionMethod.MEDIAN_CUT -> MedianCutPaletteExtractor()
                ExtractionMethod.COLOR_THIEF -> ColorThiefPaletteExtractor()
            }
        }
    }

    private class ReducerImpl : Reducer<PhotoPickerStore.State, PhotoPickerStore.Msg> {
        override fun PhotoPickerStore.State.reduce(msg: PhotoPickerStore.Msg): PhotoPickerStore.State {
            return when(msg) {
                is PhotoPickerStore.Msg.PaletteExtractionStarted -> copy(
                    isLoading = true,
                    error = null,
                    selectedImagePath = msg.selectedImagePath,
                    loadImage = false,
                )
                is PhotoPickerStore.Msg.PaletteExtracted -> copy(
                    isLoading = false,
                    extractedPalette = msg.palette
                )
                is PhotoPickerStore.Msg.ExtractionCanceled -> copy(
                    isLoading = false,
                    selectedImagePath = null
                )
                is PhotoPickerStore.Msg.Error -> copy(
                    isLoading = false,
                    error = msg.message,
                    loadImage = false,
                )
                is PhotoPickerStore.Msg.Loading -> copy(isLoading = true)
                is PhotoPickerStore.Msg.NavigateToSavedPalettePage -> copy(
                    navigateToPalettePage = true,

                )
                is PhotoPickerStore.Msg.LoadImage -> copy(
                    isLoading = true,
                    loadImage = true
                )
                is PhotoPickerStore.Msg.ImageNotLoaded -> copy(
                    isLoading = false,
                    loadImage = false,
                )
            }
        }
    }
}
