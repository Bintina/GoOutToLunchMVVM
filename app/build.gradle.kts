
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

    implementation("androidx.core:core-ktx:1.13.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.annotation:annotation:1.7.1")
    implementation("androidx.test:core-ktx:1.5.0")
    implementation("com.google.android.libraries.places:places:3.4.0")
    debugImplementation("androidx.fragment:fragment-testing:1.6.2")

    //Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.arch.core:core-testing:2.2.0")
    // Add Mockito dependency
    testImplementation("org.mockito:mockito-core:5.11.0")
    // If you're using Kotlin, include the Kotlin test dependencies
    testImplementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:2.7.7")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

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
    implementation("androidx.fragment:fragment:1.6.2")
    //Kotlin
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    //Gson dependancy
    implementation("com.google.code.gson:gson:2.10.1")

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
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
    implementation(platform("com.google.firebase:firebase-bom:32.8.1"))
    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-auth")
    // Also add the dependency for the Google Play services library and specify its version
    implementation("com.google.android.gms:play-services-auth:21.0.0")
    //implementation("com.google.android.libraries.identity.googleid:googleid:20.7.0")

    // Google Identity Services SDK (only required for Auth with Google)
    implementation("com.google.android.gms:play-services-auth:21.0.0")

    // Facebook Android SDK (only required for Facebook Login)
    // Used in FacebookLoginActivity.
    implementation("com.facebook.android:facebook-login:16.3.0")
    implementation("com.facebook.android:facebook-android-sdk:16.3.0")
    implementation("androidx.browser:browser:1.8.0")

    // Kotlin components
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.20")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    //RecyclerView dependency
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    //Glide Image dependancy
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
    implementation("com.github.bumptech.glide:okhttp3-integration:4.11.0")

    //Google Maps
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.maps.android:android-maps-utils:3.8.0")
    implementation("com.google.android.libraries.places:places:2.5.0")

    //Retrofit implementations
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}

kotlin {
    android {
        kotlinOptions {
            jvmTarget = "1.8"
        }
    }
}

// Ensure the correct configuration for kapt tasks
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}