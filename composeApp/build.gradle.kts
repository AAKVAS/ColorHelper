
import com.android.build.api.dsl.androidLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    kotlin("plugin.serialization") version "2.0.20"
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.example"
    generateResClass = auto
}

kotlin {
    androidLibrary  {
        namespace = "com.example"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        withJava() // enable java compilation support
//        compilerOptions.configure {
//            jvmTarget.set(
//                JavaVersion.VERSION_17
//            )
//        }
    }

    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(compose.ui)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.components.resources)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(libs.korender)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlin.coroutines.okhttp)
            implementation(libs.simplifyK)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.compose.colorpicker)
//            implementation(libs.androidx.material3.adaptive)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}
//
//android {
//    namespace = "com.example"
//    compileSdk = libs.versions.android.compileSdk.get().toInt()
//
//    defaultConfig {
//        applicationId = "com.example"
//        minSdk = libs.versions.android.minSdk.get().toInt()
//        targetSdk = libs.versions.android.targetSdk.get().toInt()
//        versionCode = 1
//        versionName = "1.0"
//    }
//    packaging {
//        resources {
//            excludes += "/META-INF/{AL2.0,LGPL2.1}"
//        }
//    }
//    buildTypes {
//        getByName("release") {
//            isMinifyEnabled = true
//        }
//    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_17
//        targetCompatibility = JavaVersion.VERSION_17
//    }
//    buildFeatures {
//        compose = true
//    }
//}

compose.desktop {
    application {
        mainClass = "MainKt"
        jvmArgs("--add-exports", "java.desktop/sun.awt=ALL-UNNAMED")

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.example"
            packageVersion = "1.0.0"
        }
    }
}
