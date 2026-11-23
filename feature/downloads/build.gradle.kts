plugins {
    alias(libs.plugins.deckarr.compose.library)
}

kotlin {
    android {
        namespace = "com.iregados.deckarr.feature.downloads"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.core)
            implementation(projects.api.common)
        }
    }
}