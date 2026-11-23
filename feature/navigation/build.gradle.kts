plugins {
    alias(libs.plugins.deckarr.compose.library)
}

kotlin {
    android {
        namespace = "com.iregados.deckarr.feature.navigation"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core)
            implementation(projects.api.common)
            implementation(projects.api.radarr)
            implementation(projects.api.sonarr)
            implementation(projects.feature.downloads)
            implementation(projects.feature.home)
            implementation(projects.feature.movies)
            implementation(projects.feature.search)
            implementation(projects.feature.series)
            implementation(projects.feature.settings)
        }
    }
}