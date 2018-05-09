package at.phatbl.fork

import org.gradle.testfixtures.ProjectBuilder
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*

object ForkPluginSpek: Spek({
    given("a plugin") {
        val plugin = ForkPlugin()
        val project = ProjectBuilder.builder().build()
        on("apply") {
            plugin.apply(project)
            it("should create tasks")
            }
        }
    }
})
