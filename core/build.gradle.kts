plugins {
    alias(libs.plugins.deckarr.compose.library)
}

kotlin {
    android {
        namespace = "com.iregados.deckarr.core"
        androidResources.enable = true
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.api.common)
            implementation(projects.api.transmission)
            implementation(projects.api.qbittorrent)
            implementation(projects.api.radarr)
            implementation(projects.api.sonarr)
            implementation(projects.api.tmdb)

            implementation(libs.ktor.client.core)
            implementation(libs.datastore)
            implementation(libs.datastore.preferences)
        }
    }
}