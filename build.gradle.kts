// SPDX-License-Identifier: MIT

import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform") version "1.5.10"
}

repositories {
    mavenCentral()
}

val msys2 = File(System.getenv("MSYS2_ROOT") ?: "C:/msys64/")

kotlin {
    mingwX64("mingw64")
    mingwX86("mingw32")

    targets.withType<KotlinNativeTarget> {
        sourceSets["${targetName}Main"].apply {
            kotlin.srcDir("src/nativeMain/kotlin")
        }
        compilations["main"].apply {
            enableEndorsedLibs = true
            cinterops.create("ffmpeg") {
                includeDirs(msys2.resolve("${targetName}/include"))
            }
            cinterops.create("sdl") {
                includeDirs(msys2.resolve("${targetName}/include/SDL2"))
            }
        }
        binaries.executable {
            entryPoint = "sample.videoplayer.main"
            linkerOpts(
                "-L${msys2.resolve("${targetName}/lib")}",
                "-lavutil",
                "-lavformat",
                "-lavcodec",
                "-lswscale",
                "-lswresample",
                "-lSDL2"
            )
        }
    }
}
