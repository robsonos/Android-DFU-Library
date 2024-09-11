pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
    versionCatalogs {
        create("libs") {
            from("no.nordicsemi.android.gradle:version-catalog:2.4-1")
        }
    }
}
rootProject.name = "Device Firmware Update"

include(":app")
include(":lib:analytics")
include(":lib:dfu")
include(":lib:storage")
include(":lib:settings")
include(":profile:main")
include(":profile:scanner")
include(":profile:settings")
include(":profile:welcome")
include(":profile:navigation")

//if (file("../Kotlin-BLE-Library").exists()) {
//    includeBuild("../Kotlin-BLE-Library")
//}
