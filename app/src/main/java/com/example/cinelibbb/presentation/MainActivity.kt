package com.example.cinelibbb.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.cinelibbb.presentation.details.DetailsScreen
import com.example.cinelibbb.presentation.movie_detail.views.OmdbMovieDetailScreen
import com.example.cinelibbb.presentation.movielist.views.HomeScreen
import com.example.cinelibbb.presentation.movies.views.OmdbMovieScreen
import com.example.cinelibbb.presentation.splash.SplashScreen
import com.example.cinelibbb.presentation.ui.theme.CineLibbbTheme
import com.example.cinelibbb.util.Constants.IMDB_ID
import com.example.cinelibbb.util.Screen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CineLibbbTheme {
                SetBarColor(color = Color(0xFF333333))
                Surface(color = MaterialTheme.colorScheme.background) {

                    val navController = rememberNavController()

                    NavHost(navController = navController,
                        startDestination = Screen.OmdbMovieScreen.route
                    ) {
                        composable(route = Screen.OmdbMovieScreen.route) {
                            OmdbMovieScreen(navController = navController)
                        }
                        composable(route = Screen.OmdbMovieDetailScreen.route+"/{${IMDB_ID}}") {
                            OmdbMovieDetailScreen(navController = navController)
                        }
                        composable(Screen.Home.route) {
                            HomeScreen(navController)
                        }

                        composable(
                            Screen.Details.route + "/{movieId}",
                            arguments = listOf(
                                navArgument("movieId") { type = NavType.IntType }
                            )
                        ) { backStackEntry ->
                            DetailsScreen(navController = navController)
                        }

                        composable(Screen.SplashScreen.route){
                            SplashScreen(navController = navController)
                        }

                    }


                }
            }
        }
    }
}

@Composable
private fun SetBarColor(color: Color){
    val systemUiController = rememberSystemUiController()
    LaunchedEffect(key1 = color) {
        systemUiController.setSystemBarsColor(color)
    }
}



