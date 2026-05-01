package com.moviles.unaroom.ui.screens.reservation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moviles.unaroom.core.UserMessages
import com.moviles.unaroom.data.AppContainer
import com.moviles.unaroom.ui.components.AppButton
import com.moviles.unaroom.ui.components.AppTextField
import com.moviles.unaroom.ui.components.ClassroomPickerSection
import com.moviles.unaroom.ui.components.ReservationDatePickerDialog
import com.moviles.unaroom.ui.components.ReservationSummaryCard
import com.moviles.unaroom.ui.components.ReservationTimePickerDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationCreateScreen(
    snackbarHostState: SnackbarHostState,
    initialClassroomId: String = "",
    initialClassroomName: String = "",
    modifier: Modifier = Modifier,
    viewModel: ReservationViewModel = viewModel(
        factory = ReservationViewModelFactory(
            AppContainer.reservationRepository,
            AppContainer.classroomRepository
        )
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    LaunchedEffect(initialClassroomId, initialClassroomName) {
        viewModel.onInitialArguments(initialClassroomId, initialClassroomName)
    }

    LaunchedEffect(uiState.successMessage, uiState.errorMessage) {
        val message = uiState.successMessage ?: uiState.errorMessage
        if (message != null) {
            snackbarHostState.showSnackbar(message)
            viewModel.clearMessage()
        }
    }

    if (showDatePicker) {
        ReservationDatePickerDialog(
            initialSelectedDateMillis = viewModel.initialDatePickerMillis(),
            currentDateKey = uiState.date,
            onDismiss = { showDatePicker = false },
            onConfirmMillis = { millis -> viewModel.onDatePickedFromMillis(millis) }
        )
    }

    if (showStartTimePicker) {
        val (h, m) = viewModel.initialHourMinuteForPicker(uiState.startTime)
        ReservationTimePickerDialog(
            timeKey = uiState.startTime,
            initialHour = h,
            initialMinute = m,
            onDismiss = { showStartTimePicker = false },
            onConfirmHourMinute = { hour, minute -> viewModel.onStartTimePicked(hour, minute) }
        )
    }

    if (showEndTimePicker) {
        val (h, m) = viewModel.initialHourMinuteForPicker(uiState.endTime)
        ReservationTimePickerDialog(
            timeKey = uiState.endTime,
            initialHour = h,
            initialMinute = m,
            onDismiss = { showEndTimePicker = false },
            onConfirmHourMinute = { hour, minute -> viewModel.onEndTimePicked(hour, minute) }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(4.dp))

        ReservationSummaryCard(
            title = uiState.summaryTitle,
            subtitle = uiState.summarySubtitle,
            showChangeClassroom = uiState.classroomId.isNotBlank(),
            onChangeClassroomClick = { viewModel.clearClassroomSelection() }
        )

        if (uiState.classroomId.isBlank()) {
            ClassroomPickerSection(
                isLoading = uiState.pickerLoading,
                errorMessage = uiState.pickerError,
                classrooms = uiState.pickerClassrooms,
                onRetry = { viewModel.loadClassroomsForPicker() },
                onClassroomSelected = { viewModel.selectClassroom(it) }
            )
        }

        AppTextField(
            value = uiState.date,
            label = UserMessages.Reservation.FIELD_DATE,
            placeholder = UserMessages.Reservation.FIELD_DATE_PLACEHOLDER,
            onValueChange = {},
            readOnly = true,
            onClick = { showDatePicker = true }
        )

        AppTextField(
            value = uiState.startTime,
            label = UserMessages.Reservation.FIELD_START,
            placeholder = UserMessages.Reservation.FIELD_TIME_PLACEHOLDER,
            onValueChange = {},
            readOnly = true,
            onClick = { showStartTimePicker = true }
        )

        AppTextField(
            value = uiState.endTime,
            label = UserMessages.Reservation.FIELD_END,
            placeholder = UserMessages.Reservation.FIELD_TIME_PLACEHOLDER,
            onValueChange = {},
            readOnly = true,
            onClick = { showEndTimePicker = true }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            AppButton(
                text = if (uiState.isLoading) {
                    UserMessages.Reservation.BUTTON_CREATING
                } else {
                    UserMessages.Reservation.BUTTON_CREATE
                },
                onClick = { viewModel.createReservation() },
                enabled = !uiState.isLoading
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
