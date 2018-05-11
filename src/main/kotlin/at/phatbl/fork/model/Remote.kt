package at.phatbl.fork.model

import java.net.URL

abstract class Remote(
        val name: String,
        val url: String
) {
    private val validatedUrl: URL = URL(url)

    val hostname = validatedUrl.host
}
