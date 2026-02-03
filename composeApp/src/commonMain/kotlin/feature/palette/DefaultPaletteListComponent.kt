package feature.palette

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.navigate
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import feature.palette.model.ColorPalette
import feature.palette.photoPicker.DefaultPhotoPickerComponent
import feature.palette.photoPicker.PhotoPickerComponent
import feature.palette.photoPicker.PhotoPickerStoreFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

class DefaultPaletteListComponent(
    componentContext: ComponentContext,
    storeFactory: PaletteListStoreFactory
) : PaletteListComponent, ComponentContext by componentContext {
    private val _store = instanceKeeper.getStore {
        storeFactory.create()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<PaletteListStore.State> = _store.stateFlow

    override val labels: Flow<PaletteListStore.Label> = _store.labels

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, PaletteListComponent.Child>> =
        childStack(
            source = navigation,
            serializer = Config.serializer(),
            initialConfiguration = Config.NoChildren,
            handleBackButton = true,
            childFactory = ::child,
        )

    private val _modalChild = MutableValue<PaletteListComponent.ModalChild>(
        PaletteListComponent.ModalChild.NoModal
    )
    override val modalChild: Value<PaletteListComponent.ModalChild> = _modalChild

    private fun child(config: Config, componentContext: ComponentContext): PaletteListComponent.Child =
        when (config) {
            is Config.NoChildren -> PaletteListComponent.Child.NoChild
            is Config.Palette -> PaletteListComponent.Child.PaletteChild(
                paletteComponent(componentContext, config.palette)
            )
        }

    override fun onBackClicked(toIndex: Int) {
        navigation.popTo(index = toIndex)
    }

    private fun paletteComponent(
        componentContext: ComponentContext,
        colorPalette: ColorPalette
    ): PaletteComponent =
        DefaultPaletteComponent(
            componentContext = componentContext,
            storeFactory = PaletteStoreFactory(
                DefaultStoreFactory(),
                Dispatchers.IO
            ),
            colorPalette = colorPalette,
            onDelete = { deletePalette(colorPalette) },
            onNavigateBack = {
                navigation.navigate { _ ->
                    listOf(Config.NoChildren)
                }
            }
        )

    private fun photoPickerComponent(
        componentContext: ComponentContext
    ): PhotoPickerComponent {
        return DefaultPhotoPickerComponent(
            componentContext = componentContext,
            onExtractionCancel = ::closeExtractPaletteComponent,
            storeFactory = PhotoPickerStoreFactory(
                DefaultStoreFactory(),
                Dispatchers.Main
            ),
            navToPalette = { palette ->
                closeExtractPaletteComponent()
                showEditComponent(palette)
            }
        )
    }

    init {
        _store.accept(PaletteListStore.Intent.LoadPalettes)
    }


    override fun showEditComponent(colorPalette: ColorPalette) {
        navigation.navigate { _ ->
            listOf(Config.Palette(colorPalette))
        }
    }

    override fun showDeleteMessage(colorPalette: ColorPalette) {
        _store.accept(PaletteListStore.Intent.ShowDeleteMessage(colorPalette))
    }

    override fun onAddButtonClicked() {
        _store.accept(PaletteListStore.Intent.AddPalette)
    }

    override fun deletePalette(colorPalette: ColorPalette) {
        navigation.navigate { _ ->
            listOf(Config.NoChildren)
        }
        _store.accept(PaletteListStore.Intent.DeletePalette(colorPalette.uid))
    }

    override fun showExtractPaletteComponent() {
        _modalChild.value = PaletteListComponent.ModalChild.PhotoPickerChild(
            photoPickerComponent(componentContext = this)
        )
    }

    override fun closeExtractPaletteComponent() {
        _modalChild.value = PaletteListComponent.ModalChild.NoModal
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object NoChildren: Config

        @Serializable
        data class Palette(val palette: ColorPalette) : Config
    }
}