package at.phatbl.fork.tasks

import org.gradle.testfixtures.ProjectBuilder
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertEquals

object FetchRemoteTaskSpek : Spek({
    given("a task") {
        val project = ProjectBuilder.builder().build()
        val task = project.tasks.create("fetchRemote", FetchRemoteTask::class.java)
        on("pre-execute") {
            task.remoteName = "upstream"
            it("builds a git command") {
                task.preExec()
                assertEquals("git fetch upstream", task.command)
            }
        }
    }
})
