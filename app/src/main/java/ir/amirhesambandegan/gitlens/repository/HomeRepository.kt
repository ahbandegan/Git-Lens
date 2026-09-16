package ir.amirhesambandegan.gitlens.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import org.koin.core.component.KoinComponent

class HomeRepository(
    private val client: HttpClient
) : KoinComponent {

    suspend fun getUsers(username: String, page: Int = 1) = client.get("/search/users") {
        header("User-Agent", "GitLens-App")
        header("Accept", "application/vnd.github+json")
        parameter("q", username.trim())
        parameter("page", page)
        parameter("per_page", 15)
    }

    suspend fun searchRepositories(query: String, page: Int = 1) = client.get("/search/repositories") {
        header("User-Agent", "GitLens-App")
        header("Accept", "application/vnd.github+json")
        parameter("q", query.trim())
        parameter("page", page)
        parameter("per_page", 15)
    }

    suspend fun getUserDetails(username: String) = client.get("/users/${username.trim()}") {
        header("User-Agent", "GitLens-App")
        header("Accept", "application/vnd.github+json")
    }

    suspend fun getUserRepositories(username: String, page: Int = 1, perPage: Int = 30) = client.get("/users/${username.trim()}/repos") {
        header("User-Agent", "GitLens-App")
        header("Accept", "application/vnd.github+json")
        parameter("page", page)
        parameter("per_page", perPage)
        parameter("sort", "updated")
    }

    suspend fun getRepositoryDetails(owner: String, repo: String) = client.get("/repos/${owner.trim()}/${repo.trim()}") {
        header("User-Agent", "GitLens-App")
        header("Accept", "application/vnd.github+json")
    }
}