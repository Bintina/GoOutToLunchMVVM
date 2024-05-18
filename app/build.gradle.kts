
buildscript {
    repositories {
        google()
        // Other repositories if needed
    }
    dependencies{
    classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.7")
    }

}
plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
    // SafeArgs plugin
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    //KSP plugin
    id("com.google.devtools.ksp")
}



android {
    namespace = "com.bintina.goouttolunchmvvm"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.bintina.goouttolunchmvvm"
        minSdk = 28
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
        viewBinding = true
        buildConfig = true
    }
    secrets {
        // Optionally specify a different file name containing your secrets.
        // The plugin defaults to "local.properties"
        propertiesFileName = "secrets.properties"

        // A properties file containing default secret values. This file can be
        // checked in version control.
        defaultPropertiesFileName = "local.defaults.properties"

        // Configure which keys should be ignored by the plugin by providing regular expressions.
        // "sdk.dir" is ignored by default.
        ignoreList.add("keyToIgnore") // Ignore the key "keyToIgnore"
        ignoreList.add("sdk.*")       // Ignore all keys matching the regexp "sdk.*"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.annotation:annotation:1.7.1")
    implementation("androidx.test:core-ktx:1.5.0")
    implementation("com.google.android.libraries.places:places:3.4.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.24")

    debugImplementation("androidx.fragment:fragment-testing:1.7.0")

    //Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.arch.core:core-testing:2.2.0")
    // Add Mockito dependency
    testImplementation("org.mockito:mockito-core:5.11.0")
    testImplementation ("io.mockk:mockk:1.13.10")

    // If you're using Kotlin, include the Kotlin test dependencies
    //testImplementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:2.7.7")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")

    // Navigation Component
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    // Kotlin
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    // Feature module Support
    implementation("androidx.navigation:navigation-dynamic-features-fragment:2.7.7")

    // Testing Navigation
    androidTestImplementation("androidx.navigation:navigation-testing:2.7.7")


    //Room
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    androidTestImplementation("androidx.room:room-testing:2.6.1")

    //ViewModel dependencies
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.7.0")

    //Fragments support
    //Java language implementation
    implementation("androidx.fragment:fragment:1.7.0")
    //Kotlin
    implementation("androidx.fragment:fragment-ktx:1.7.0")

    //Gson dependancy
    implementation("com.google.code.gson:gson:2.10.1")

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")
    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries
    // Firebase Authentication
    implementation("com.google.firebase:firebase-auth")
    // Firebase UI
    // Used in FirebaseUIActivity.
    implementation("com.firebaseui:firebase-ui-auth:8.0.2")
    // Import the BoM for the Firebase platform

    implementation("com.google.android.gms:play-services-auth:21.1.1")
    //implementation("com.google.android.libraries.identity.googleid:googleid:20.7.0")


    // Facebook Android SDK (only required for Facebook Login)
    // Used in FacebookLoginActivity.
    implementation("com.facebook.android:facebook-login:17.0.0")
    implementation("com.facebook.android:facebook-android-sdk:17.0.0")
    implementation("androidx.browser:browser:1.8.0")

    // Kotlin components
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.24")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

    //RecyclerView dependency
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    //Glide Image dependancy
    implementation("com.github.bumptech.glide:glide:4.16.0")
    kapt("com.github.bumptech.glide:compiler:4.16.0")
    implementation("com.github.bumptech.glide:okhttp3-integration:4.16.0")

    //Google Maps
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.maps.android:android-maps-utils:3.8.2")
    implementation("com.google.android.libraries.places:places:3.4.0")

    //Retrofit implementations
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    //Card View
    implementation("androidx.cardview:cardview:1.0.0")
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}

kotlin {
/*    android {
        kotlinOptions {
            jvmTarget = "18"
        }
    }*/
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(9))
    }
}

// Ensure the correct configuration for kapt tasks
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}