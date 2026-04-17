package com.moviles.unaroom

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.moviles.unaroom.navigation.AppNavHost
import com.moviles.unaroom.ui.theme.UnaRoomTheme

@Composable
fun UnaRoomApp() {
    UnaRoomTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            AppNavHost(innerPadding = innerPadding)
        }
    }
}

