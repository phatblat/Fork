package at.phatbl.fork

import at.phatbl.fork.model.GitHubRemote
import at.phatbl.fork.model.Remote
import org.ajoberstar.grgit.Grgit
import org.ajoberstar.grgit.Remote as GRemote
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.File

/**
 * Plugin configuration.
 */
class ForkPlugin : Plugin<Project> {
    /** User-provided configuration data. */
    lateinit var extension: ForkExtension

    /** In-process git access. */
    lateinit var grgit: Grgit

    /** This is the fork. */
    lateinit var origin: Remote

    /** Required upstream to mirror. */
    lateinit var upstream: Remote

    /**
     * Called when the plugin is applied to a project.
     */
    override fun apply(project: Project) {
        extension = project.extensions.create("fork", ForkExtension::class.java)
        configureSsh()

        val gitRoot = findGitRoot(project.projectDir)

        // Assume we are in a .fork subdirectory
        grgit = Grgit.open(mapOf("currentDir" to gitRoot))
        origin = buildOrigin(grgit.remote.list().first())

        // Read data from DSL extension after it has been evaluated
        project.afterEvaluate {
            buildModel(extension)
        }
    }

    /**
     * Walks up from the current dir looking for the root dir which contains a .git dir.
     * This allows for the plugin to be applied from a subfolder or the root (e.g. from tests).
     */
    fun findGitRoot(currentDir: File): File {
        val gitDir = currentDir.parentFile.walkTopDown().maxDepth(2).find {
            file -> file.name == ".git"
        } ?: throw GradleException("Could not find .git dir")

        return gitDir.parentFile
    }

    /**
     * Constructs data model objects from extension.
     */
    fun buildModel(extension: ForkExtension) {
        upstream = parseUpstream(extension.upstream ?: throw GradleException("No upstream provided"))
    }

    /**
     * Constructs the origin remote.
     */
    fun buildOrigin(remote: GRemote) : Remote {
        println("${remote.name}: ${remote.url}")
        val url = remote.url as String
        val repoName = when {
            url.startsWith("http", true) ->
                // https://github.com/phatblat/Fork.git
                url.split("/")[3]
            url.startsWith("git@", true) -> {
                // git@github.com:phatblat/Fork.git
                val name = url.split("/")[1]
                name.split(".")[0]
            }
            else -> throw GradleException("Unsupported remote url format: $url")
        }
        return GitHubRemote(remote.name, repoName)
    }

    /**
     * Parses the user-provided string to build the upstream remote.
     */
    fun parseUpstream(dslString: String) : Remote {
        // Detect if string is in owner/repo format
        val components = dslString.split("/")
        if (components.count() == 2) {
            return GitHubRemote(owner = components[0], repo = components[1])
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
