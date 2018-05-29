package at.phatbl.fork

import at.phatbl.fork.model.GitHubRemote
import at.phatbl.fork.tasks.AddRemoteTask
import at.phatbl.fork.tasks.FetchRemoteTask
import at.phatbl.fork.tasks.PushRemoteTask
import at.phatbl.shellexec.ShellCommand
import org.gradle.api.GradleException
import org.gradle.testfixtures.ProjectBuilder
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.*
import org.ajoberstar.grgit.Remote as GRemote
import org.junit.jupiter.api.Assertions
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

object ForkPluginSpek : Spek({
    describe("a plugin") {
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
        on("configure ssh") {
            plugin.configureSsh()
            it("sets a system property") {
                assertEquals("true", System.getProperty("org.ajoberstar.grgit.auth.command.allow"))
            }
        }
        context("find git root") {
            on("the root project") {
                val rootDir = project.rootDir
                val gitRoot = plugin.findGitRoot(rootDir)
                it("is the same as the root project dir") {
                    assertEquals(rootDir, gitRoot)
                }
            }
            on("a child project") {
                val subprojectDir = File(project.rootDir, ".fork")
                subprojectDir.mkdir()
                val subproject = ProjectBuilder.builder().withProjectDir(subprojectDir).build()
                val gitRoot = plugin.findGitRoot(subprojectDir)
                it("is the same as the root project dir") {
                    assertEquals(project.rootDir, gitRoot)
                }
            }
        }
        context("build origin") {
            on("matching owner and remote name") {
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
            on("different owner and remote names") {
                val gRemote = GRemote(hashMapOf(
                        "name" to "upstream",
                        "url" to "https://github.com/owner/repo.git"
                ))
                val remote = plugin.buildOrigin(gRemote)
                it("creates a github remote") {
                    assertNotNull(remote)
                    assertTrue {
                        remote is GitHubRemote && remote.owner == "owner"
                    }
                    assertEquals("upstream", remote.name)
                    assertEquals("github.com", remote.hostname)
                }
            }
        }
        on("parse upstream") {
            it("creates a github remote") {
                val remote = plugin.parseUpstream("owner/repo")
                assertNotNull(remote)
                assertTrue {
                    remote is GitHubRemote && remote.owner == "owner"
                }
                assertEquals("upstream", remote.name)
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
        on("create tasks") {
            plugin.origin = GitHubRemote(
                    name = "origin",
                    owner = "phatblat",
                    repoName = "Fork"
            )
            plugin.upstream = GitHubRemote(
                    name = "upstream",
                    owner = "phatblat",
                    repoName = "Fork"
            )
            plugin.createTasks(project)
            it("creates an add remote task") {
                val task = project.tasks.getByName("addRemoteUpstream")
                assertNotNull(task)
                assertTrue { task is AddRemoteTask }
            }
            it("creates a fetch remote task for origin") {
                val task = project.tasks.getByName("fetchOrigin")
                assertNotNull(task)
                assertTrue { task is FetchRemoteTask }
            }
            it("creates a fetch remote task for upstream") {
                val task = project.tasks.getByName("fetchUpstream")
                assertNotNull(task)
                assertTrue { task is FetchRemoteTask }
            }
            it("creates a push remote task") {
                val task = project.tasks.getByName("pushRemoteOrigin")
                assertNotNull(task)
                assertTrue { task is PushRemoteTask }
            }
        }
    }
})
