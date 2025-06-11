package com.example.soulsnotes

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("inicial") {
            InicialScreen(navController)
        }
        composable("home") {
            HomeScreen(navController)
        }
        composable("undertaleSonds") {
            UndertaleSondsScreen(navController)
        }
        composable("deltaruneSonds") {
            DeltaruneSondsScreen(navController)
        }

    }
}