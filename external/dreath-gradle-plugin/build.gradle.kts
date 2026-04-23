plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
}

group = "com.marcpg"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

gradlePlugin {
    plugins {
        create("dreathMod") {
            id = "com.marcpg.dreath-mod"
            implementationClass = "com.marcpg.gradle.DreathModPlugin"
        }
    }
}

tasks.register<Copy>("syncGradleLibsFile") {
    from(rootProject.file("gradle/libs.versions.toml"))
    into(layout.projectDirectory.dir("src/main/resources"))
}

tasks {
    processResources {
        dependsOn("syncGradleLibsFile")
    }
}
