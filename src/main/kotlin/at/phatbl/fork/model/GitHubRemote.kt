package at.phatbl.fork.model

import java.net.URL

class GitHubRemote(
        val owner: String,
        name: String
) : Remote(
        name = name,
        url = "https://github.com/$owner/$name.git"
) {
    private val validatedUrl: URL = URL(url)

    val httpsUrl: String
        get() = "https://$hostname/$owner/$name"

    val sshUrl: String
        get() = "git@$hostname:$owner/$name.git"
}
