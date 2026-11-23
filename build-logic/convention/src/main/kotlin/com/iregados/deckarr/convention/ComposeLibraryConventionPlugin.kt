package com.iregados.deckarr.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class ComposeLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.plugins.deckarr.kmp.library.get().pluginId)
        pluginManager.apply(libs.plugins.composeMultiplatform.get().pluginId)
        pluginManager.apply(libs.plugins.composeCompiler.get().pluginId)
        pluginManager.apply(libs.plugins.composeHotReload.get().pluginId)

        extensions.configure<KotlinMultiplatformExtension> {
            compilerOptions {
                freeCompilerArgs.addAll(
                    "-opt-in=kotlin.uuid.ExperimentalUuidApi",
                    "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                    "-opt-in=kotlinx.coroutines.FlowPreview",
                    "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
                    "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
                    "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                )
            }

            sourceSets.apply {
                androidMain.dependencies {
                    implementation(libs.compose.preview)
                    implementation(libs.androidx.compose.ui.tooling)
                    implementation(libs.androidx.activity.compose)
                    implementation(libs.koin.android)
                    implementation(libs.koin.android.compose)
                }
                jvmMain.dependencies {
                    implementation(libs.kotlinx.coroutinesSwing)
                }
                commonMain.dependencies {
                    implementation(libs.compose.runtime)
                    implementation(libs.compose.foundation)
                    implementation(libs.compose.material3)
                    implementation(libs.compose.components.resources)
                    implementation(libs.androidx.lifecycle.viewmodelCompose)
                    implementation(libs.androidx.lifecycle.runtimeCompose)
                    implementation(libs.icons)
                    implementation(libs.koin.core)
                    implementation(libs.koin.compose)
                    implementation(libs.koin.compose.viewmodel)
                    implementation(libs.kotlinx.json)
                    implementation(libs.kotlinx.datetime)
                    implementation(libs.coil.compose)
                    implementation(libs.coil.engine)
                    implementation(libs.compose.nav3)
                    implementation(libs.compose.viewmodelNav3)
                    implementation(libs.kermit)
                }
            }
        }
    }
}