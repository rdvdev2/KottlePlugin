package tk.rdvdev2.kottleplugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ModuleDependency

class KottlePlugin: Plugin<Project> {

    override fun apply(project: Project) {
        val kottleConfiguration = project.configurations.create("kottle")

        project.configurations.getByName("compile").extendsFrom(kottleConfiguration)

        val kottleTask = project.tasks.register("prepareKottle", PrepareKottleTask::class.java)

        project.beforeEvaluate { project ->

            project.configurations.forEach { configuration ->

                configuration.dependencies.forEach { dependency ->
                    if (dependency.group.equals("net.minecraftforge.gradle") && dependency.name.equals("ForgeGradle")) {
                        (dependency as ModuleDependency).exclude(mapOf("group" to "trove", "module" to "trove"))
                    }
                }
            }
        }

        project.afterEvaluate { project ->
            project.tasks.getByName("prepareRuns").dependsOn(kottleTask)
        }
    }
}