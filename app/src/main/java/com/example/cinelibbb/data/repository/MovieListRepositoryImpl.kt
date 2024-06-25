package com.example.cinelibbb.data.repository

import com.example.cinelibbb.data.local.MovieDatabase
import com.example.cinelibbb.data.mappers.toMovie
import com.example.cinelibbb.data.mappers.toMovieEntity
import com.example.cinelibbb.data.remote.MovieApi
import com.example.cinelibbb.domain.model.Movie
import com.example.cinelibbb.domain.repository.MovieListRepository
import com.example.cinelibbb.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MovieListRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDatabase: MovieDatabase
) : MovieListRepository {

    override suspend fun getMovieList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<Resource<List<Movie>>> = flow {
        emit(Resource.Loading(true))

        val localMovieList = movieDatabase.movieDao.getMovieListByCategory(category)
        val shouldLoadLocalMovie = localMovieList.isNotEmpty() && !forceFetchFromRemote

        if (shouldLoadLocalMovie) {
            emit(Resource.Success(localMovieList.map { it.toMovie(category) }))
            emit(Resource.Loading(false))
            return@flow
        }

        try {
            val movieListFromApi = movieApi.getMoviesList(category, page)
            val movieEntities = movieListFromApi.results.map { it.toMovieEntity(category) }

            movieDatabase.movieDao.upsertMovieList(movieEntities)

            emit(Resource.Success(movieEntities.map { it.toMovie(category) }))
        } catch (e: HttpException) {
            e.printStackTrace()
            emit(Resource.Error(message = "Hata oluştu."))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(message = "Hata oluştu."))
        } finally {
            emit(Resource.Loading(false))
        }
    }

    override suspend fun getMovie(id: Int): Flow<Resource<Movie>> = flow {
        emit(Resource.Loading(true))

        val movieEntity = movieDatabase.movieDao.getMovieById(id)

        if (movieEntity != null) {
            emit(Resource.Success(movieEntity.toMovie(movieEntity.category)))
        } else {
            emit(Resource.Error("Hata oluştu."))
        }

        emit(Resource.Loading(false))
    }
}
