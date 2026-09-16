package ir.amirhesambandegan.gitlens.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.call.body
import io.ktor.http.HttpStatusCode
import ir.amirhesambandegan.gitlens.model.RepoItem
import ir.amirhesambandegan.gitlens.repository.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class RepoDetailViewModel(
    private val repository: HomeRepository
) : KoinComponent, ViewModel() {

    private val _repo = MutableStateFlow<RepoItem?>(null)
    val repo = _repo.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun loadRepo(owner: String, repoName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val res = repository.getRepositoryDetails(owner, repoName)
                if (res.status == HttpStatusCode.OK) {
                    _repo.value = res.body<RepoItem>()
                } else {
                    _errorMessage.value = "Failed to load repository: ${res.status.value}"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "Failed to connect to GitHub"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
