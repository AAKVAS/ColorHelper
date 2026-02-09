package feature.imageBusket

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize


@Composable
expect fun ImageBusketScreen(
    modifier: Modifier = Modifier,
    component: ImageBusketComponent,
    windowSize: DpSize,
)