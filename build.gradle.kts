plugins {
    //trick: for the same plugin versions in all sub-modules
    alias(libs.plugins.androidApplication).apply(false)
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.kotlinAndroid).apply(false)
    alias(libs.plugins.kotlinMultiplatform).apply(false)
    id("com.mikepenz.aboutlibraries.plugin") version "10.10.0"
}
buildscript {
    dependencies {
        classpath(libs.firebase.crashlytics.gradle)
    }
}
