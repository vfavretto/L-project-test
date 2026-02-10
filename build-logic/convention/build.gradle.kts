plugins {
    `kotlin-dsl`
}

group = "com.itinerary.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly("com.android.tools.build:gradle:8.7.3")
    compileOnly("org.jetbrains.kotlin:kotlin-gradle-plugin:2.1.0")
    compileOnly("org.jetbrains.kotlin:compose-compiler-gradle-plugin:2.1.0")
    compileOnly("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:2.1.0-1.0.29")
}

gradlePlugin {
    plugins {
        register("androidLibrary") {
            id = "itinerary.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "itinerary.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidFeature") {
            id = "itinerary.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }
    }
}
