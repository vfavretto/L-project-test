plugins {
    id("itinerary.android.library")
}

android {
    namespace = "com.itinerary.core.data"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:database"))
    implementation(project(":core:network"))
    implementation(project(":core:common"))
    
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.koin.core)
}
