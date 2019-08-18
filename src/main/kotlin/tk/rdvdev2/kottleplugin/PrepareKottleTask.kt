package tk.rdvdev2.kottleplugin

import net.minecraftforge.gradle.common.util.MinecraftExtension
import net.minecraftforge.gradle.common.util.RunConfig
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import javax.inject.Inject

open class PrepareKottleTask @Inject constructor() : DefaultTask() {

    val kottleConfiguration = project.configurations.getByName("kottle")

    init {
        for (run in project.getRuns()) {
            doLast {
                project.delete(project.fileTree(run.workingDirectory).matching{ it.include("Kottle*.jar") })
                project.copy {
                    it.from(kottleConfiguration.asFileTree)
                    it.include("**/*.jar")
                    it.into("${project.file(run.workingDirectory)}/mods")
                }
            }
        }
    }

    private fun Project.getRuns(): List<RunConfig> {
        val extension = extensions.getByName("minecraft") as MinecraftExtension
        return extension.runs.toList()
    }
}