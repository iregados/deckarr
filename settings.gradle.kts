rootProject.name = "Deckarr"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(
    ":core",
    ":androidApp",
    ":api:common",
    ":api:radarr",
    ":api:sonarr",
    ":api:transmission",
    ":api:qbittorrent",
    ":feature:downloads",
    ":feature:movies",
    ":feature:series",
    ":feature:navigation",
    ":feature:search",
    ":feature:settings",
    ":feature:home",
)