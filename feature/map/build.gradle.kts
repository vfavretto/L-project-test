plugins {
    id("itinerary.android.feature")
}

android {
    namespace = "com.itinerary.feature.map"
}

dependencies {
    implementation(libs.maps.compose)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    implementation(libs.accompanist.permissions)
}
