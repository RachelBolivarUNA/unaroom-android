package com.moviles.unaroom.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moviles.unaroom.core.UserMessages
import com.moviles.unaroom.data.Classroom

@Composable
fun ClassroomPickerSection(
    isLoading: Boolean,
    errorMessage: String?,
    classrooms: List<Classroom>,
    onRetry: () -> Unit,
    onClassroomSelected: (Classroom) -> Unit,
    modifier: Modifier = Modifier
) {
    when {
        isLoading -> {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
        }
        errorMessage != null -> {
            Column(modifier = modifier.fillMaxWidth()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
                TextButton(onClick = onRetry) {
                    Text(UserMessages.Picker.RETRY)
                }
            }
        }
        else -> {
            Column(modifier = modifier.fillMaxWidth()) {
                classrooms.forEach { classroom ->
                    TextButton(
                        onClick = { onClassroomSelected(classroom) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = classroom.name,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
