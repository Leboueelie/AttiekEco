pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    // foojay-resolver-convention is intentionally disabled for offline builds.
    // It triggers toolchain auto-provisioning (download) of a vendor-specific JDK,
    // which fails without network access. Gradle instead uses the locally detected
    // JDK via org.gradle.java.installations (see gradle.properties).
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "AttiekEco"
include(":app")
