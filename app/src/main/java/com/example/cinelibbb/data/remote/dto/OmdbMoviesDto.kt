package com.example.cinelibbb.data.remote.dto

import com.example.cinelibbb.domain.model.OmdbMovie

data class OmdbMoviesDto(
    val Response: String,
    val Search: List<Search>,
    val totalResults: String
)

fun OmdbMoviesDto.toOmdbMovieList(): List<OmdbMovie> {
    return Search.map { search ->
        OmdbMovie(
            search.Poster,
            search.Title,
            search.Year,
            search.imdbID,
            search.imdbRating
        )
    }
}