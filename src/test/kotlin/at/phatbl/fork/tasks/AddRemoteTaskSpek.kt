package at.phatbl.fork.tasks

import org.gradle.testfixtures.ProjectBuilder
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertEquals

object AddRemoteTaskSpek : Spek({
    describe("add remote task") {
        val project = ProjectBuilder.builder().build()
        val task = project.tasks.create("addRemote", AddRemoteTask::class.java)
        on("pre-execute") {
            task.remoteName = "upstream"
            task.url = "git@github.com:phatblat/Fork.git"
            it("builds a git command") {
                task.preExec()
                assertEquals("git add upstream git@github.com:phatblat/Fork.git", task.command)
            }
        }
    }
})
