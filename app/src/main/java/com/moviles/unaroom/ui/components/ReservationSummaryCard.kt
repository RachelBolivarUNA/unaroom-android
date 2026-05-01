package com.moviles.unaroom.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moviles.unaroom.core.UserMessages
import com.moviles.unaroom.ui.theme.AppPrimary
import com.moviles.unaroom.ui.theme.AppSecondaryText

@Composable
fun ReservationSummaryCard(
    title: String,
    subtitle: String,
    showChangeClassroom: Boolean,
    onChangeClassroomClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = AppPrimary
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = AppSecondaryText
            )
            if (showChangeClassroom) {
                Spacer(modifier = Modifier.height(10.dp))
                TextButton(onClick = onChangeClassroomClick) {
                    Text(UserMessages.ReservationSummary.CHANGE_CLASSROOM)
                }
            }
        }
    }
}
