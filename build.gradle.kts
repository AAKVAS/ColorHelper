plugins {
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kspPlugin) apply false
    alias(libs.plugins.roomPlugin) apply(false)
}