package com.moviles.unaroom.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.moviles.unaroom.core.UserMessages
import com.moviles.unaroom.data.UpcomingReservation
import com.moviles.unaroom.ui.theme.AppPrimary
import com.moviles.unaroom.ui.theme.AppSecondaryText

/**
 * Horizontal pager carousel for upcoming reservation cards (peek of adjacent pages).
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun UpcomingReservationsCarousel(
    reservations: List<UpcomingReservation>,
    modifier: Modifier = Modifier,
    onReservationClick: (UpcomingReservation) -> Unit = {}
) {
    if (reservations.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { reservations.size })

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 24.dp),
            pageSpacing = 12.dp,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val item = reservations[page]
            UpcomingReservationCard(
                reservation = item,
                onClick = { onReservationClick(item) }
            )
        }

        if (reservations.size > 1) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(reservations.size) { index ->
                    val selected = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .size(if (selected) 8.dp else 6.dp)
                            .clip(CircleShape)
                            .background(
                                if (selected) AppPrimary else AppSecondaryText.copy(alpha = 0.35f)
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun UpcomingReservationsSectionTitle(
    modifier: Modifier = Modifier
) {
    Text(
        text = UserMessages.Home.SECTION_UPCOMING,
        modifier = modifier.padding(horizontal = 20.dp, vertical = 4.dp),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground
    )
}
