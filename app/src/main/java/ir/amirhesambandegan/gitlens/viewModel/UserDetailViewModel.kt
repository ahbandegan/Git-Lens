package ir.amirhesambandegan.gitlens.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import ir.amirhesambandegan.gitlens.model.RepoItem
import ir.amirhesambandegan.gitlens.model.UserDetailResponse
import ir.amirhesambandegan.gitlens.repository.HomeRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class UserDetailViewModel(
    private val repository: HomeRepository
) : KoinComponent, ViewModel() {

    private val _user = MutableStateFlow<UserDetailResponse?>(null)
    val user = _user.asStateFlow()

    private val _repositories = MutableStateFlow<List<RepoItem>>(emptyList())
    val repositories = _repositories.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab = _selectedTab.asStateFlow()

    fun setSelectedTab(index: Int) {
        _selectedTab.value = index
    }

    fun loadUser(username: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val userDeferred = async { repository.getUserDetails(username) }
                val reposDeferred = async { repository.getUserRepositories(username) }

                val userRes = userDeferred.await()
                val reposRes = reposDeferred.await()

                if (userRes.status == HttpStatusCode.OK) {
                    _user.value = userRes.body<UserDetailResponse>()
                } else {
                    _errorMessage.value = "Failed to load user profile: ${userRes.status.value}"
                }

                if (reposRes.status == HttpStatusCode.OK) {
                    _repositories.value = reposRes.body<List<RepoItem>>()
                }
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "Failed to connect to GitHub"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
