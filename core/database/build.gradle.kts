plugins {
    id("itinerary.android.library")
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.itinerary.core.database"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:common"))
    
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.koin.core)
}
