package com.example.cinelibbb.presentation.movies

sealed class OmdbMoviesEvent {
    data class Search(val searchString :String) : OmdbMoviesEvent()

}
