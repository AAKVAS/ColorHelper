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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


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
                RootComponent.Child.ColorLabChild(
                    colorLabComponent(componentContext)
                ),
                RootComponent.Child.ImageBusketChild(
                    imageBusketComponent(componentContext)
                )
            )
        } else {
            listOf(
                RootComponent.Child.PaletteListChild(
                    paletteListComponent(componentContext)
                ),
                RootComponent.Child.ColorLabChild(
                    colorLabComponent(componentContext)
                )
            )
        }

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
                    return false
                }
            }
        }
        return true
    }

    private fun closeWindow() {
        _closeConfirmed.value = true
    }
}