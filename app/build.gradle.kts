plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.adityat.loancal_emicalculator"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.adityat.loancal_emicalculator"
        minSdk = 24
        targetSdk = 34
        versionCode = 5
        versionName = "5.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1" // Latest stable version
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Versions
    val nav_version = "2.7.5" // Latest navigation version
    val compose_version = "1.6.0" // Latest stable Compose version
    val room_version = "2.6.1" // Latest Room version
    val core_ktx_version = "1.13.1"
    val lifecycle_version = "2.8.4"
    val activity_compose_version = "1.9.1"
    val compose_bom_version = "2023.08.00"
    val junit_version = "4.13.2"
    val android_test_ext_junit_version = "1.2.1"
    val espresso_core_version = "3.6.1"
    val play_services_ads_version = "23.2.0" // Latest Play Services Ads version

    // Compose
    implementation("androidx.compose.ui:ui:$compose_version")
    implementation("androidx.compose.material:material:$compose_version")
    implementation("androidx.compose.ui:ui-tooling-preview:$compose_version")
    implementation(platform("androidx.compose:compose-bom:$compose_bom_version"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // Navigation
    implementation("androidx.navigation:navigation-compose:$nav_version")

    // Core
    implementation("androidx.core:core-ktx:$core_ktx_version")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycle_version")
    implementation("androidx.activity:activity-compose:$activity_compose_version")

    // Room
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    kapt("androidx.room:room-compiler:$room_version")

    // Google Play Services
    implementation("com.google.android.gms:play-services-ads:$play_services_ads_version")

    // Testing
    testImplementation("junit:junit:$junit_version")
    androidTestImplementation("androidx.test.ext:junit:$android_test_ext_junit_version")
    androidTestImplementation("androidx.test.espresso:espresso-core:$espresso_core_version")
    androidTestImplementation(platform("androidx.compose:compose-bom:$compose_bom_version"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation ("com.google.accompanist:accompanist-navigation-animation:0.28.0")
}
