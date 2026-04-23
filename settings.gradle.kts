rootProject.name = "Dreath"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

include(
    "api",

    "core:common",
    "core:engine",
    "core:render",

    "environment:client",
    "environment:server",

    "external:cotton",
    "external:dreathful-protocol",
    "external:launcher",
    "external:mod-example",
)
