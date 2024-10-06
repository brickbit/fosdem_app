plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidxRoom)
    alias(libs.plugins.kotlinxSerialization)
    id("org.kodein.mock.mockmp") version "1.17.0"
}
apply(plugin= "com.mikepenz.aboutlibraries.plugin")

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
            implementation(libs.kotlinx.coroutines.core.v180)
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
            //viewModel
            implementation(libs.lifecycle.viewmodel.compose)
            //xml
            implementation(libs.xmlutil.core)
            implementation(libs.xmlutil.serialization)
            //room
            implementation(libs.androidx.room.runtime)
            implementation(libs.androidx.sqlite.bundled)
            //ksoup
            implementation(libs.ksoup.lite)
            implementation("co.touchlab:stately-common:2.0.5")

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
            //implementation(libs.androidx.lifecycle.viewmodel.compose)
        }
        iosMain.dependencies {
            //ktor
            implementation(libs.ktor.client.darwin)
        }
    }

    sourceSets.commonMain {
        kotlin.srcDir("build/generated/ksp/metadata")
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

dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspIosX64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}