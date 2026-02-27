package com.example

import App
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.adaptive.currentWindowSize
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.toSize
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import feature.home.DefaultRootComponent
import ui.composeComponents.SetupStatusBar

class MainActivity : ComponentActivity() {
    private lateinit var root: DefaultRootComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lifecycle = LifecycleRegistry()
        root = DefaultRootComponent(
            componentContext = DefaultComponentContext(lifecycle = lifecycle),
        )

        setContent {
            val windowSize = with(LocalDensity.current) {
                currentWindowSize().toSize().toDpSize()
            }
            SetupStatusBar()
            App(
                rootComponent = root,
                windowSize = windowSize
            )
        }
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        when (intent.action) {
            Intent.ACTION_SEND -> {
                if (intent.type?.startsWith("image/") == true) {
                    val imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)
                    } else {
                        @Suppress("DEPRECATION")
                        intent.getParcelableExtra<Uri>(Intent.EXTRA_STREAM)
                    }
                    imageUri?.let { uri ->
                        root.navigateToPaletteExtractor(uri.toString())
                    }
                }
            }
        }
    }
}