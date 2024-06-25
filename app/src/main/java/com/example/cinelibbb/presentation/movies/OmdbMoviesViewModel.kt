package com.example.cinelibbb.presentation.movies

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinelibbb.domain.use_case.get_movies.GetOmdbMoviesUseCase

import com.example.cinelibbb.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class OmdbMoviesViewModel @Inject constructor(
    private val getOmdbMoviesUseCase: GetOmdbMoviesUseCase
) : ViewModel() {

    private val _state = mutableStateOf<OmdbMoviesState>(OmdbMoviesState())
    val state : State<OmdbMoviesState> = _state

    private var job : Job? = null

    init {
        getMovies(_state.value.search)
    }

    private fun getMovies(search: String) {
        job?.cancel()
        job = getOmdbMoviesUseCase.executeGetOmdbMovies(search).onEach {
            when (it) {
                is Resource.Success -> {
                    _state.value = OmdbMoviesState(movies = it.data ?: emptyList())
                }

                is Resource.Error -> {
                    _state.value = OmdbMoviesState(error = it.message ?: "Hata!")
                }

                is Resource.Loading -> {
                    _state.value = OmdbMoviesState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event : OmdbMoviesEvent) {
        when(event) {
            is OmdbMoviesEvent.Search -> {
                getMovies(event.searchString)
            }
        }
    }

}