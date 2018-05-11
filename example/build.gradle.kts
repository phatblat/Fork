/*
 * build.gradle.kts
 * Fork Example
 */

import at.phatbl.fork.ForkExtension

plugins {
    id("at.phatbl.fork") version "whatever"
}

fork {
    upstream = "owner/repo"
}
