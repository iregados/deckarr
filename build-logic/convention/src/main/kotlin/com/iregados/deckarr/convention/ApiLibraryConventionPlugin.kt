package com.iregados.deckarr.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class ApiLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(libs.plugins.deckarr.kmp.library.get().pluginId)

        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.apply {
                commonMain.dependencies {
                    implementation(libs.ktor.client.core)
                    implementation(libs.ktor.client.okhttp)
                    implementation(libs.ktor.client.content.negotiation)
                    implementation(libs.ktor.client.logging)
                    implementation(libs.ktor.serialization.kotlinx.json)
                    implementation(libs.kotlinx.json)
                    implementation(libs.kermit)
                }
            }
        }
    }
}