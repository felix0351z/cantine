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
    val hikari_version = "5.0.1"
    val mysql_connector_version = "8.0.30"
    val sqlite_connector_version = "3.30.1"

    //Ktor Server Core Library mit Netty als Network Framework
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-host-common-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")

    //Session Authentifizierung
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-sessions-jvm:$ktor_version")

    implementation("io.ktor:ktor-server-status-pages-jvm:$ktor_version")

    //Serialization
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("com.charleskorn.kaml:kaml:$kaml_version")

    //Logback als Logger
    implementation("ch.qos.logback:logback-classic:$logback_version")

    //Exposed als Datenbank Library
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")
    //HikariCP als Verbindungsframework
    implementation("com.zaxxer:HikariCP:$hikari_version")
    //MySQL/MAriaDB/SQLite Treiber
    implementation("mysql:mysql-connector-java:$mysql_connector_version")
    implementation("org.xerial:sqlite-jdbc:$sqlite_connector_version")


    //JUnit als Test Library
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}