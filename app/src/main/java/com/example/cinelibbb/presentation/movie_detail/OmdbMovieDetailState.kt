package com.example.cinelibbb.presentation.movie_detail

import com.example.cinelibbb.domain.model.OmdbMovieDetail

data class OmdbMovieDetailState(
    val isLoading : Boolean = false,
    val movie : OmdbMovieDetail? = null,
    val error : String = ""
)