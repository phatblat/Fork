package at.phatbl.fork

import at.phatbl.shellexec.ShellExec
import org.gradle.api.tasks.Input

open class FetchRemoteTask : ShellExec() {
    @Input
    lateinit var remoteName: String

    /**
     * Builds the git command.
     */
    override fun preExec() {
        super.preExec()
        command = "git fetch $remoteName"
    }
}
