package com.moviles.unaroom.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.moviles.unaroom.core.UserMessages
import com.moviles.unaroom.data.AuthSession
import com.moviles.unaroom.data.UpcomingReservation
import com.moviles.unaroom.data.repository.ApiResult
import com.moviles.unaroom.data.repository.ReservationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val isLoading: Boolean = true,
    val userDisplayName: String = "",
    val greeting: String = "",
    val reservations: List<UpcomingReservation> = emptyList(),
    val errorBanner: String? = null
)

class HomeViewModel(
    private val reservationRepository: ReservationRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun load() {
        val user = AuthSession.currentUser
        if (user == null) {
            _uiState.value = HomeUiState(
                isLoading = false,
                greeting = "",
                errorBanner = UserMessages.Home.SESSION_EXPIRED
            )
            return
        }

        val displayName = user.name.trim()
        _uiState.value = HomeUiState(
            isLoading = true,
            userDisplayName = displayName,
            greeting = UserMessages.Home.greeting(user.name),
            reservations = emptyList(),
            errorBanner = null
        )

        viewModelScope.launch {
            when (val result = reservationRepository.getUpcomingReservations(userId = user.id)) {
                is ApiResult.Success -> {
                    _uiState.value = HomeUiState(
                        isLoading = false,
                        userDisplayName = displayName,
                        greeting = UserMessages.Home.greeting(user.name),
                        reservations = result.data,
                        errorBanner = null
                    )
                }
                is ApiResult.Error -> {
                    _uiState.value = HomeUiState(
                        isLoading = false,
                        userDisplayName = displayName,
                        greeting = UserMessages.Home.greeting(user.name),
                        reservations = emptyList(),
                        errorBanner = result.message + UserMessages.TAP_TO_RETRY_SUFFIX
                    )
                }
            }
        }
    }
}

class HomeViewModelFactory(
    private val reservationRepository: ReservationRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(reservationRepository) as T
    }
}
