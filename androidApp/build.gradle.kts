plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    kotlin("plugin.serialization") version "1.9.0"
    id("com.google.firebase.crashlytics")

}
apply(plugin= "com.mikepenz.aboutlibraries.plugin")


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
            buildConfigField("String", "license", "\"https://www.apache.org/licenses/LICENSE-2.0.txt\"")
            buildConfigField("String", "linkedIn", "\"https://www.linkedin.com/in/rgr92\"")
            buildConfigField("String", "gitHub", "\"https://github.com/brickbit/fosdem_app\"")

        }
        getByName("release") {
            isMinifyEnabled = false
            buildConfigField("String[]", "languages", "{\"English\",\"Français\",\"Español\"}")
            buildConfigField("String", "license", "\"https://www.apache.org/licenses/LICENSE-2.0.txt\"")
            buildConfigField("String", "linkedIn", "\"https://www.linkedin.com/in/rgr92\"")
            buildConfigField("String", "gitHub", "\"https://github.com/brickbit/fosdem_app\"")
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
    //chrome custom tab
    implementation(libs.androidx.browser)
    //splash
    implementation(libs.androidx.core.splashscreen)
    //licenses
    implementation(libs.aboutlibraries.compose)
    //serialization
    implementation(libs.kotlinx.serialization.json)
    //firebase
    implementation(platform("com.google.firebase:firebase-bom:32.1.0"))
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
}