package com.moviles.unaroom.ui.screens.classrooms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moviles.unaroom.data.Classroom

@Composable
fun ClassroomsScreen(modifier: Modifier = Modifier) {
    // Mock list – hardcoded for now, will replace with ViewModel/API later
    val classrooms = listOf(
        Classroom(name = "A-101", capacity = 30, location = "Building A"),
        Classroom(name = "B-204", capacity = 45, location = "Building B"),
        Classroom(name = "Lab-3", capacity = 20, location = "Tech Wing")
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Classrooms Screen",
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

