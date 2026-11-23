plugins {
    alias(libs.plugins.deckarr.compose.library)
}

kotlin {
    android {
        namespace = "com.iregados.deckarr.feature.movies"
        androidResources.enable = true
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core)
            implementation(projects.api.radarr)
            implementation(projects.api.common)
        }
    }
}