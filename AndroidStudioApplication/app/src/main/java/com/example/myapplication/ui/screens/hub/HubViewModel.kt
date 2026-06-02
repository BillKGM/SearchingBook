package com.example.myapplication.ui.screens.hub

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Author
import com.example.myapplication.data.model.Book
import com.example.myapplication.data.remote.repository.AuthorRepository
import com.example.myapplication.data.remote.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class HubUiState(
    val query: String = "",
    val searchMode: SearchMode = SearchMode.BOOKS,
    val books: List<Book> = emptyList(),
    val authors: List<Author> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

enum class SearchMode { BOOKS, AUTHORS }

class HubViewModel(
    private val bookRepository: BookRepository,
    private val authorRepository: AuthorRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HubUiState())
    val uiState: StateFlow<HubUiState> = _uiState

    fun onQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
    }

    fun onSearchModeChange(mode: SearchMode) {
        _uiState.value = _uiState.value.copy(searchMode = mode, books = emptyList(), authors = emptyList())
    }

    fun search() {
        val query = _uiState.value.query.trim()
        if (query.isEmpty()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                when (_uiState.value.searchMode) {
                    SearchMode.BOOKS -> {
                        val books = bookRepository.searchBooks(query)
                        _uiState.value = _uiState.value.copy(books = books, isLoading = false)
                    }
                    SearchMode.AUTHORS -> {
                        val authors = authorRepository.searchAuthors(query)
                        _uiState.value = _uiState.value.copy(authors = authors, isLoading = false)
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = "Ошибка загрузки: ${e.message}")
            }
        }
    }

    class Factory(private val bookRepo: BookRepository, private val authorRepo: AuthorRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = HubViewModel(bookRepo, authorRepo) as T
    }
}
