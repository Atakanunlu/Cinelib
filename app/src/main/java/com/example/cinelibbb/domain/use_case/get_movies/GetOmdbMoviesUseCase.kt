package com.example.cinelibbb.domain.use_case.get_movies

import com.example.cinelibbb.data.remote.dto.toOmdbMovieList
import com.example.cinelibbb.domain.model.OmdbMovie
import com.example.cinelibbb.domain.repository.OmdbMovieRepository

import com.example.cinelibbb.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOError
import javax.inject.Inject

class GetOmdbMoviesUseCase @Inject constructor(private val repository : OmdbMovieRepository) {

    fun executeGetOmdbMovies(search: String) : Flow<Resource<List<OmdbMovie>>> = flow {
        try {
            emit(Resource.Loading())
            val omdbMovieList = repository.getOmdbMovies(search)
            if(omdbMovieList.Response.equals("True")) {
                emit(Resource.Success(omdbMovieList.toOmdbMovieList()))
            } else {
                emit(Resource.Error(message = "Film Bulunamadı!"))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(message = e.localizedMessage ?: "Hata oluştu"))
        } catch (e: IOError) {
            emit(Resource.Error(message = "Internet Yok!"))
        }
    }

}