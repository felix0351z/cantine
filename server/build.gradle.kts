val ktor_version: String by project
val kotlin_version: String by project

group = "de.felix0351"
version = "0.0.1"

plugins {
    application
    kotlin("jvm") version "1.8.0"
    id("io.ktor.plugin") version "2.2.2"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.0"
}

tasks.test {
    // Use Junit5 for tests
    useJUnitPlatform()
}

application {
    mainClass.set("de.felix0351.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

ktor {
    fatJar {
        archiveFileName.set("cantine-server.jar")
    }
}

repositories {
    // All dependencies are available from maven central
    mavenCentral()
}


dependencies {

    //Ktor Server Core Library with Netty as Engine
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-host-common-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-forwarded-header:$ktor_version")

    // Dependency Injection with Koin
    val koin_version = "3.2.2"
    implementation("io.insert-koin:koin-core:$koin_version")
    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")

    //Session Authentication and Authorization
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-sessions-jvm:$ktor_version")

    // Status pages for exceptions and errors
    implementation("io.ktor:ktor-server-status-pages-jvm:$ktor_version")

    //Password Hashing with BCrypt
    implementation("org.mindrot:jbcrypt:0.4")

    //Serialization with KotlinX
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")

    // KAML for yaml config serialization
    val kaml_version = "0.49.0"
    implementation("com.charleskorn.kaml:kaml:$kaml_version")

    //Logback as logger
    val logback_version = "1.4.3"
    val janino_version = "3.1.9"
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.codehaus.janino:janino:$janino_version")

    //MongoDB as database
    val kmongo_version = "4.8.0"
    implementation("org.litote.kmongo:kmongo-coroutine:$kmongo_version")

}

dependencies {
    implementation("io.ktor:ktor-server-forwarded-header-jvm:2.2.2")//Test dependencies for the ktor server
    testImplementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")

    //JUnit5 als Test Library
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:$kotlin_version")
}