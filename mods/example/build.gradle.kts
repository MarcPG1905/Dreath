plugins {
    kotlin("jvm")

    id("com.marcpg.dreath-mod")
}

version = "1.0.0"

kotlin {
    jvmToolchain(25)
}

repositories {
    mavenLocal()
    mavenCentral()
}
