package com.example.cinelibbb.presentation.movie_detail


import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinelibbb.domain.use_case.get_movie_detail.GetOmdbMovieDetailsUseCase
import com.example.cinelibbb.util.Constants.IMDB_ID
import com.example.cinelibbb.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
@HiltViewModel
class OmdbMovieDetailViewModel @Inject constructor(
    private val getOmdbMovieDetailsUseCase: GetOmdbMovieDetailsUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf<OmdbMovieDetailState>(OmdbMovieDetailState())
    val state : State<OmdbMovieDetailState> = _state

    init {
        savedStateHandle.get<String>(IMDB_ID)?.let {
            getOmdbMovieDetail(it)
        }
    }

    private fun getOmdbMovieDetail(imdbId: String) {
        getOmdbMovieDetailsUseCase.executeGetOmdbMovieDetails(imdbId = imdbId).onEach {
            when (it) {
                is Resource.Success -> {
                    _state.value = OmdbMovieDetailState(movie = it.data)
                }

                is Resource.Error -> {
                    _state.value = OmdbMovieDetailState(error = it.message ?: "Hata!")

                }

                is Resource.Loading -> {
                    _state.value = OmdbMovieDetailState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

}