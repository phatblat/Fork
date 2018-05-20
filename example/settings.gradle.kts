/*
 * settings.gradle.kts
 * Fork Example
 */

rootProject.name = "ForkExample"

val forkProject = file("../")
// Include Fork plugin project in composite build
includeBuild(forkProject) {
    dependencySubstitution {
        substitute(module("at.phatbl:fork")).with(project(":"))
    }
}

// Intercept plugin lookup and map to module from composite project.
pluginManagement {
    repositories {
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "at.phatbl.fork" ->
                    useModule("at.phatbl:fork:${requested.version}")
                else -> Unit
            }
        }
    }
}
