package com.example.cinelibbb.data.dependencyinjection

import android.app.Application
import androidx.room.Room
import com.example.cinelibbb.data.local.MovieDatabase
import com.example.cinelibbb.data.remote.MovieApi
import com.example.cinelibbb.data.remote.OmdbMovieAPI
import com.example.cinelibbb.data.repository.OmdbMovieRepositoryImpl
import com.example.cinelibbb.domain.repository.OmdbMovieRepository
import com.example.cinelibbb.util.Constants.BASE_URL
import com.example.cinelibbb.util.Constants.OMDBBASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()

    @Provides
    @Singleton
    fun providesMovieApi() : MovieApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(client)
            .build()
            .create(MovieApi::class.java)
    }

    @Provides
    @Singleton
    fun providesMovieDatabase(app: Application): MovieDatabase {
        return Room.databaseBuilder(
            app,
            MovieDatabase::class.java,
            "moviedb.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMovieApi() : OmdbMovieAPI {
        return Retrofit.Builder()
            .baseUrl(OMDBBASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OmdbMovieAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieRepository(api : OmdbMovieAPI) : OmdbMovieRepository {
        return OmdbMovieRepositoryImpl(api = api)
    }
}





