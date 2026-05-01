package com.moviles.unaroom.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moviles.unaroom.core.UserMessages
import com.moviles.unaroom.data.AppContainer
import com.moviles.unaroom.ui.components.UpcomingReservationsCarousel
import com.moviles.unaroom.ui.components.UpcomingReservationsSectionTitle
import com.moviles.unaroom.ui.theme.AppSecondaryText

@Composable
fun HomeScreen(
    snackbarHostState: SnackbarHostState,
    successMessage: String? = null,
    onSuccessMessageShown: () -> Unit = {},
    modifier: Modifier = Modifier,
    onBrowseClassroomsClick: () -> Unit = {},
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(AppContainer.reservationRepository)
    )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(successMessage) {
        if (successMessage != null) {
            snackbarHostState.showSnackbar(message = successMessage)
            onSuccessMessageShown()
        }
    }

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.load()
        }
    }

    val errorBanner = uiState.errorBanner
    when {
        uiState.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = UserMessages.Home.LOADING_RESERVATIONS)
            }
        }

        errorBanner != null -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                TextButton(onClick = { viewModel.load() }) {
                    Text(text = errorBanner)
                }
            }
        }

        else -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = uiState.greeting,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = UserMessages.Home.SUBTITLE,
                        style = MaterialTheme.typography.bodyLarge,
                        color = AppSecondaryText
                    )
                }

                TextButton(
                    onClick = onBrowseClassroomsClick,
                    modifier = Modifier.padding(horizontal = 12.dp)
                ) {
                    Text(text = UserMessages.Home.BROWSE_CLASSROOMS)
                }

                Spacer(modifier = Modifier.height(4.dp))

                UpcomingReservationsSectionTitle()

                if (uiState.reservations.isEmpty()) {
                    Text(
                        text = UserMessages.Home.EMPTY_RESERVATIONS_TITLE,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = AppSecondaryText
                    )
                    Text(
                        text = UserMessages.Home.EMPTY_RESERVATIONS_HINT,
                        modifier = Modifier.padding(horizontal = 20.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppSecondaryText
                    )
                } else {
                    UpcomingReservationsCarousel(
                        reservations = uiState.reservations,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp)
                    )
                }
            }
        }
    }
}
