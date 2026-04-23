dependencies {
    api(kotlin("reflect"))

    projectApi(":api")
    projectApi(":external:dreathful-protocol")
    api(libs.clikt)
    api(libs.jline)
}

tasks {
    build {
        dependsOn(processResources)
    }
}
