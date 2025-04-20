rootProject.name = "Dreath"

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

include(
    "api",

    "core:common",
    "core:engine",
    "core:render",

    "environment:client",
    "environment:server",

    "external:mod-example",
)
