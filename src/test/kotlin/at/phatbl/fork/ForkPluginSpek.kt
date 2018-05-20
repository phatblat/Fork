package at.phatbl.fork

import at.phatbl.fork.model.GitHubRemote
import at.phatbl.shellexec.ShellCommand
import org.gradle.api.GradleException
import org.gradle.testfixtures.ProjectBuilder
import org.jetbrains.spek.api.Spek
import org.ajoberstar.grgit.Remote as GRemote
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.jupiter.api.Assertions
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

object ForkPluginSpek : Spek({
    given("a plugin") {
        val plugin = ForkPlugin()
        val project = ProjectBuilder.builder().build()
        beforeGroup {
            val command = ShellCommand(project.rootDir, "git init && git remote add origin https://github.com/phatblat/Fork.git")
            command.start()
            print(command.stdout)
        }
        on("apply") {
            val command = ShellCommand(project.rootDir, "git remote -v")
            command.start()
            print(command.stdout)
            plugin.apply(project)
            it("creates an extension") {
                val extension = project.extensions.getByName("fork")
                assertTrue(extension is ForkExtension)
            }
        }
        on("build origin") {
            val gRemote = GRemote(hashMapOf(
                    "name" to "owner",
                    "url" to "https://github.com/owner/repo.git"
            ))
            val remote = plugin.buildOrigin(gRemote)
            it("creates a github remote") {
                assertNotNull(remote)
                assertTrue {
                    remote is GitHubRemote && remote.owner == "owner"
                }
                assertEquals("owner", remote.name)
                assertEquals("github.com", remote.hostname)
            }
        }
        on("parse upstream") {
            it("creates a github remote") {
                val remote = plugin.parseUpstream("owner/repo")
                assertNotNull(remote)
                assertTrue {
                    remote is GitHubRemote && remote.owner == "owner"
                }
                assertEquals("owner", remote.name)
                assertEquals("github.com", remote.hostname)
            }
        }
        on("parse invalid upstream") {
            it("throws an exception") {
                Assertions.assertThrows(GradleException::class.java) {
                    plugin.parseUpstream("not a valid github reference")
                }
            }
        }
    }
})
