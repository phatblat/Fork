package at.phatbl.fork

import org.gradle.api.Plugin
import org.gradle.api.Project

class ForkPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create("fork", ForkExtension::class.java)
    }
}
