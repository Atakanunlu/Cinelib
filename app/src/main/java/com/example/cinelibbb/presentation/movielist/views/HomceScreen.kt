package com.example.cinelibbb.presentation.movielist.views

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Movie
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Upcoming
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cinelibbb.R
import com.example.cinelibbb.presentation.movielist.MovieListUiEvent
import com.example.cinelibbb.presentation.movielist.MovieListViewModel
import com.example.cinelibbb.util.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val movieListViewModel = hiltViewModel<MovieListViewModel>()
    val movieListState = movieListViewModel.movieListState.collectAsState().value
    val bottomNavController = rememberNavController()
    val selected = rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                bottomNavController = bottomNavController,
                onEvent = movieListViewModel::onEvent,
                mainNavController = navController,
                selected = selected
            )
        },
        topBar = {
            TopAppBar(
                title = {
                    val titleRes = when (selected.intValue) {
                        0 -> R.string.popular_movies
                        1 -> R.string.upcoming_movies
                        else -> R.string.app_name // Veya varsayılan bir başlık
                    }
                    Text(
                        text = stringResource(id = titleRes),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                modifier = Modifier.shadow(2.dp),
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    Color(0xFF333333),
                )
            )
        },
        containerColor = Color.Black
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            NavHost(
                navController = bottomNavController,
                startDestination = Screen.PopularMovieList.route
            ) {
                composable(Screen.PopularMovieList.route) {
                    PopularMoviesScreen(
                        navController = navController,
                        movieListState = movieListState,
                        onEvent = movieListViewModel::onEvent
                    )
                }
                composable(Screen.UpcomingMovieList.route) {
                    UpcomingMoviesScreen(
                        navController = navController,
                        movieListState = movieListState,
                        onEvent = movieListViewModel::onEvent
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    bottomNavController: NavHostController,
    onEvent: (MovieListUiEvent) -> Unit,
    mainNavController: NavHostController,
    selected: MutableState<Int>
) {
    val items = listOf(
        BottomItem(
            title = stringResource(R.string.popular),
            icon = Icons.Rounded.Movie,
            route = Screen.PopularMovieList.route
        ),
        BottomItem(
            title = stringResource(R.string.upcoming),
            icon = Icons.Rounded.Upcoming,
            route = Screen.UpcomingMovieList.route
        ),
        BottomItem(
            title = stringResource(R.string.search),
            icon = Icons.Rounded.Search,
            route = Screen.OmdbMovieScreen.route
        )
    )

    NavigationBar(
        containerColor = Color(0xFF333333)
    ) {
        items.forEachIndexed { index, bottomItem ->
            NavigationBarItem(
                selected = selected.value == index,
                onClick = {
                    selected.value = index
                    when (bottomItem.route) {
                        Screen.PopularMovieList.route -> {
                            onEvent(MovieListUiEvent.Navigate)
                            bottomNavController.navigate(bottomItem.route) {
                                popUpTo(bottomItem.route) { inclusive = true }
                            }
                        }
                        Screen.UpcomingMovieList.route -> {
                            onEvent(MovieListUiEvent.Navigate)
                            bottomNavController.navigate(bottomItem.route) {
                                popUpTo(bottomItem.route) { inclusive = true }
                            }
                        }
                        Screen.OmdbMovieScreen.route -> {
                            mainNavController.navigate(bottomItem.route)
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = bottomItem.icon,
                        contentDescription = bottomItem.title,
                        tint = Color.White
                    )
                },
                label = {
                    Text(
                        text = bottomItem.title,
                        color = Color.White
                    )
                }
            )
        }
    }
}

data class BottomItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)