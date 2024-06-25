package com.example.cinelibbb.data.repository

import com.example.cinelibbb.data.remote.OmdbMovieAPI
import com.example.cinelibbb.data.remote.dto.OmdbMovieDetailDto
import com.example.cinelibbb.data.remote.dto.OmdbMoviesDto
import com.example.cinelibbb.domain.repository.OmdbMovieRepository
import javax.inject.Inject

class OmdbMovieRepositoryImpl @Inject constructor(private val api : OmdbMovieAPI) :
    OmdbMovieRepository {
    override suspend fun getOmdbMovies(search: String): OmdbMoviesDto {
        return api.getOmdbMovies(searchString = search)
    }
    override suspend fun getOmdbMovieDetail(imdbId: String): OmdbMovieDetailDto {
        return api.getOmdbMovieDetail(imdbId = imdbId)
    }
}