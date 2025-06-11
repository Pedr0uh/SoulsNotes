package com.example.soulsnotes

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    AnimatedNavHost(
        navController = navController,
        startDestination = "splash",
    ) {
        composable(
            route = "splash",
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            SplashScreen(navController)
        }
        composable(
            route = "inicial",
            enterTransition = {
                val from = initialState.destination.route
                if (from == "splash") EnterTransition.None else fadeIn()
            },
            exitTransition = { fadeOut() }
        ) {
            InicialScreen(navController)
        }
        composable(
            route = "home",
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            HomeScreen(navController)
        }
        composable(
            route = "undertaleSonds",
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            UndertaleSondsScreen(navController)
        }
        composable(
            route = "deltaruneSonds",
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() }
        ) {
            DeltaruneSondsScreen(navController)
        }
    }
}