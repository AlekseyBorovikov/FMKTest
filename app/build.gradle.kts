plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.fmk"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.fmk"
        minSdk = 24
        targetSdk = 34
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

    viewBinding {
        enable = true
    }
}

dependencies {

    val kotlinVersion = "1.12.0"
    val coroutinesVersion = "1.6.4"
    // support libs
    val appcompatVersion = "1.6.1"
    val materialVersion = "1.11.0"
    val timberVersion = "5.0.1"
    val splashscreenVersion = "1.0.1"
    val glideVersion = "4.15.1"
    val activity_version = "1.8.2"
    val fragment_version = "1.6.2"
    val nav_version = "2.7.7"
    // hilt
    val hiltVersion = "2.50"
    // network
    val retrofitVersion = "2.9.0"
    val okhttpVersion = "5.0.0-alpha.5"

    implementation("androidx.core:core-ktx:$kotlinVersion")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    // support libs
    implementation("androidx.appcompat:appcompat:$appcompatVersion")
    implementation("com.google.android.material:material:$materialVersion")
    implementation("com.jakewharton.timber:timber:$timberVersion")
    implementation("androidx.core:core-splashscreen:$splashscreenVersion")
    implementation("com.github.bumptech.glide:glide:$glideVersion")

    implementation("androidx.activity:activity-ktx:$activity_version")
    implementation("androidx.fragment:fragment-ktx:$fragment_version")
    // network libs
    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion")
    implementation("com.squareup.okhttp3:logging-interceptor:$okhttpVersion")
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    // dagger hilt
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-compiler:$hiltVersion")

    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")


    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}