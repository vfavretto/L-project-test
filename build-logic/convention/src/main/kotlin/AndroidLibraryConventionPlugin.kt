import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<LibraryExtension> {
                compileSdk = 35

                defaultConfig {
                    minSdk = 26
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }

                kotlinOptions {
                    jvmTarget = "17"
                }

                buildFeatures {
                    buildConfig = true
                }
            }

            dependencies {
                add("implementation", kotlin("stdlib"))
            }
        }
    }
}

private fun LibraryExtension.kotlinOptions(block: org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions.() -> Unit) {
    (this as org.gradle.api.plugins.ExtensionAware)
        .extensions
        .configure("kotlinOptions", block)
}
