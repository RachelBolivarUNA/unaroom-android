package com.moviles.unaroom.ui.screens.classrooms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassroomsScreen(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    successMessage: String? = null,
    onSuccessMessageShown: () -> Unit = {},
    onClassroomClick: (Classroom) -> Unit = {},
    viewModel: ClassroomsViewModel = viewModel(
        factory = ClassroomsViewModelFactory(
            AppContainer.classroomRepository,
            AppContainer.networkMonitor
        )
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(successMessage) {
        if (successMessage != null) {
            snackbarHostState.showSnackbar(message = successMessage)
            onSuccessMessageShown()
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        // Offline banner — visible whenever the device has no internet connection.
        if (uiState.isOffline) {
            Surface(color = MaterialTheme.colorScheme.errorContainer) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WifiOff,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = UserMessages.Network.OFFLINE_BANNER,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        val errorBanner = uiState.errorBanner
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = UserMessages.Classrooms.LOADING)
                }
            }

            errorBanner != null -> {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    TextButton(onClick = { viewModel.fetchClassrooms() }) {
                        Text(text = errorBanner)
                    }
                }
            }

            else -> {
                // PullToRefreshBox wraps the list and shows a spinner when isRefreshing = true.
                // The user can pull down to trigger fetchClassrooms(), which syncs with the API.
                PullToRefreshBox(
                    isRefreshing = uiState.isRefreshing,
                    onRefresh = { viewModel.fetchClassrooms() },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    if (uiState.classrooms.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = UserMessages.Classrooms.EMPTY,
                                color = AppSecondaryText,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
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
        }
    }
}
