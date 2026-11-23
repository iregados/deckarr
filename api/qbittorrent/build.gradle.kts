plugins {
    alias(libs.plugins.deckarr.api.library)
}

kotlin {
    android {
        namespace = "com.iregados.api.qbittorrent"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.api.common)
        }
    }
}