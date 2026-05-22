package com.moviles.unaroom.ui.screens.classrooms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moviles.unaroom.core.UserMessages
import com.moviles.unaroom.data.AppContainer
import com.moviles.unaroom.data.Classroom
import com.moviles.unaroom.ui.components.ClassroomCard
import com.moviles.unaroom.ui.theme.AppSecondaryText

@Composable
fun ClassroomsScreen(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    successMessage: String? = null,
    onSuccessMessageShown: () -> Unit = {},
    onClassroomClick: (Classroom) -> Unit = {},
    viewModel: ClassroomsViewModel = viewModel(
        factory = ClassroomsViewModelFactory(AppContainer.classroomRepository)
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(successMessage) {
        if (successMessage != null) {
            snackbarHostState.showSnackbar(message = successMessage)
            onSuccessMessageShown()
        }
    }

    val errorBanner = uiState.errorBanner
    when {
        uiState.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = UserMessages.Classrooms.LOADING)
            }
        }

        errorBanner != null -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                TextButton(onClick = { viewModel.fetchClassrooms() }) {
                    Text(text = errorBanner)
                }
            }
        }

        uiState.classrooms.isEmpty() -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = UserMessages.Classrooms.EMPTY,
                    color = AppSecondaryText,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        else -> {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 14.dp,
                    end = 14.dp,
                    top = 12.dp,
                    bottom = 96.dp
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(uiState.classrooms) { classroom ->
                    ClassroomCard(
                        classroom = classroom,
                        onClick = { onClassroomClick(classroom) }
                    )
                }
            }
        }
    }
}
