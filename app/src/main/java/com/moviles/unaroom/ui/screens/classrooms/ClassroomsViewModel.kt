package com.moviles.unaroom.ui.screens.classrooms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.moviles.unaroom.core.NetworkMonitor
import com.moviles.unaroom.data.Classroom
import com.moviles.unaroom.data.repository.ApiResult
import com.moviles.unaroom.data.repository.ClassroomRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ClassroomsUiState(
    val isLoading: Boolean = true,
    val classrooms: List<Classroom> = emptyList(),
    val errorBanner: String? = null,
    val isRefreshing: Boolean = false,
    val isOffline: Boolean = false
)

/**
 * Drives [ClassroomsScreen].
 * Seeds the database on first launch, observes the Room Flow, and syncs from the API.
 * Data flow:  API ──upsert──▶ Room ──Flow──▶ ViewModel ──StateFlow──▶ Screen
 */
class ClassroomsViewModel(
    private val classroomRepository: ClassroomRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClassroomsUiState())
    val uiState: StateFlow<ClassroomsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            networkMonitor.isOnline.collect { online ->
                _uiState.value = _uiState.value.copy(isOffline = !online)
            }
        }

        viewModelScope.launch {
            classroomRepository.seedIfEmpty()
            classroomRepository.getClassroomsFlow().collect { classrooms ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isRefreshing = false,
                    classrooms = classrooms
                )
            }
        }

        fetchClassrooms()
    }

    fun fetchClassrooms() {
        _uiState.value = _uiState.value.copy(errorBanner = null, isRefreshing = true)
        viewModelScope.launch {
            val result = classroomRepository.getClassrooms()
            if (result is ApiResult.Error && _uiState.value.classrooms.isEmpty()) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isRefreshing = false,
                    errorBanner = result.message
                )
            } else {
                _uiState.value = _uiState.value.copy(isRefreshing = false)
            }
        }
    }
}

class ClassroomsViewModelFactory(
    private val classroomRepository: ClassroomRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        ClassroomsViewModel(classroomRepository, networkMonitor) as T
}
