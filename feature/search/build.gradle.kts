plugins {
    alias(libs.plugins.deckarr.compose.library)
}

kotlin {
    android {
        namespace = "com.iregados.deckarr.feature.search"
        androidResources.enable = true
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core)
            implementation(projects.api.common)
            implementation(projects.api.radarr)
            implementation(projects.api.sonarr)
        }
    }
}