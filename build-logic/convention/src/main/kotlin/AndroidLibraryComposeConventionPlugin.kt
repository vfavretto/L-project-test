import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.android.library")
            pluginManager.apply("org.jetbrains.kotlin.plugin.compose")

            val extension = extensions.getByType<LibraryExtension>()
            extension.apply {
                buildFeatures {
                    compose = true
                }
                
                composeOptions {
                    kotlinCompilerExtensionVersion = "1.10.2"
                }
            }

            dependencies {
                val bom = project.dependencies.platform(libs.findLibrary("androidx.compose.bom").get())
                add("implementation", bom)
                add("implementation", libs.findLibrary("androidx.compose.ui").get())
                add("implementation", libs.findLibrary("androidx.compose.ui.graphics").get())
                add("implementation", libs.findLibrary("androidx.compose.ui.tooling.preview").get())
                add("implementation", libs.findLibrary("androidx.compose.material3").get())
                add("debugImplementation", libs.findLibrary("androidx.compose.ui.tooling").get())
            }
        }
    }
}

private val Project.libs
    get() = extensions.getByType<org.gradle.accessors.dm.LibrariesForLibs>()
