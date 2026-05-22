package com.moviles.unaroom.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import com.moviles.unaroom.core.UserMessages
import androidx.compose.runtime.key

/**
 * Stateless date/time picker dialogs. Parent controls visibility with `if (show) { ... }`.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationDatePickerDialog(
    initialSelectedDateMillis: Long,
    currentDateKey: String,
    onDismiss: () -> Unit,
    onConfirmMillis: (Long) -> Unit
) {
    key(currentDateKey) {
        val state = rememberDatePickerState(initialSelectedDateMillis = initialSelectedDateMillis)
        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = {
                        state.selectedDateMillis?.let(onConfirmMillis)
                        onDismiss()
                    }
                ) { Text(UserMessages.Picker.OK) }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(UserMessages.Picker.CANCEL)
                }
            }
        ) {
            DatePicker(state = state)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationTimePickerDialog(
    timeKey: String,
    initialHour: Int,
    initialMinute: Int,
    onDismiss: () -> Unit,
    onConfirmHourMinute: (Int, Int) -> Unit
) {
    key(timeKey) {
        val timeState = rememberTimePickerState(
            initialHour = initialHour,
            initialMinute = initialMinute,
            is24Hour = true
        )
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = {
                        onConfirmHourMinute(timeState.hour, timeState.minute)
                        onDismiss()
                    }
                ) { Text(UserMessages.Picker.OK) }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(UserMessages.Picker.CANCEL)
                }
            },
            text = { TimePicker(state = timeState) }
        )
    }
}
