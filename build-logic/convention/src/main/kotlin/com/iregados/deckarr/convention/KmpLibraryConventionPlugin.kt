package com.iregados.deckarr.convention

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.plugins.kotlinMultiplatform.get().pluginId)
        pluginManager.apply(libs.plugins.kmpLibrary.get().pluginId)
        pluginManager.apply(libs.plugins.kotlinxJson.get().pluginId)

        extensions.configure<KotlinMultiplatformExtension> {
            jvmToolchain(libs.versions.jdk.get().toInt())

            compilerOptions {
                freeCompilerArgs.add(
                    "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
                )
            }

            jvm()

            (this as ExtensionAware).extensions.configure<KotlinMultiplatformAndroidLibraryTarget>(
                "android"
            ) {
                compileSdk {
                    version = release(libs.versions.androidCompileSdk.get().toInt()) {
                        minorApiLevel = libs.versions.androidCompileSdkMinor.get().toInt()
                    }
                }
                minSdk = libs.versions.androidMinSdk.get().toInt()
            }

            sourceSets.apply {
                commonTest.dependencies {
                    implementation(libs.kotlin.test)
                }
            }
        }
    }
}