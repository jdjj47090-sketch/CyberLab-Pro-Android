
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.mahdi.cyberlab"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.mahdi.cyberlab"
        minSdk = 26
        targetSdk = 28
        versionCode = 1
        versionName = "1.0.0"
    }
}
