plugins {
    id("itinerary.android.feature")
}

android {
    namespace = "com.itinerary.feature.trip"
}

dependencies {
    implementation(project(":feature:map"))
    implementation(project(":feature:destinations"))
    implementation(project(":feature:schedule"))
}
