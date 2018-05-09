package at.phatbl.fork

import org.gradle.testfixtures.ProjectBuilder
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertTrue

object ForkPluginSpek: Spek({
    given("a plugin") {
        val plugin = ForkPlugin()
        val project = ProjectBuilder.builder().build()
        on("apply") {
            plugin.apply(project)
            it("creates an extension") {
                val extension = project.extensions.getByName("fork")
                assertTrue(extension is ForkExtension)
            }
        }
    }
})
