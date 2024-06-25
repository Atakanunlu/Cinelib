package com.example.cinelibbb.presentation.movies.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalMovies
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Upcoming
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.cinelibbb.R
import com.example.cinelibbb.domain.model.OmdbMovie
import com.example.cinelibbb.util.Screen
import com.example.cinelibbb.presentation.movies.OmdbMoviesEvent
import com.example.cinelibbb.presentation.movies.OmdbMoviesViewModel

@Composable
fun OmdbMovieScreen(
    navController: NavController,
    omdbViewModel: OmdbMoviesViewModel = hiltViewModel()
) {
    val state = omdbViewModel.state.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            MovieSearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 30.dp),
                hint = "GodFather",
                onSearch = {
                    omdbViewModel.onEvent(OmdbMoviesEvent.Search(it))
                }
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f)
            ) {
                items(state.movies.size) { index ->
                    val omdbMovie = state.movies[index]
                    MovieListRow(omdbMovie = omdbMovie, onItemClick = {
                        navController.navigate(Screen.OmdbMovieDetailScreen.route + "/${omdbMovie.imdbID}")
                    })
                }
            }

            MovieBottomBar(navController = navController, omdbViewModel = omdbViewModel)
        }

        if (state.error.isNotBlank()) {
            Text(
                text = state.error,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieSearchBar(
    modifier: Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {}
) {
    var text by remember { mutableStateOf("") }
    var isHintDisplayed by remember { mutableStateOf(hint != "") }

    Box(modifier = modifier) {
        TextField(
            value = text,
            keyboardActions = KeyboardActions(onDone = {
                onSearch(text)
            }),
            onValueChange = {
                text = it
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.White),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp)
                .onFocusChanged {
                    isHintDisplayed = it.isFocused != true && text.isEmpty()
                }
        )
        if (isHintDisplayed) {
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }
}

@Composable
fun MovieBottomBar(navController: NavController, omdbViewModel: OmdbMoviesViewModel) {
    NavigationBar(
        modifier = Modifier.background(Color(0xFF333333)),
        containerColor = Color(0xFF333333)
    ) {
        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate(Screen.Home.route)
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Movie,
                    contentDescription = "Popular",
                    tint = Color.White
                )
            },
            label = {
                Text(text = stringResource(id = R.string.popular), color = Color.White)
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Blue,
                unselectedIconColor = Color.Blue
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = {
                navController.navigate(Screen.Home.route)
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Upcoming,
                    contentDescription = "Upcoming",
                    tint = Color.White
                )
            },
            label = {
                Text(text = stringResource(id = R.string.upcoming), color = Color.White)
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Blue,
                unselectedIconColor = Color.Blue
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = {
                val randomMovie = selectRandomMovie(omdbViewModel)
                navController.navigate(Screen.OmdbMovieDetailScreen.route + "/${randomMovie.imdbID}")
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.LocalMovies,
                    contentDescription = "Watchlist",
                    tint = Color.White
                )
            },
            label = {
                Text(text = stringResource(id = R.string.watchlist), color = Color.White)
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.Blue,
                unselectedIconColor = Color.Blue
            )
        )
    }
}

fun selectRandomMovie(omdbViewModel: OmdbMoviesViewModel): OmdbMovie {
    val allMovies = omdbViewModel.state.value.movies

    val randomIndex = (0 until allMovies.size).random()
    return allMovies[randomIndex]
}