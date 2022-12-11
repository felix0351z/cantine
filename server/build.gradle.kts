val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project


plugins {
    application
    kotlin("jvm") version "1.7.10"
    id("io.ktor.plugin") version "2.1.2"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.7.10"
}

tasks.test {
    useJUnit()
    ignoreFailures = true
}

group = "de.felix0351"
version = "0.0.1"
application {
    mainClass.set("de.felix0351.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}


dependencies {
    val kaml_version = "0.49.0"
    val kmongo_version = "4.8.0"
    val koin_version = "3.2.2"

    //Ktor Server Core Library mit Netty als Network Framework
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-host-common-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")

    // Dependency Injection mit Koin
    implementation("io.insert-koin:koin-core:$koin_version")
    implementation("io.insert-koin:koin-ktor:$koin_version")
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")

    //Session Authentifizierung
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-sessions-jvm:$ktor_version")

    implementation("io.ktor:ktor-server-status-pages-jvm:$ktor_version")

    //Hashing with BCrypt
    //implementation("at.favre.lib:bcrypt:0.9.0")
    implementation("org.mindrot:jbcrypt:0.4")

    //Serialization
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("com.charleskorn.kaml:kaml:$kaml_version")

    //Logback als Logger
    implementation("ch.qos.logback:logback-classic:$logback_version")

    //MongoDB
    implementation("org.litote.kmongo:kmongo:$kmongo_version")



    //JUnit als Test Library
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}