package com.moviles.unaroom.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.MeetingRoom
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.moviles.unaroom.core.UserMessages
import com.moviles.unaroom.navigation.MainTab
import com.moviles.unaroom.ui.theme.AppBackground
import com.moviles.unaroom.ui.theme.AppNavUnselected
import com.moviles.unaroom.ui.theme.AppPrimary

@Composable
fun AppNavigationBar(
    selectedTab: MainTab,
    onHomeClick: () -> Unit,
    onClassroomsClick: () -> Unit,
    onCalendarClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        NavItem(
            selected = selectedTab == MainTab.Home,
            onClick = onHomeClick,
            icon = Icons.Filled.Home,
            label = UserMessages.NavigationBar.HOME
        )
        NavItem(
            selected = selectedTab == MainTab.Classrooms,
            onClick = onClassroomsClick,
            icon = Icons.Outlined.MeetingRoom,
            label = UserMessages.NavigationBar.CLASSROOMS
        )
        NavItem(
            selected = selectedTab == MainTab.Calendar,
            onClick = onCalendarClick,
            icon = Icons.Outlined.CalendarToday,
            label = UserMessages.NavigationBar.CALENDAR
        )
        NavItem(
            selected = selectedTab == MainTab.Profile,
            onClick = onProfileClick,
            icon = Icons.Outlined.Person,
            label = UserMessages.NavigationBar.PROFILE
        )
    }
}

@Composable
private fun RowScope.NavItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    label: String
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = label
            )
        },
        label = { Text(text = label) },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = AppPrimary,
            selectedTextColor = AppPrimary,
            unselectedIconColor = AppNavUnselected,
            unselectedTextColor = AppNavUnselected,
            indicatorColor = AppBackground
        )
    )
}
