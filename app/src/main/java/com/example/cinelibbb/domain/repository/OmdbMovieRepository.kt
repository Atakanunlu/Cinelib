package com.example.cinelibbb.domain.repository

import com.example.cinelibbb.data.remote.dto.OmdbMovieDetailDto
import com.example.cinelibbb.data.remote.dto.OmdbMoviesDto

interface OmdbMovieRepository {

    suspend fun getOmdbMovies(search : String) : OmdbMoviesDto

    suspend fun getOmdbMovieDetail(imdbId : String) : OmdbMovieDetailDto


}