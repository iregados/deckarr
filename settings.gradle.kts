rootProject.name = "Deckarr"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":core")
include(":androidApp")
include(":api:common")
include(":api:radarr")
include(":api:sonarr")
include(":api:transmission")
include(":api:qbittorrent")
include(":feature:downloads")
include(":feature:movies")
include(":feature:series")
include(":feature:navigation")
include(":feature:search")
include(":feature:settings")
include(":feature:home")
