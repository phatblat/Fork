package at.phatbl.fork.model

import java.net.URL
import org.ajoberstar.grgit.Remote as GRemote

class GitHubRemote(
        val owner: String,
        repo: String
) : Remote(
        name = owner,
        url = "https://github.com/$owner/$repo.git"
) {
    private val validatedUrl: URL = URL(url)

    val httpsUrl: String
        get() = "https://$hostname/$owner/$name.git"

    val sshUrl: String
        get() = "git@$hostname:$owner/$name.git"
}
