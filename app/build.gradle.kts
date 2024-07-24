buildscript {
    repositories {
        google()
        mavenCentral()
        // Other repositories if needed
    }
    dependencies {
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.7.7")
    }

}
plugins {
    id("com.android.application")
    kotlin("android")
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")
    // SafeArgs plugin
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    //KSP plugin
    id("com.google.devtools.ksp")
    id("androidx.room")
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

        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21

    }

    kotlinOptions {
        jvmTarget = "21"
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
    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.annotation:annotation:1.8.0")
    implementation("androidx.test:core-ktx:1.5.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.24")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.10")
    implementation("androidx.test:rules:1.5.0")
    implementation("androidx.test.espresso:espresso-contrib:3.5.1")
    implementation("androidx.work:work-runtime:2.9.0")
    implementation("com.google.firebase:firebase-database:21.0.0")
    implementation("com.google.firebase:firebase-database-ktx:21.0.0")


    //Testing
    testImplementation("androidx.test:core:1.5.0")
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test.ext:junit:1.1.5")
    testImplementation("androidx.test:runner:1.5.2")
    testImplementation("androidx.test:rules:1.5.0")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
    debugImplementation("androidx.fragment:fragment-testing:1.7.1")
    // Add Mockito dependency
    testImplementation("org.mockito:mockito-core:5.11.0")
    testImplementation("io.mockk:mockk:1.13.10")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
    testImplementation("org.mockito:mockito-inline:4.0.0")
    //Coroutine testing
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
    // Testing Navigation
    androidTestImplementation("androidx.navigation:navigation-testing:2.7.7")
    // Espresso for UI testing (optional, if you need it)
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //testImplementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:2.7.7")

    // Navigation Component
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    // Kotlin
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    // Feature module Support
    implementation("androidx.navigation:navigation-dynamic-features-fragment:2.7.7")


    //Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    // kapt("android.arch.persistence.room:compiler:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    androidTestImplementation("androidx.room:room-testing:2.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.5.0")
    //Dagger2
    implementation("com.google.dagger:dagger:2.49")
    ksp("com.google.dagger:dagger-compiler:2.48.1")

    //ViewModel and LiveData dependencies
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.1")
    implementation("androidx.lifecycle:lifecycle-common-java8:2.8.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.1")

    //Fragments support
    //Java language implementation
    implementation("androidx.fragment:fragment:1.7.1")
    //Kotlin
    implementation("androidx.fragment:fragment-ktx:1.7.1")

    //Gson dependancy
    implementation("com.google.code.gson:gson:2.10.1")

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")
    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries
    // Firebase Authentication
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-auth-ktx")
    // Firebase UI
    // Used in FirebaseUIActivity.
    implementation("com.firebaseui:firebase-ui-auth:8.0.2")
    // Import the BoM for the Firebase platform

    // Add the dependencies for the App Check libraries
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-appcheck-playintegrity")

    //FCM
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-installations")
    implementation("com.google.firebase:firebase-installations-ktx")

    implementation("com.google.android.gms:play-services-auth:20.7.0")
    //implementation("com.google.android.libraries.identity.googleid:googleid:20.7.0")
    //credentials not supported. Recommended action: Use a different version of dependency 'androidx.credentials:credentials:1.5.0-alpha01',
    //      or set compileSdkPreview to "VanillaIceCream" in your build.gradle
    //      file if you intend to experiment with that preview SDK.
    //implementation("androidx.credentials:credentials:1.5.0-alpha01")


    //Login dependencies
    //Google login dependency
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.0")
    // Google Identity Services SDK (only required for Auth with Google)
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    // Facebook Android SDK (only required for Facebook Login)
    // Used in FacebookLoginActivity.
    implementation("com.facebook.android:facebook-android-sdk:17.0.0")
    implementation("androidx.browser:browser:1.8.0")
    implementation("com.facebook.android:facebook-login:17.0.0")

    // Kotlin components
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.24")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

    //RecyclerView dependency
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    //Glide Image dependancy
    implementation("com.github.bumptech.glide:glide:4.16.0")
    ksp("com.github.bumptech.glide:ksp:4.16.0")
    implementation("com.github.bumptech.glide:okhttp3-integration:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    //Google Maps
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.maps.android:android-maps-utils:3.8.2")
    implementation("com.google.android.libraries.places:places:3.4.0")


    //Retrofit implementations
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    //Card View
    implementation("androidx.cardview:cardview:1.0.0")

    //Json Conversion
    implementation("com.google.code.gson:gson:2.10.1")

    //WorkManager Dependencies......................................................................
    // (Java only)
    implementation("androidx.work:work-runtime:2.9.0")
    // Kotlin + coroutines
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    // optional - RxJava2 support
    implementation("androidx.work:work-rxjava2:2.9.0")
    // optional - GCMNetworkManager support
    implementation("androidx.work:work-gcm:2.9.0")
    // optional - Test helpers
    implementation("androidx.work:work-testing:2.9.0")
    androidTestImplementation("androidx.work:work-testing:2.9.0")
    // Test helpers for LiveData
    androidTestImplementation("android.arch.core:core-testing:1.1.1")
    // optional - Multiprocess support
    implementation("androidx.work:work-multiprocess:2.9.0")
}

kotlin {
/*    android {
        kotlinOptions {
            jvmTarget = "9"
        }
    }*/
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

// Ensure the correct configuration for kapt tasks
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "21"
    }
}