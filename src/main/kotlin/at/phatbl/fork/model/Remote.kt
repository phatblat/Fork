package at.phatbl.fork.model

import java.net.URL

data class Remote(
        val name: String,
        val url: String
) {
    init {
        val validatedUrl = URL(url)
    }
}
