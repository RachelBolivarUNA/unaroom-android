package com.moviles.unaroom.ui.screens.reservation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.moviles.unaroom.core.UserMessages
import com.moviles.unaroom.data.AuthSession
import com.moviles.unaroom.data.Classroom
import com.moviles.unaroom.data.repository.ApiResult
import com.moviles.unaroom.data.repository.ClassroomRepository
import com.moviles.unaroom.data.repository.ReservationRepository
import com.moviles.unaroom.domain.datetime.ReservationDateTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ReservationUiState(
    val classroomId: String = "",
    val classroomDisplayName: String = "",
    val pickerClassrooms: List<Classroom> = emptyList(),
    val pickerLoading: Boolean = false,
    val pickerError: String? = null,
    val date: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val isLoading: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
) {
    val summaryTitle: String
        get() = when {
            classroomId.isBlank() -> UserMessages.Reservation.SUMMARY_CHOOSE_CLASSROOM
            classroomDisplayName.isNotBlank() ->
                UserMessages.Reservation.SUMMARY_FOR_ROOM_PREFIX + classroomDisplayName
            else -> UserMessages.Reservation.SUMMARY_FOR_ROOM_FALLBACK
        }

    val summarySubtitle: String
        get() = when {
            classroomId.isBlank() -> UserMessages.Reservation.SUMMARY_HINT_NO_ROOM
            else -> UserMessages.Reservation.SUMMARY_HINT_WITH_ROOM
        }
}

class ReservationViewModel(
    private val reservationRepository: ReservationRepository,
    private val classroomRepository: ClassroomRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ReservationUiState())
    val uiState: StateFlow<ReservationUiState> = _uiState.asStateFlow()

    fun onInitialArguments(initialClassroomId: String, initialClassroomName: String) {
        if (initialClassroomId.isNotBlank()) {
            setClassroomFromNav(initialClassroomId, initialClassroomName)
        } else {
            val s = _uiState.value
            if (!s.pickerLoading && s.pickerClassrooms.isEmpty()) {
                loadClassroomsForPicker()
            }
        }
    }

    fun setClassroomFromNav(id: String, displayName: String) {
        _uiState.value = _uiState.value.copy(
            classroomId = id.trim(),
            classroomDisplayName = displayName.trim()
        )
    }

    fun selectClassroom(classroom: Classroom) {
        _uiState.value = _uiState.value.copy(
            classroomId = classroom.id,
            classroomDisplayName = classroom.name
        )
    }

    fun clearClassroomSelection() {
        _uiState.value = _uiState.value.copy(
            classroomId = "",
            classroomDisplayName = "",
            pickerClassrooms = emptyList(),
            pickerError = null
        )
        loadClassroomsForPicker()
    }

    fun loadClassroomsForPicker() {
        viewModelScope.launch {
            if (_uiState.value.pickerLoading) return@launch
            _uiState.value = _uiState.value.copy(pickerLoading = true, pickerError = null)
            when (val result = classroomRepository.getClassrooms()) {
                is ApiResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        pickerLoading = false,
                        pickerClassrooms = result.data
                    )
                }
                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        pickerLoading = false,
                        pickerError = result.message
                    )
                }
            }
        }
    }

    fun onDateChange(value: String) {
        _uiState.value = _uiState.value.copy(date = value)
    }

    fun onDatePickedFromMillis(millis: Long) {
        onDateChange(ReservationDateTime.millisToIsoUtcDate(millis))
    }

    fun onStartTimeChange(value: String) {
        _uiState.value = _uiState.value.copy(startTime = value)
    }

    fun onEndTimeChange(value: String) {
        _uiState.value = _uiState.value.copy(endTime = value)
    }

    fun onStartTimePicked(hour: Int, minute: Int) {
        onStartTimeChange(ReservationDateTime.formatHourMinuteToApi(hour, minute))
    }

    fun onEndTimePicked(hour: Int, minute: Int) {
        onEndTimeChange(ReservationDateTime.formatHourMinuteToApi(hour, minute))
    }

    /** Millis for [DatePicker] initial selection (UTC calendar day). */
    fun initialDatePickerMillis(): Long =
        ReservationDateTime.isoDateToMillisUtc(_uiState.value.date)
            ?: ReservationDateTime.todayUtcStartMillis()

    fun initialHourMinuteForPicker(time: String): Pair<Int, Int> =
        ReservationDateTime.initialHourMinute(time)

    fun createReservation() {
        val current = _uiState.value
        val userId = AuthSession.currentUser?.id?.trim().orEmpty()
        if (userId.isBlank()) {
            _uiState.value = current.copy(errorMessage = UserMessages.Reservation.SESSION_EXPIRED)
            return
        }

        val classroomId = current.classroomId.trim()
        if (classroomId.isBlank() || current.date.isBlank() ||
            current.startTime.isBlank() || current.endTime.isBlank()
        ) {
            _uiState.value = current.copy(errorMessage = UserMessages.Reservation.VALIDATION_INCOMPLETE)
            return
        }

        val startApi = ReservationDateTime.normalizeToApiTime(current.startTime)
        val endApi = ReservationDateTime.normalizeToApiTime(current.endTime)

        _uiState.value = current.copy(
            isLoading = true,
            errorMessage = null,
            successMessage = null
        )
        viewModelScope.launch {
            when (
                val result = reservationRepository.createReservation(
                    userId = userId,
                    classroomId = classroomId,
                    date = current.date.trim(),
                    startTime = startApi,
                    endTime = endApi
                )
            ) {
                is ApiResult.Success -> {
                    _uiState.value = ReservationUiState(
                        successMessage = UserMessages.Reservation.SUCCESS_CREATED
                    )
                }
                is ApiResult.Error -> {
                    _uiState.value = current.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
            }
        }
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(successMessage = null, errorMessage = null)
    }
}

class ReservationViewModelFactory(
    private val reservationRepository: ReservationRepository,
    private val classroomRepository: ClassroomRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ReservationViewModel(reservationRepository, classroomRepository) as T
    }
}
