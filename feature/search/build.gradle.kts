plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kmpLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlinxJson)
}

kotlin {
    jvmToolchain(libs.versions.jdk.get().toInt())

    compilerOptions {
        freeCompilerArgs.addAll(
            "-opt-in=kotlin.uuid.ExperimentalUuidApi",
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-opt-in=kotlinx.coroutines.FlowPreview",
            "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
            "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
            "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
            "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
        )
    }

    jvm()

    androidLibrary {
        namespace = "com.iregados.deckarr.feature.search"
        compileSdk = libs.versions.androidCompileSdk.get().toInt()
        minSdk = libs.versions.androidMinSdk.get().toInt()
        androidResources.enable = true
    }

    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(libs.compose.preview)
                implementation(libs.androidx.activity.compose)
                implementation(libs.koin.android)
                implementation(libs.koin.android.compose)
            }
        }
        val commonMain by getting {
            dependencies {
                implementation(projects.core)
                implementation(projects.api.common)
                implementation(projects.api.radarr)
                implementation(projects.api.sonarr)
                implementation(projects.feature.movies)
                implementation(projects.feature.series)

                implementation(libs.compose.material3)
                implementation(libs.compose.components.resources)
                implementation(libs.androidx.compose.ui.tooling)
                implementation(libs.androidx.compose.ui.tooling.preview)
                implementation(libs.androidx.lifecycle.viewmodelCompose)
                implementation(libs.androidx.lifecycle.runtimeCompose)
                implementation(libs.icons)
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
                implementation(libs.kotlinx.json)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.coroutinesSwing)
                implementation(libs.coil.compose)
                implementation(libs.coil.engine)
                implementation(libs.compose.nav3)
                implementation(libs.compose.viewmodelNav3)
                implementation(libs.kermit)
            }
        }
    }
}
