package at.phatbl.fork

import org.gradle.api.Task

/** Execute all task actions. */
fun Task.executeActions() = actions.forEach { it.execute(this) }
