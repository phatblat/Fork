/*
 * settings.gradle.kts
 * Fork
 */

rootProject.name = "Fork"

// Include grgit project in composite build
val grgitProject = file("../grgit-DISABLED")
if (grgitProject.exists()) {
    includeBuild(grgitProject) {
        dependencySubstitution {
            substitute(module("org.ajoberstar:grgit")).with(project(":"))
        }
    }
}

// Workaround to make the JUnit Platform Gradle Plugin available using the `plugins` DSL
// See: https://github.com/junit-team/junit5/issues/768#issuecomment-330078905
pluginManagement {
    repositories {
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "at.phatbl.clamp" ->
                    useModule("at.phatbl:clamp:${requested.version}")
                "org.junit.platform.gradle.plugin" ->
                    useModule("org.junit.platform:junit-platform-gradle-plugin:${requested.version}")
                else -> Unit
            }
        }
    }
}
