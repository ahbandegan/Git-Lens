package ir.amirhesambandegan.gitlens.navigation.modals

import kotlinx.serialization.Serializable

@Serializable
data class RepoDetailModal(val owner: String, val repoName: String)