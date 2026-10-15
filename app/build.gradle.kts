plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.jozeluindev.firebaseStorage"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.jozeluindev.firebaseStorage"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        compose=true
    }
    composeOptions{
        kotlinCompilerExtensionVersion="1.4.0"
    }
}

dependencies {
    // 🌟 Firebase
    implementation(platform(libs.google.firebase.bom))
    implementation(libs.google.firebase.auth.ktx)
    implementation(libs.firebase.storage)


    // 🌟 Login & Terceros (Así traduce Gradle los alias largos con puntos en el código)
    implementation(libs.com.google.android.gms.play.services.auth)
    implementation(libs.com.facebook.android.facebook.login)
    implementation(libs.io.github.chaosleung.pinview)

    // 🌟 Dagger Hilt
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)

    // 🌟 Core & UI clásica
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.com.google.android.material)
    implementation(libs.androidx.constraintlayout)

    // 🌟 Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)

    //Compose
    implementation (platform(libs.androidx.compose.bom))
    implementation (libs.androidx.compose.ui)
    implementation (libs.androidx.compose.material)
    implementation (libs.androidx.compose.runtime)
    implementation (libs.androidx.compose.ui.graphics)
    implementation (libs.androidx.compose.ui.tooling.preview)
    implementation (libs.androidx.activity.compose)
    implementation(libs.coil.compose)

}
