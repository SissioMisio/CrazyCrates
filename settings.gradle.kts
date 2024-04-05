enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        maven("https://repo.papermc.io/repository/maven-public/")

        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "CrazyCrates"

include(":api")
include(":paper")

//include(":vital:test-plugin")
include(":vital:vital-paper")
include(":vital:buildSrc")
include(":vital")