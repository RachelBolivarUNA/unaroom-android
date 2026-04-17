package com.moviles.unaroom

import androidx.compose.runtime.Composable
import com.moviles.unaroom.navigation.AppNavHost
import com.moviles.unaroom.ui.theme.UnaRoomTheme

@Composable
fun UnaRoomApp() {
    UnaRoomTheme {
        AppNavHost()
    }
}
