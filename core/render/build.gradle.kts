val nativeFileName = "librenderer"
val nativeDir = file("native")
val buildNativeDir = file("build/native")
val resourceNativeDir = file("src/main/resources")

val lwjglNatives = "natives-linux"

dependencies {
    projectApi(":core:common")

    implementation(libs.joml)
    implementation(libs.joml.primitives)

    implementation(platform(libs.lwjgl))

    implementation("org.lwjgl:lwjgl")
    implementation("org.lwjgl:lwjgl-assimp")
    implementation("org.lwjgl:lwjgl-glfw")
    implementation("org.lwjgl:lwjgl-jemalloc")
    implementation("org.lwjgl:lwjgl-nfd")
    implementation("org.lwjgl:lwjgl-opengl")
    implementation("org.lwjgl:lwjgl-shaderc")
    implementation("org.lwjgl:lwjgl-stb")
    runtimeOnly("org.lwjgl", "lwjgl", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-assimp", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-glfw", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-jemalloc", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-nfd", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-opengl", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-shaderc", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-stb", classifier = lwjglNatives)
}

tasks {
    processResources {
        exclude("shader_glsl/*")
    }
    register<Exec>("buildNative") {
        // Disable cache for this task:
        outputs.upToDateWhen { false }

        group = "build"
        description = "Build native C++ library with CMake"
        workingDir = nativeDir

        val buildType = if (project.hasProperty("release")) "Release" else "Debug"

        commandLine("cmake", "-B", buildNativeDir.absolutePath, "-DCMAKE_BUILD_TYPE=$buildType", nativeDir.absolutePath)
        doLast {
            providers.exec {
                workingDir = buildNativeDir
                commandLine("cmake", "--build", ".")
            }

            val libFiles = buildNativeDir.listFiles { a -> a.nameWithoutExtension == "librenderer" }
            libFiles.forEach { it.copyTo(resourceNativeDir.resolve(it.name), overwrite = true) }
        }
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        dependsOn("buildNative")
    }
}
