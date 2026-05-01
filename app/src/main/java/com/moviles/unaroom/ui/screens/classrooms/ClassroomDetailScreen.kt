package com.moviles.unaroom.ui.screens.classrooms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moviles.unaroom.core.UserMessages
import com.moviles.unaroom.data.Classroom
import com.moviles.unaroom.ui.components.AppButton

@Composable
fun ClassroomDetailScreen(
    classroom: Classroom,
    onReserveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = UserMessages.ClassroomDetail.NAME_PREFIX + classroom.name,
            style = MaterialTheme.typography.titleMedium
        )
        Text(text = UserMessages.ClassroomDetail.CAPACITY_PREFIX + classroom.capacity)
        Text(text = UserMessages.ClassroomDetail.LOCATION_PREFIX + classroom.location)
        Spacer(modifier = Modifier.height(16.dp))
        AppButton(
            text = UserMessages.ClassroomDetail.RESERVE_BUTTON,
            onClick = onReserveClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
