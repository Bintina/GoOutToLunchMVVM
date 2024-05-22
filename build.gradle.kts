// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("com.android.library") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false
    // Add the dependency for the Google services Gradle plugin
    id("com.google.gms.google-services") version "4.4.0" apply false
    //SafeArgs
    id ("androidx.navigation.safeargs.kotlin") version "2.7.7" apply false
    //Maps secrets plugin
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
    //KSP plugin
    id("com.google.devtools.ksp") version "1.9.24-1.0.20" apply false
    id("androidx.room") version "2.6.0" apply false
}

buildscript{

    repositories{
        mavenCentral()
        google()
        }


    dependencies {
        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.20")
        //classpath("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.24")
    }

}