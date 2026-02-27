package feature.home

import feature.colorLab.ColorLabComponent
import feature.imageBusket.ImageBusketComponent
import feature.palette.PaletteListComponent
import feature.perspectiveBuilder.PerspectiveBuilderComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface RootComponent {
    val children: List<Child>
    val openChild: Flow<Child>
    sealed class Child {
        class ColorLabChild(val component: ColorLabComponent): Child()
        class PaletteListChild(val component: PaletteListComponent): Child()
        class ImageBusketChild(val component: ImageBusketComponent): Child()
        class PerspectiveBuilderChild(val component: PerspectiveBuilderComponent): Child()
    }
    val closeConfirmed: StateFlow<Boolean>

    fun navigateToPaletteExtractor(uri: String)
}