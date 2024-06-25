package com.example.cinelibbb.presentation.details

import com.example.cinelibbb.domain.model.Movie

data class DetailsState(
    val isLoading: Boolean = false,
    val movie: Movie? = null
)
