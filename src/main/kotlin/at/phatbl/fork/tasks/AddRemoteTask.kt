package at.phatbl.fork.tasks

import at.phatbl.shellexec.ShellCommand
import at.phatbl.shellexec.ShellExec
import org.gradle.api.tasks.Input

open class AddRemoteTask : ShellExec() {
    @Input
    lateinit var remoteName: String

    @Input
    lateinit var url: String

    init {
        group = "\uD83C\uDF74 Fork"

        onlyIf {
            val detectExisting = ShellCommand(project.rootDir, "git remote | grep '^upstream'")
            detectExisting.start()
            print(detectExisting.stdout)
            // Only add if remote isn't found
            detectExisting.failed
        }
    }

    /**
     * Builds the git command.
     */
    override fun preExec() {
        super.preExec()
        command = "git add $remoteName $url"
    }
}