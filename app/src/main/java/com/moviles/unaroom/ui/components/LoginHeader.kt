package com.moviles.unaroom.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MeetingRoom
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moviles.unaroom.core.UserMessages
import com.moviles.unaroom.ui.theme.AppPrimary
import com.moviles.unaroom.ui.theme.AppSecondaryText
import com.moviles.unaroom.ui.theme.AppSurfaceVariant

@Composable
fun LoginHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(82.dp)
                .background(
                    color = AppSurfaceVariant,
                    shape = RoundedCornerShape(22.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.MeetingRoom,
                contentDescription = null,
                tint = AppPrimary,
                modifier = Modifier.size(38.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = UserMessages.LoginUi.HEADER_SUBTITLE,
            color = AppSecondaryText,
            style = MaterialTheme.typography.headlineSmall
        )
    }
}
