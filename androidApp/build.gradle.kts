plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
}

android {
    namespace = "com.snap.fosdem.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.snap.fosdem.android"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("debug") {
            buildConfigField("String[]", "languages", "{\"English\",\"Français\",\"Español\"}")
        }
        getByName("release") {
            isMinifyEnabled = false
            buildConfigField("String[]", "languages", "{\"English\",\"Français\",\"Español\"}")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.shared)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.androidx.activity.compose)
    debugImplementation(libs.compose.ui.tooling)
    //viewModel
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    //koin
    implementation(libs.koin.androidx.compose)
    //navigation
    implementation(libs.androidx.navigation.compose)
    //fonts
    implementation(libs.androidx.ui.text.google.fonts)
    //datastore
    implementation(libs.androidx.data.store.core)
    //coil
    implementation(libs.coil.compose)
    //permissions
    implementation(libs.accompanist.permissions)

}