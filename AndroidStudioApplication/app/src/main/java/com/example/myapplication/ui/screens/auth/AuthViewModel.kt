package com.example.myapplication.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isRegisterMode: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    fun onEmailChange(email: String) { _uiState.value = _uiState.value.copy(email = email, error = null) }
    fun onPasswordChange(password: String) { _uiState.value = _uiState.value.copy(password = password, error = null) }
    fun onConfirmPasswordChange(password: String) { _uiState.value = _uiState.value.copy(confirmPassword = password, error = null) }
    fun toggleMode() { _uiState.value = _uiState.value.copy(isRegisterMode = !_uiState.value.isRegisterMode, error = null) }

    fun submit() {
        val state = _uiState.value
        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Заполните все поля")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = if (state.isRegisterMode) {
                if (state.password != state.confirmPassword) {
                    _uiState.value = _uiState.value.copy(isLoading = false, error = "Пароли не совпадают")
                    return@launch
                }
                userRepository.register(state.email, state.password)
            } else {
                userRepository.login(state.email, state.password)
            }
            result.fold(
                onSuccess = { _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true) },
                onFailure = { _uiState.value = _uiState.value.copy(isLoading = false, error = it.message) }
            )
        }
    }

    class Factory(private val userRepo: UserRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = AuthViewModel(userRepo) as T
    }
}
