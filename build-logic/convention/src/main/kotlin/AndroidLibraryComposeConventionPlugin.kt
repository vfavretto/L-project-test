import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
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
                    kotlinCompilerExtensionVersion = "1.5.15"
                }
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            
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
