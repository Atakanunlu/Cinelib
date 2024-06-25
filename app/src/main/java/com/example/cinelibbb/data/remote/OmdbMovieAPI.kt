package com.example.cinelibbb.data.remote

import com.example.cinelibbb.data.remote.dto.OmdbMovieDetailDto
import com.example.cinelibbb.data.remote.dto.OmdbMoviesDto
import com.example.cinelibbb.util.Constants.OMDBAPI_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbMovieAPI {

    @GET(".")
    suspend fun getOmdbMovies(
        @Query("s") searchString :String,
        @Query("apikey") apiKey :String = OMDBAPI_KEY
    ) : OmdbMoviesDto

    @GET(".")
    suspend fun getOmdbMovieDetail(
        @Query("i") imdbId : String,
        @Query("apikey") apiKey: String = OMDBAPI_KEY
    ) : OmdbMovieDetailDto

}