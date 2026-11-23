plugins {
    `kotlin-dsl`
}

group = "com.iregados.deckarr.buildlogic"

kotlin {
    jvmToolchain(libs.versions.jdk.get().toInt())
}

dependencies {
    compileOnly(files(libs::class.java.superclass.protectionDomain.codeSource.location))
    compileOnly(libs.android.gradleApi)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("kmpLibrary") {
            id = libs.plugins.deckarr.kmp.library.get().pluginId
            implementationClass = "com.iregados.deckarr.convention.KmpLibraryConventionPlugin"
        }
        register("apiLibrary") {
            id = libs.plugins.deckarr.api.library.get().pluginId
            implementationClass = "com.iregados.deckarr.convention.ApiLibraryConventionPlugin"
        }
        register("composeLibrary") {
            id = libs.plugins.deckarr.compose.library.get().pluginId
            implementationClass = "com.iregados.deckarr.convention.ComposeLibraryConventionPlugin"
        }
    }
}