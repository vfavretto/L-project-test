plugins {
    id("itinerary.android.library")
}

android {
    namespace = "com.itinerary.core.domain"
}

dependencies {
    implementation(project(":core:common"))
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.koin.core)
}
