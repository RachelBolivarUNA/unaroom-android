package com.moviles.unaroom.ui.screens.classrooms

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.moviles.unaroom.core.UserMessages
import com.moviles.unaroom.data.Classroom
import com.moviles.unaroom.data.repository.ApiResult
import com.moviles.unaroom.data.repository.ClassroomRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ClassroomsUiState(
    val isLoading: Boolean = false,
    val classrooms: List<Classroom> = emptyList(),
    val errorBanner: String? = null
)

class ClassroomsViewModel(
    private val classroomRepository: ClassroomRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ClassroomsUiState())
    val uiState: StateFlow<ClassroomsUiState> = _uiState.asStateFlow()

    init {
        fetchClassrooms()
    }

    fun fetchClassrooms() {
        _uiState.value = _uiState.value.copy(isLoading = true, errorBanner = null)
        viewModelScope.launch {
            when (val result = classroomRepository.getClassrooms()) {
                is ApiResult.Success -> {
                    _uiState.value = ClassroomsUiState(
                        isLoading = false,
                        classrooms = result.data
                    )
                }
                is ApiResult.Error -> {
                    _uiState.value = ClassroomsUiState(
                        isLoading = false,
                        errorBanner = result.message + UserMessages.TAP_TO_RETRY_SUFFIX
                    )
                }
            }
        }
    }
}

class ClassroomsViewModelFactory(
    private val classroomRepository: ClassroomRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ClassroomsViewModel(classroomRepository) as T
    }
}
