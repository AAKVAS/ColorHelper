package feature.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import feature.colorLab.ColorLabComponent
import feature.colorLab.DefaultColorLabComponent
import feature.imageBusket.DefaultImageBusketComponent
import feature.imageBusket.ImageBusketComponent
import feature.imageBusket.ImageBusketStoreFactory
import feature.palette.DefaultPaletteListComponent
import feature.palette.PaletteListComponent
import feature.palette.PaletteListStoreFactory
import feature.perspectiveBuilder.DefaultPerspectiveBuilderComponent
import feature.perspectiveBuilder.PerspectiveBuilderComponent
import feature.perspectiveBuilder.PerspectiveBuilderStoreFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class DefaultRootComponent(
    componentContext: ComponentContext,
    withImageBusket: Boolean = false
) : RootComponent, CloseHandler, ComponentContext by componentContext {
    override val children: List<RootComponent.Child> =
        if (withImageBusket) {
            listOf(
                RootComponent.Child.PaletteListChild(
                    paletteListComponent(componentContext)
                ),
                RootComponent.Child.ImageBusketChild(
                    imageBusketComponent(componentContext)
                ),
                RootComponent.Child.PerspectiveBuilderChild(
                    perspectiveBuilderComponent(componentContext)
                ),
                RootComponent.Child.ColorLabChild(
                    colorLabComponent(componentContext)
                ),
            )
        } else {
            listOf(
                RootComponent.Child.PaletteListChild(
                    paletteListComponent(componentContext)
                ),
                RootComponent.Child.PerspectiveBuilderChild(
                    perspectiveBuilderComponent(componentContext)
                ),
                RootComponent.Child.ColorLabChild(
                    colorLabComponent(componentContext)
                ),
            )
        }

    private val _openChild = MutableSharedFlow<RootComponent.Child>()
    override val openChild: Flow<RootComponent.Child> = _openChild.asSharedFlow()

    private val _closeConfirmed = MutableStateFlow(false)
    override val closeConfirmed: StateFlow<Boolean>
        get() = _closeConfirmed.asStateFlow()

    private fun colorLabComponent(componentContext: ComponentContext): ColorLabComponent =
        DefaultColorLabComponent(
            componentContext = componentContext
        )

    private fun imageBusketComponent(componentContext: ComponentContext): ImageBusketComponent =
        DefaultImageBusketComponent(
            componentContext = componentContext,
            storeFactory = ImageBusketStoreFactory(
                DefaultStoreFactory(),
                Dispatchers.IO
            ),
            onClose = ::closeWindow
        )

    private fun perspectiveBuilderComponent(componentContext: ComponentContext): PerspectiveBuilderComponent =
        DefaultPerspectiveBuilderComponent(
            componentContext = componentContext,
            storeFactory = PerspectiveBuilderStoreFactory(
                DefaultStoreFactory(),
                Dispatchers.IO
            )
        )

    @OptIn(DelicateDecomposeApi::class)
    private fun paletteListComponent(componentContext: ComponentContext): PaletteListComponent =
        DefaultPaletteListComponent(
            componentContext = componentContext,
            storeFactory = PaletteListStoreFactory(
                DefaultStoreFactory(),
                Dispatchers.IO
            )
        )

    override fun onWindowClosing(): Boolean {
        for (child in children) {
            if (child is RootComponent.Child.ImageBusketChild) {
                val canClose = child.component.onWindowClosing()
                if (!canClose) {
                    CoroutineScope(Dispatchers.Main).launch {
                        _openChild.emit(child)
                    }
                    return false
                }
            }
        }
        return true
    }

    override fun navigateToPaletteExtractor(uri: String) {
        val paletteListComponent = (children.first {
            it is RootComponent.Child.PaletteListChild
        } as RootComponent.Child.PaletteListChild).component

        paletteListComponent.showExtractPaletteComponent(uri)
    }

    private fun closeWindow() {
        _closeConfirmed.value = true
    }
}