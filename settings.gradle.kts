pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ItineraryBuilder"

// App module
include(":app")

// Core modules
include(":core:common")
include(":core:design-system")
include(":core:domain")
include(":core:database")
include(":core:network")
include(":core:data")

// Feature modules
include(":feature:home")
include(":feature:trip")
include(":feature:map")
include(":feature:destinations")
include(":feature:schedule")
include(":feature:details")
