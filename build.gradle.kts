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

                "-l:libaom.dll.a",
                "-l:libbluray.dll.a",
                "-l:libbz2.dll.a",
                "-l:libcelt0.dll.a",
                "-l:libdav1d.dll.a",
                "-l:libgnutls.dll.a",
                "-l:libgsm.dll.a",
                "-l:libiconv.dll.a",
                "-l:liblzma.dll.a",
                "-l:libmfx.dll.a",
                "-l:libmp3lame.dll.a",
                "-l:libmodplug.dll.a",
                "-l:libopencore-amrnb.dll.a",
                "-l:libopencore-amrwb.dll.a",
                "-l:libopenjp2.dll.a",
                "-l:libopus.dll.a",
                "-l:librtmp.dll.a",
                "-l:libsoxr.dll.a",
                "-l:libspeex.dll.a",
                "-l:libsrt.dll.a",
                "-l:libtheoraenc.dll.a",
                "-l:libtheoradec.dll.a",
                "-l:libvorbis.dll.a",
                "-l:libvorbisenc.dll.a",
                "-l:libvpx.dll.a",
                "-l:libvulkan.dll.a",
                "-l:libwebp.dll.a",
                "-l:libwebpmux.dll.a",
                "-l:libx264.dll.a",
                "-l:libx265.dll.a",
                "-l:libxml2.dll.a",
                "-l:xvidcore.dll.a",
                "-l:libz.dll.a",

                "-lbcrypt",
                "-lgdi32",
                "-limm32",
                "-lole32",
                "-loleaut32",
                "-lmfplat",
                "-lsetupapi",
                "-lstrmiids",
                "-lversion",
                "-lwinmm"
            )
        }
    }
}
