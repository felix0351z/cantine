plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "de.juliando.app.android"
    compileSdk = 33
    defaultConfig {
        applicationId = "de.juliando.app.android"
        minSdk = 27
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.0"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // Shared module als Abhänigkeiten
    implementation(project(":shared"))

    val navVersion = "2.5.3"
    val composeVersion = "1.3.1"

    val composeBom = platform("androidx.compose:compose-bom:2023.01.00")
    implementation(composeBom)

    // Jetpack Compose
    implementation("androidx.activity:activity-compose:1.6.1")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.compose.foundation:foundation:$composeVersion")

    // Glide for image loading
    implementation("com.github.bumptech.glide:compose:1.0.0-alpha.1")


    // Compose Navigation
    implementation("androidx.navigation:navigation-compose:$navVersion")

    // Use Material UI 3
    implementation("androidx.compose.material3:material3:1.0.1")
    implementation("androidx.compose.material3:material3-window-size-class:1.0.1")

    // More material icons
    implementation("androidx.compose.material:material-icons-extended")




}