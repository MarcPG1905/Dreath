rootProject.name = "Dreath"

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

include("game", "api", "mod-example")
