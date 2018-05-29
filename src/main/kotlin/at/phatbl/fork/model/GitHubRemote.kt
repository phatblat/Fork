package at.phatbl.fork.model

import java.net.URL
import org.ajoberstar.grgit.Remote as GRemote

class GitHubRemote(
        val owner: String,
        name: String = owner,
        repoName: String
) : Remote(
        name = name,
        url = "https://github.com/$owner/$repoName.git"
) {
    private val validatedUrl: URL = URL(url)

    val httpsUrl: String
        get() = "https://$hostname/$owner/$name.git"

    val sshUrl: String
        get() = "git@$hostname:$owner/$name.git"
}
