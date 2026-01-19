package feature.colorLab

import feature.colorLab.model.ColorLabState
import kotlinx.coroutines.flow.StateFlow

interface ColorLabComponent {
    val state: StateFlow<ColorLabState>

    fun updateState(colorLabState: ColorLabState)
}