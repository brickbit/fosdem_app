plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    kotlin("plugin.serialization") version "1.9.0"
    id("io.realm.kotlin") version "1.11.0"
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            //coroutines
            implementation(libs.kotlinx.coroutines.core)
            //serialization
            implementation(libs.kotlinx.serialization.json)
            //ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.cio)
            implementation("io.ktor:ktor-client-logging:2.3.7")

            //datastore
            implementation(libs.androidx.data.store.core)
            //koin
            implementation(libs.koin.core.v320)
            //datetime
            implementation(libs.kotlinx.datetime)
            //realm
            implementation(libs.library.base)

        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        androidMain.dependencies {
            //viewModel
            implementation(libs.androidx.lifecycle.viewmodel.compose)
        }
        iosMain.dependencies {
            //ktor
            implementation(libs.ktor.client.darwin)
        }
    }
}

android {
    namespace = "com.rgr.fosdem"
    compileSdk = 34
    defaultConfig {
        minSdk = 27
    }
}
