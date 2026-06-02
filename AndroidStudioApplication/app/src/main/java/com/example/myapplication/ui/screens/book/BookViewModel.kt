package com.example.myapplication.ui.screens.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.repository.FavoriteRepository
import com.example.myapplication.data.model.Book
import com.example.myapplication.data.remote.repository.BookRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class BookUiState(
    val book: Book? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val isFavorite: Boolean = false,
    val isWantToRead: Boolean = false
)

class BookViewModel(
    private val bookId: String,
    private val bookRepository: BookRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookUiState())
    val uiState: StateFlow<BookUiState> = _uiState

    init {
        loadBook()
        checkFavorites()
    }

    private fun loadBook() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val book = bookRepository.getBookDetail(bookId)
                _uiState.value = _uiState.value.copy(book = book, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    private fun checkFavorites() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isFavorite = favoriteRepository.isFavorite(bookId + "_fav"),
                isWantToRead = favoriteRepository.isFavorite(bookId + "_wtr")
            )
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val book = _uiState.value.book ?: return@launch
            val id = bookId + "_fav"
            if (_uiState.value.isFavorite) {
                favoriteRepository.removeFavorite(id)
                _uiState.value = _uiState.value.copy(isFavorite = false)
            } else {
                favoriteRepository.addFavorite(id, book.title, book.authorName, book.coverId, "book")
                _uiState.value = _uiState.value.copy(isFavorite = true)
            }
        }
    }

    fun toggleWantToRead() {
        viewModelScope.launch {
            val book = _uiState.value.book ?: return@launch
            val id = bookId + "_wtr"
            if (_uiState.value.isWantToRead) {
                favoriteRepository.removeFavorite(id)
                _uiState.value = _uiState.value.copy(isWantToRead = false)
            } else {
                favoriteRepository.addFavorite(id, book.title, book.authorName, book.coverId, "want_to_read")
                _uiState.value = _uiState.value.copy(isWantToRead = true)
            }
        }
    }

    class Factory(private val bookId: String, private val bookRepo: BookRepository, private val favRepo: FavoriteRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = BookViewModel(bookId, bookRepo, favRepo) as T
    }
}
