package feature.home



import feature.colorLab.ColorLabComponent
import feature.imageBusket.ImageBusketComponent
import feature.palette.PaletteListComponent
import kotlinx.coroutines.flow.StateFlow

interface RootComponent {
    val children: List<Child>
    sealed class Child {
        class ColorLabChild(val component: ColorLabComponent): Child()
        class PaletteListChild(val component: PaletteListComponent): Child()
        class ImageBusketChild(val component: ImageBusketComponent): Child()
    }
    val closeConfirmed: StateFlow<Boolean>
}