plugins {
    id("itinerary.android.library")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.itinerary.core.network"
}

dependencies {
    implementation(project(":core:common"))
    
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.koin.core)
}
