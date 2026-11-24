plugins {
    alias(libs.plugins.deckarr.api.library)
}

kotlin {
    android {
        namespace = "com.iregados.api.tmdb"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.api.common)
        }
    }
}