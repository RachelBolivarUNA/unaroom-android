package com.moviles.unaroom.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.moviles.unaroom.core.UserMessages
import com.moviles.unaroom.data.AppContainer
import com.moviles.unaroom.data.AuthSession
import com.moviles.unaroom.data.remote.model.UserDto
import com.moviles.unaroom.data.repository.ApiResult
import com.moviles.unaroom.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val isLoading: Boolean = false,
    val user: UserDto? = null,
    val errorMessage: String? = null
)

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String) {
        val trimmedEmail = email.trim()
        if (trimmedEmail.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState(errorMessage = UserMessages.Auth.LOGIN_EMPTY_FIELDS)
            return
        }
        _uiState.value = LoginUiState(isLoading = true)
        viewModelScope.launch {
            when (val result = authRepository.login(email = trimmedEmail, password = password)) {
                is ApiResult.Success -> {
                    AuthSession.setUser(result.data)
                    _uiState.value = LoginUiState(user = result.data)
                    // After login, register the FCM token so the backend can push notifications.
                    registerFcmTokenWithBackend()
                }
                is ApiResult.Error -> {
                    _uiState.value = LoginUiState(errorMessage = result.message)
                }
            }
        }
    }

    /**
     * Fetches the current FCM token and registers it with the backend for this user.
     * Non-fatal — a failure here does not affect the login flow.
     */
    private fun registerFcmTokenWithBackend() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            viewModelScope.launch {
                AppContainer.fcmTokenRepository.registerToken(token)
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

class LoginViewModelFactory(
    private val authRepository: AuthRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(authRepository) as T
    }
}
