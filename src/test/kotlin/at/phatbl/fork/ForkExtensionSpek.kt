package at.phatbl.fork

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertNull

object ForkExtensionSpek : Spek({
    describe("dsl extension") {
        val extension = ForkExtension()
        it("has an upstream property") {
            assertNull(extension.upstream)
        }
    }
})
