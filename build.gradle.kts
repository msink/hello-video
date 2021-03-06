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
                "-Wl,-Bstatic",
                "-lstdc++",
                "-static",
                "-lSDL2",
                "-lavformat",
                "-lavcodec",
                "-lavutil",
                "-lswscale",
                "-lswresample",

                "-laom",
                "-ldav1d",
                "-lgsm",
                "-lmfx",
                "-lmodplug",
                "-lmp3lame",
                "-lopencore-amrnb",
                "-lopencore-amrwb",
                "-lspeex",
                "-lopus",
                "-lvorbis",
                "-lvorbisenc",
                "-ltheoraenc",
                "-ltheoradec",
                "-logg",
                "-lsoxr",
                "-lgomp",
                "-lwebpmux",
                "-lwebp",
                "-lvpx",
                "-l:xvidcore.a",

                "-l:libbluray.dll.a",
                "-l:libcelt0.dll.a",
                "-l:libgnutls.dll.a",
                "-l:libopenjp2.dll.a",
                "-l:librtmp.dll.a",
                "-l:libsrt.dll.a",
                "-l:libvulkan.dll.a",
                "-l:libx264.dll.a",
                "-l:libx265.dll.a",
                "-l:libxml2.dll.a",

                "-liconv",
                "-lbz2",
                "-llzma",
                "-lz",

                "-lbcrypt",
                "-lgdi32",
                "-limm32",
                "-lole32",
                "-loleaut32",
                "-lmfplat",
                "-lsetupapi",
                "-lssp",
                "-lstrmiids",
                "-lversion",
                "-lwinmm"
            )
        }
    }
}
