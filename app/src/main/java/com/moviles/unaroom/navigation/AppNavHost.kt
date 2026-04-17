package com.moviles.unaroom.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.moviles.unaroom.ui.screens.classrooms.ClassroomsScreen
import com.moviles.unaroom.ui.screens.login.LoginScreen

@Composable
fun AppNavHost(innerPadding: PaddingValues) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppDestinations.LOGIN,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        composable(route = AppDestinations.LOGIN) {
            LoginScreen(
                onLoginClick = {
                    navController.navigate(AppDestinations.CLASSROOMS)
                }
            )
        }

        composable(route = AppDestinations.CLASSROOMS) {
            ClassroomsScreen()
        }
    }
}

