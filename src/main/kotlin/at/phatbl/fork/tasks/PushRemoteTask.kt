package at.phatbl.fork.tasks

import at.phatbl.shellexec.ShellExec
import org.gradle.api.tasks.Input

open class PushRemoteTask : ShellExec() {
    @Input
    lateinit var remoteName: String

    init {
        group = "\uD83C\uDF74 Fork"
    }

    /**
     * Builds the git command.
     */
    override fun preExec() {
        super.preExec()
        command = "git push $remoteName"
    }
}