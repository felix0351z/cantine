plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.0"
}

kotlin {
    // Shared-Module Dokumentation
    // https://kotlinlang.org/docs/multiplatform-share-on-platforms.html

    // In Android wird das SharedModule als Dependency direkt integriert
    android {
        // Kotlin-Kompilierung-support für Java 8
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    // In iOS wird das SharedModule als Framework kompiliert
    ios() {
        // Erstellt  vorkonfigurierte SourceSets für iOsArm64 und iOSX64
        binaries {
            framework {
                baseName = "shared" // Name der Framework-Datei, welche von dem Shared-Module kompiliert wird
            }
        }
    }
    iosSimulatorArm64() // SourceSet für den MacBook iOS-Visualizer

    val ktorVersion = "2.2.3"
    val multiplatformSettings = "1.0.0"
    val koin = "3.2.0"
    val kotlinxTime = "0.4.0"
    sourceSets {
        // Neue Source-Set Namensgebung in Kotlin 1.8.0
        // https://kotlinlang.org/docs/whatsnew18.html#kotlinsourceset-naming-schema

        // SourceSet für geteilten Code
        val commonMain by getting {
            dependencies {

                // Ktor
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                //Multiplatform Settings(local data store)

                implementation("com.russhwolf:multiplatform-settings-no-arg:$multiplatformSettings")
                implementation("com.russhwolf:multiplatform-settings-serialization:$multiplatformSettings")

                //Time utilities
                api("org.jetbrains.kotlinx:kotlinx-datetime:$kotlinxTime")

                // Dependency Injection with koin
                api("io.insert-koin:koin-core:$koin")
            }
        }

        // SourceSet für geteilten Test-Code
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
            }
        }

        // SourceSet für spezifischen Android-Code
        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
            }
        }

        // SourceSet für spezifische AndroidUnitTest
        // (IntegrationsTest nicht! -> Anderes SourceSet)
        val androidUnitTest by getting

        // SourceSet für iOS spezificher Code (ios64Main, iosX64Main, iosSimulator64Main)
        val iosMain by sourceSets.getting {
            dependencies {
                implementation("io.ktor:ktor-client-darwin:$ktorVersion")
            }
        }

        // SourceSet für iOS spezificher Test-Code (ios64Main, iosX64Main, iosSimulator64Main)
        val iosTest by sourceSets.getting


        // Integration des iOS-Simulators auf M1/M2 Macbooks zumm iosMain/iosTest SourceSet
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