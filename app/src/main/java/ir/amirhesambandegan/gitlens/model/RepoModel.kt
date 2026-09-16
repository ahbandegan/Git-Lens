package ir.amirhesambandegan.gitlens.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RepoSearchResponse(
    @SerialName("total_count") val totalCount: Int = 0,
    @SerialName("incomplete_results") val incompleteResults: Boolean = false,
    val items: List<RepoItem> = emptyList()
)

@Serializable
data class RepoItem(
    val id: Long,
    val name: String,
    @SerialName("full_name") val fullName: String,
    val owner: RepoOwner? = null,
    val private: Boolean = false,
    @SerialName("html_url") val htmlUrl: String,
    val description: String? = null,
    val fork: Boolean = false,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null,
    @SerialName("pushed_at") val pushedAt: String? = null,
    val homepage: String? = null,
    val size: Long = 0,
    @SerialName("stargazers_count") val stargazersCount: Long = 0,
    @SerialName("watchers_count") val watchersCount: Long = 0,
    val language: String? = null,
    @SerialName("forks_count") val forksCount: Long = 0,
    @SerialName("open_issues_count") val openIssuesCount: Long = 0,
    @SerialName("default_branch") val defaultBranch: String = "main",
    val topics: List<String> = emptyList(),
    val visibility: String? = null,
    @SerialName("clone_url") val cloneUrl: String? = null,
    @SerialName("ssh_url") val sshUrl: String? = null,
    val license: RepoLicense? = null
)

@Serializable
data class RepoOwner(
    val login: String,
    val id: Long,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("html_url") val htmlUrl: String? = null
)

@Serializable
data class RepoLicense(
    val key: String? = null,
    val name: String? = null,
    @SerialName("spdx_id") val spdxId: String? = null,
    val url: String? = null
)
