plugins {
    id("itinerary.android.library")
}

android {
    namespace = "com.itinerary.core.common"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
}
