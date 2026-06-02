package com.example.myapplication.ui.screens.author

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.repository.FavoriteRepository
import com.example.myapplication.data.model.Author
import com.example.myapplication.data.model.AuthorWork
import com.example.myapplication.data.remote.repository.AuthorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AuthorUiState(
    val author: Author? = null,
    val works: List<AuthorWork> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null,
    val isFavorite: Boolean = false
)

class AuthorViewModel(
    private val authorId: String,
    private val authorRepository: AuthorRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthorUiState())
    val uiState: StateFlow<AuthorUiState> = _uiState

    init {
        loadAuthor()
        checkFavorite()
    }

    private fun loadAuthor() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val author = authorRepository.getAuthorDetail(authorId)
                val works = authorRepository.getAuthorWorks(authorId)
                _uiState.value = _uiState.value.copy(author = author, works = works, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    private fun checkFavorite() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isFavorite = favoriteRepository.isFavorite(authorId))
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val author = _uiState.value.author ?: return@launch
            if (_uiState.value.isFavorite) {
                favoriteRepository.removeFavorite(authorId)
                _uiState.value = _uiState.value.copy(isFavorite = false)
            } else {
                favoriteRepository.addFavorite(authorId, author.name, null, author.photos?.firstOrNull(), "author")
                _uiState.value = _uiState.value.copy(isFavorite = true)
            }
        }
    }

    class Factory(private val authorId: String, private val authorRepo: AuthorRepository, private val favRepo: FavoriteRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = AuthorViewModel(authorId, authorRepo, favRepo) as T
    }
}
