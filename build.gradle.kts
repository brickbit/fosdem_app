plugins {
    //trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.androidApplication).apply(false)
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.kotlinAndroid).apply(false)
    alias(libs.plugins.kotlinMultiplatform).apply(false)
    id("com.mikepenz.aboutlibraries.plugin") version "10.10.0"
    id("com.google.gms.google-services") version "4.4.0" apply false
}
buildscript {
    dependencies {
        classpath(libs.firebase.crashlytics.gradle)
    }
}
