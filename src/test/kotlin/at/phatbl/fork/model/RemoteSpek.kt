package at.phatbl.fork.model

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.jupiter.api.Assertions.assertThrows
import java.net.MalformedURLException
import kotlin.test.assertNotNull

object RemoteSpek : Spek({
    given("a remote") {
        on("a valid url") {
            it("is created") {
                val remote = Remote("origin", "https://github.com/phatblat/Fork.git")
                assertNotNull(remote)
            }
        }
        on("an invalid url") {
            it("is not created") {
                assertThrows(MalformedURLException::class.java) {
                    Remote("origin", "not a url")
                }
            }
        }
    }
})
