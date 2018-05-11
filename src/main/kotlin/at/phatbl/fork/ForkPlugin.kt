package at.phatbl.fork

import at.phatbl.fork.model.GitHubRemote
import at.phatbl.fork.model.Remote
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Plugin configuration.
 */
class ForkPlugin : Plugin<Project> {
    lateinit var extension: ForkExtension
    lateinit var upstream: Remote

    /**
     * Called when the plugin is applied to a project.
     */
    override fun apply(project: Project) {
        extension = project.extensions.create("fork", ForkExtension::class.java)
        configureSsh()

        // Read data from DSL extension after it has been evaluated
        project.afterEvaluate {
            buildModel(extension)
        }
    }

    /**
     * Constructs data model objects from extension.
     */
    fun buildModel(extension: ForkExtension) {
        upstream = parseUpstream(extension.upstream ?: throw GradleException("No upstream provided"))
    }

    /**
     * Parses the user-provided string to build the upstream remote.
     */
    fun parseUpstream(dslString: String) : Remote {
        // Detect if string is in owner/repo format
        val components = dslString.split("/")
        if (components.count() == 2) {
            return GitHubRemote(owner = components[0], name = components[1])
        }
        throw GradleException("Unable to parse upstream remote: $dslString")

//        if (dslString.startsWith("http")) {
//            // TODO: Construct a generic http remote
//        } else if (dslString.startsWith("ssh") || dslString.startsWith("git@")) {
//            // TODO: Construct an SSH remote
//        } else {
//        }
    }

    /**
     * Configure grgit to not use JSch. Requires 2.2.0+
     * http://ajoberstar.org/grgit/grgit-authentication.html
     */
    fun configureSsh() {
        //SSH protocol using ssh or plink directly. This approach should work as long as you can push/pull on your machine
        //
        //Note
        //This is intended to replace JSch, but is new as of Grgit 2.2.0. If it proves to be reliable, it will be on by default in 3.0.0.
        //Tip
        //This support must be explicitly enabled with the system property org.ajoberstar.grgit.auth.command.allow=true.
        System.setProperty("org.ajoberstar.grgit.auth.command.allow", "true")
    }
}
