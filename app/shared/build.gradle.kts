plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.0"
}

kotlin {
    // Shared-Module documentation
    // https://kotlinlang.org/docs/multiplatform-share-on-platforms.html

    // in android, the shared module will directly applied as gradle dependency
    android {
        // kotlin compilation support for java 8
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    // in ios, the shared module will integrated(and pre-compiled) as ios framework
    ios() {
        // creates pre configured source set configurations for iOSArm64 and iOSX64
        binaries {
            framework {
                baseName = "shared" // name of the framework file, which will be created from the shared module
            }
        }
    }
    iosSimulatorArm64() // SourceSet configuration for the m1/m2 ios simulator

    val ktorVersion = "2.2.3"
    val multiplatformSettings = "1.0.0"
    val koin = "3.2.0"
    val kotlinxTime = "0.4.0"
    sourceSets {
        // New source-set naming in Kotlin 1.8.0
        // https://kotlinlang.org/docs/whatsnew18.html#kotlinsourceset-naming-schema

        // Source sets for shared code
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4") // Kotlin coroutines for asynchronous features
                implementation("io.ktor:ktor-client-core:$ktorVersion") // Ktor as network client
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion") // Ktor plugin for serialization, uses the kotlinx serialization api

                implementation("com.russhwolf:multiplatform-settings-no-arg:$multiplatformSettings")  //Multiplatform Settings(local data store)
                implementation("com.russhwolf:multiplatform-settings-serialization:$multiplatformSettings") // Direct serialization features for the settings plugin

                api("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxTime") // Time utilities
                api("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion") // Serialization for objects, API needed for proguard configuration in android
                api("io.insert-koin:koin-core:$koin") // Dependency Injection with koin
            }
        }

        // sourceSet for shared test code
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
            }
        }

        // sourceSet für specific android code
        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
            }
        }

        // sourceSet für specific android unit tests
        // (No integration tests!)
        val androidUnitTest by getting

        // sourceSet for specific ios code (ios64Main, iosX64Main, iosSimulator64Main)
        val iosMain by sourceSets.getting {
            dependencies {
                implementation("io.ktor:ktor-client-darwin:$ktorVersion")
            }
        }

        // sourceSet for specific ios test code (ios64Main, iosX64Main, iosSimulator64Main)
        val iosTest by sourceSets.getting


        // integration for the ios-Simulator on M1/M2 processors for ios source set testing
        val iosSimulatorArm64Main by sourceSets.getting
        val iosSimulatorArm64Test by sourceSets.getting
        iosSimulatorArm64Main.dependsOn(iosMain)
        iosSimulatorArm64Test.dependsOn(iosTest)

    }
}

android {
    namespace = "de.juliando.app"
    compileSdk = 33
    defaultConfig {
        minSdk = 27
        targetSdk = 33
    }
}