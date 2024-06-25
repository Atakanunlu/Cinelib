package com.example.cinelibbb.domain.use_case.get_movie_detail

import com.example.cinelibbb.data.remote.dto.toOmdbMovieDetail
import com.example.cinelibbb.domain.model.OmdbMovieDetail
import com.example.cinelibbb.domain.repository.OmdbMovieRepository
import com.example.cinelibbb.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOError
import javax.inject.Inject

class GetOmdbMovieDetailsUseCase @Inject constructor(private val repository : OmdbMovieRepository) {


    fun executeGetOmdbMovieDetails(imdbId: String) : Flow<Resource<OmdbMovieDetail>> = flow {
        try {
            emit(Resource.Loading())
            val omdbMovieDetail = repository.getOmdbMovieDetail(imdbId = imdbId).toOmdbMovieDetail()
            emit(Resource.Success(omdbMovieDetail))
        } catch (e: HttpException) {
            emit(Resource.Error(message = e.localizedMessage ?: "Hata oluştu."))
        } catch (e: IOError) {
            emit(Resource.Error(message = "Internet Yok"))
        }
    }

}