plugins {
    alias(libs.plugins.android.application)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

secrets {
    // Tells the plugin where to read the key from
    propertiesFileName = "local.properties"
}

android {
    namespace = "com.example.lostandfound"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.lostandfound"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.core:core:1.13.1")

    // Google Maps
    implementation("com.google.android.gms:play-services-maps:19.0.0")

    // Google Places
    implementation("com.google.android.libraries.places:places:3.5.0")

    // GPS location
    implementation("com.google.android.gms:play-services-location:21.3.0")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
}