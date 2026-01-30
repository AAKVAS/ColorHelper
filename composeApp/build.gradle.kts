
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
    kotlin("plugin.serialization") version "2.0.20"
    alias(libs.plugins.kspPlugin)
    alias(libs.plugins.roomPlugin)
    alias(libs.plugins.android.application)
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.example"
    generateResClass = auto
}

room {
    schemaDirectory("$projectDir/schemas")
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
        configurations {
            create("cleanedAnnotations")
            implementation {
                exclude(group = "org.jetbrains", module = "annotations")
            }
        }
    }

    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.adaptive)
            implementation(libs.google.systemuicontroller)
        }
        commonMain.dependencies {
            implementation(compose.ui)
            implementation(compose.foundation)
            implementation(compose.material3)
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
            implementation(libs.decompose)
            implementation(libs.decompose.extensions)

            implementation(libs.mvikotlin)
            implementation(libs.mvikotlin.main)
            implementation(libs.mvikotlin.logging)
            implementation(libs.mvikotlin.timetravel)
            implementation(libs.mvikotlin.coroutines)
            implementation(libs.decompose.lifecycle.coroutines)

            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)
            implementation(libs.androidx.room.compiler)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}



dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspDesktop", libs.androidx.room.compiler)
}

android {
    namespace = "org.example"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.example"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        jvmArgs += listOf(
            "--add-opens", "java.base/java.lang=ALL-UNNAMED",
            "--add-opens", "java.base/java.util=ALL-UNNAMED",
            "--add-opens", "java.base/sun.security.action=ALL-UNNAMED",
            "--add-opens", "java.base/sun.nio.ch=ALL-UNNAMED",
            "--add-opens", "java.base/sun.security.x509=ALL-UNNAMED",
            "--add-opens", "java.desktop/sun.awt=ALL-UNNAMED",
            "--add-opens", "java.desktop/sun.swing=ALL-UNNAMED",
            "--add-exports", "java.desktop/sun.awt=ALL-UNNAMED",
            "--add-exports", "java.base/sun.security.x509=ALL-UNNAMED",
            "--add-exports", "java.base/sun.security.util=ALL-UNNAMED",
            "--add-exports", "java.desktop/com.sun.java.swing.plaf.windows=ALL-UNNAMED"
        )

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Exe)
            packageName = "colorHelper"
            packageVersion = "1.0.0"

            modules("java.sql", "jdk.unsupported")

            windows {
                iconFile.set(project.file("launcher.ico"))
                menuGroup = "Color Helper"
                menu = true
                shortcut = true

                jvmArgs += listOf(
                    "-Djava.awt.headless=false",
                    "--add-opens", "java.base/sun.nio.ch=ALL-UNNAMED",
                    "--add-opens", "java.base/java.lang=ALL-UNNAMED",
                    "--add-opens", "java.base/java.util=ALL-UNNAMED"
                )
                includeAllModules = true
            }
        }
    }
}
