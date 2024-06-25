package com.example.cinelibbb.presentation.movies

import com.example.cinelibbb.domain.model.OmdbMovie

data class OmdbMoviesState(
    val isLoading : Boolean = false,
    val movies : List<OmdbMovie> = emptyList(),
    val error : String = "",
    val search : String = "godfather"
)