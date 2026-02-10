plugins {
    id("itinerary.android.library")
    id("itinerary.android.library.compose")
}

android {
    namespace = "com.itinerary.core.designsystem"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.coil.compose)
}
