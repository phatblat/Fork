package at.phatbl.fork.tasks

import org.gradle.testfixtures.ProjectBuilder
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertEquals

object PushRemoteTaskSpek : Spek({
    describe("push remote task") {
        val project = ProjectBuilder.builder().build()
        val task = project.tasks.create("pushRemote", PushRemoteTask::class.java)
        on("pre-execute") {
            task.remoteName = "origin"
            it("builds a git command") {
                task.preExec()
                assertEquals("git push origin", task.command)
            }
        }
    }
})
