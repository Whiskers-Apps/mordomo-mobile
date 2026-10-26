plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")

    kotlin("plugin.serialization") version "2.4.21"
}

android {
    namespace = "org.whiskersapps.mordomo.mobile"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "org.whiskersapps.mordomo.mobile"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "0.15.0"
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

        getByName("debug") {
            applicationIdSuffix = ".debug"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.window)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)


    // Data Store
    implementation(libs.androidx.datastore.preferences)

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    implementation(libs.kotlinx.coroutines.core)

    // Routes
    implementation(libs.androidx.navigation.compose)

    // Coil for async image loading
    implementation(libs.coil.compose)

    // Sniffer (Fuzzy String Matching)
    implementation(libs.sniffer.kt)

    // Fingerprint
    implementation(libs.androidx.biometric)

    // Koin
    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.navigation)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.androidx.compose.navigation)

    // Droid Icons (Icon Packs)
    implementation(libs.droid.icons)

    // Room DB
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // MonoCode Material
    implementation(libs.compose.material)

    // Layout
    implementation(libs.layout.scaffold)

    // Slide to Unlock (Delete Sliders)
    implementation(libs.compose.slidetounlock)

    //JSON
    implementation(libs.kotlinx.serialization.json)

    // Coil (Images)
    implementation(libs.coil3.coil.compose)

    implementation("com.github.topjohnwu.libsu:core:6.0.0")
}