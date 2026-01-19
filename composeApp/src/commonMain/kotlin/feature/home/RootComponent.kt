package feature.home


import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import feature.colorLab.ColorLabComponent
import feature.palette.PaletteListComponent

interface RootComponent {
    val stack: Value<ChildStack<*, Child>>

    fun onBackClicked(toIndex: Int)

    fun navigateToColorLab()
    fun navigateToPaletteList()

    sealed class Child {
        class ColorLabChild(val component: ColorLabComponent): Child()
        class PaletteListChild(val component: PaletteListComponent): Child()
    }
}