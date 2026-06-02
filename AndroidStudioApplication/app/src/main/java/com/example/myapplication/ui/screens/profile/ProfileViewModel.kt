package com.example.myapplication.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.entity.FavoriteEntity
import com.example.myapplication.data.local.entity.UserEntity
import com.example.myapplication.data.local.repository.FavoriteRepository
import com.example.myapplication.data.local.repository.UserRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ProfileUiState(
    val user: UserEntity? = null,
    val favoriteBooks: List<FavoriteEntity> = emptyList(),
    val favoriteAuthors: List<FavoriteEntity> = emptyList(),
    val wantToRead: List<FavoriteEntity> = emptyList(),
    val isLoggedIn: Boolean = false
)

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState

    init {
        viewModelScope.launch {
            userRepository.getLoggedUser().collect { user ->
                _uiState.value = _uiState.value.copy(user = user, isLoggedIn = user != null)
            }
        }
        viewModelScope.launch {
            favoriteRepository.getFavoritesByType("book").collect { books ->
                _uiState.value = _uiState.value.copy(favoriteBooks = books)
            }
        }
        viewModelScope.launch {
            favoriteRepository.getFavoritesByType("author").collect { authors ->
                _uiState.value = _uiState.value.copy(favoriteAuthors = authors)
            }
        }
        viewModelScope.launch {
            favoriteRepository.getFavoritesByType("want_to_read").collect { wtr ->
                _uiState.value = _uiState.value.copy(wantToRead = wtr)
            }
        }
    }

    fun logout() {
        viewModelScope.launch { userRepository.logout() }
    }

    fun removeFavorite(id: String) {
        viewModelScope.launch { favoriteRepository.removeFavorite(id) }
    }

    class Factory(private val userRepo: UserRepository, private val favRepo: FavoriteRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = ProfileViewModel(userRepo, favRepo) as T
    }
}
