plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.compose.compiler)
    kotlin("plugin.serialization") version "1.9.0"
    id("com.google.firebase.crashlytics")
    id("com.google.gms.google-services")

}
apply(plugin= "com.mikepenz.aboutlibraries.plugin")


android {
    namespace = "com.rgr.fosdem.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.rgr.fosdem.android"
        minSdk = 27
        targetSdk = 34
        versionCode = 20240130
        versionName = "0.5.1"
    }
    buildFeatures {
        compose = true
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
            buildConfigField("String", "location", "\"https://fosdem.org/2024/practical/transportation/\"")
            buildConfigField("String", "scheduleSaturday", "\"https://fosdem.org/2024/schedule/day/saturday/\"")
            buildConfigField("String", "scheduleSunday", "\"https://fosdem.org/2024/schedule/day/sunday/\"")
        }
        getByName("release") {
            isMinifyEnabled = false
            buildConfigField("String[]", "languages", "{\"English\",\"Français\",\"Español\"}")
            buildConfigField("String", "license", "\"https://www.apache.org/licenses/LICENSE-2.0.txt\"")
            buildConfigField("String", "linkedIn", "\"https://www.linkedin.com/in/rgr92\"")
            buildConfigField("String", "gitHub", "\"https://github.com/brickbit/fosdem_app\"")
            buildConfigField("String", "location", "\"https://fosdem.org/2024/practical/transportation/\"")
            buildConfigField("String", "scheduleSaturday", "\"https://fosdem.org/2024/schedule/day/saturday/\"")
            buildConfigField("String", "scheduleSunday", "\"https://fosdem.org/2024/schedule/day/sunday/\"")
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
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("io.coil-kt:coil-video:2.4.0")
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
    //lottie
    implementation(libs.lottie.compose)
    //material
    implementation(libs.androidx.material)
    //datetime
    implementation(libs.kotlinx.datetime)
    //room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.sqlite.bundled)

    implementation("androidx.media3:media3-exoplayer:1.2.0")
    implementation("androidx.media3:media3-ui:1.2.0")

}