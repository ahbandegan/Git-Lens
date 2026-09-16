package ir.amirhesambandegan.gitlens.navigation.modals

import kotlinx.serialization.Serializable

@Serializable
object HomeModal

@Serializable
data class UserDetailModal(val username: String)

@Serializable
data class RepoDetailModal(val owner: String, val repoName: String)