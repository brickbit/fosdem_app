plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    kotlin("plugin.serialization") version "1.9.0"
    id("io.realm.kotlin") version "1.11.0"
    id("org.kodein.mock.mockmp") version "1.17.0"
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
            implementation(libs.ktor.client.logging)
            //okio for files
            implementation(libs.okio)
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
            implementation(libs.kotlinx.coroutines.test)
            //Test for viewModel
            implementation(libs.turbine)
            //datetime
            implementation(libs.kotlinx.datetime)
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

mockmp {
    usesHelper = true
    installWorkaround()
}

android {
    namespace = "com.rgr.fosdem"
    compileSdk = 34
    defaultConfig {
        minSdk = 27
    }
}
