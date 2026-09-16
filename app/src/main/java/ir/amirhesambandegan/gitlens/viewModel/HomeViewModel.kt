package ir.amirhesambandegan.gitlens.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.http.HttpStatusCode
import ir.amirhesambandegan.gitlens.model.RepoSearchResponse
import ir.amirhesambandegan.gitlens.model.SearchResponse
import ir.amirhesambandegan.gitlens.repository.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

enum class SearchTab(val title: String) {
    USERS("Users"),
    REPOSITORIES("Repositories")
}

class HomeViewModel(
    private val repository: HomeRepository
) : KoinComponent, ViewModel() {

    private val _currentTab = MutableStateFlow(SearchTab.USERS)
    val currentTab = _currentTab.asStateFlow()

    private val _users = MutableStateFlow<SearchResponse?>(null)
    val users = _users.asStateFlow()

    private val _repositories = MutableStateFlow<RepoSearchResponse?>(null)
    val repositories = _repositories.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun setTab(tab: SearchTab) {
        _currentTab.value = tab
    }

    fun getUsers(username: String, page: Int = 1) {
        if (username.isBlank()) {
            _users.value = null
            _isLoading.value = false
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val res = repository.getUsers(username, page)
                if (res.status == HttpStatusCode.OK) {
                    _users.emit(res.body<SearchResponse>())
                } else {
                    _errorMessage.emit("Server error: ${res.status.value}")
                }
            } catch (e: HttpRequestTimeoutException) {
                _errorMessage.emit("Request timeout: Please check your connection")
            } catch (e: Exception) {
                _errorMessage.emit(e.localizedMessage ?: "Unknown network error")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchRepositories(query: String, page: Int = 1) {
        if (query.isBlank()) {
            _repositories.value = null
            _isLoading.value = false
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val res = repository.searchRepositories(query, page)
                if (res.status == HttpStatusCode.OK) {
                    _repositories.emit(res.body<RepoSearchResponse>())
                } else {
                    _errorMessage.emit("Server error: ${res.status.value}")
                }
            } catch (e: HttpRequestTimeoutException) {
                _errorMessage.emit("Request timeout: Please check your connection")
            } catch (e: Exception) {
                _errorMessage.emit(e.localizedMessage ?: "Unknown network error")
            } finally {
                _isLoading.value = false
            }
        }
    }
}