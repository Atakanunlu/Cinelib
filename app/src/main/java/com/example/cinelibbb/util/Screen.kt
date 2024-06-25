package com.example.cinelibbb.util

sealed class Screen (val route:String) {
    object OmdbMovieScreen : Screen("movie_screen")
    object OmdbMovieDetailScreen : Screen("movie_detail_screen")
    object Home : Screen("main")
    object PopularMovieList : Screen("popularMovie")
    object UpcomingMovieList : Screen("upcomingMovie")
    object Details : Screen("details")
    object SplashScreen : Screen("splash")
}
