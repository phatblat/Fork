package at.phatbl.fork.model

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.jupiter.api.Assertions.assertThrows
import java.net.MalformedURLException
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Concrete class for testing abstract Remote.
 */
class TestRemote(
        name: String,
        url: String
) : Remote(name, url)

object RemoteSpek : Spek({
    given("a remote") {
        on("a valid url") {
            val url = "https://github.com/phatblat/Fork.git"
            it("is created") {
                val remote = TestRemote("origin", url)
                assertNotNull(remote)
                assertEquals(remote.hostname, "github.com")
            }
        }
        on("an invalid url") {
            val url = "not a url"
            it("is not created") {
                assertThrows(MalformedURLException::class.java) {
                    TestRemote("origin", url)
                }
            }
        }
    }
})
