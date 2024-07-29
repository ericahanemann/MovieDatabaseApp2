package com.example.moviedatabaseapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.moviedatabaseapp.R
import com.example.moviedatabaseapp.data.Movie
import com.example.moviedatabaseapp.network.MovieDatabaseApi
import com.example.moviedatabaseapp.ui.views.AppScreens
import com.example.moviedatabaseapp.ui.views.MainScreenUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface MoviesUIState {

    object Loading : MoviesUIState
    data class Success(val movies: List<Movie>) : MoviesUIState
    object Error : MoviesUIState
}

class MoviesViewModel : ViewModel() {

    private var _uiState: MutableStateFlow<MoviesUIState> = MutableStateFlow(MoviesUIState.Loading)
    val uiState: StateFlow<MoviesUIState> = _uiState.asStateFlow()

    private val _appUIState: MutableStateFlow<MainScreenUIState> = MutableStateFlow(MainScreenUIState())

    val MainScreenUIState: StateFlow<MainScreenUIState> = _appUIState.asStateFlow()

    init {
        getPopularMovies()
    }

    private fun getPopularMovies() {
        viewModelScope.launch {
            try {
                val apiKey = "712cf47cf9731a61bcca19b09c67d862"
                Log.d("MoviesViewModel", "Fetching popular movies with API key: $apiKey")
                val response = MovieDatabaseApi.retrofitService.getPopularMovies(apiKey)
                Log.d("MoviesViewModel", "Fetched ${response.results.size} movies")
                _uiState.value = MoviesUIState.Success(response.results)

            } catch (e: IOException) {
                _uiState.value = MoviesUIState.Error

            } catch (e: retrofit2.HttpException) {
                _uiState.value = MoviesUIState.Error
            }
        }
    }

    fun navigate(movie: Movie, navController: NavController) {
        _appUIState.update { currentState ->
            currentState.copy(
                title = R.string.MovieDescription,
            )
        }
        navController.navigate("${AppScreens.MovieDescription.name}/${movie.id}")

        _appUIState.update {
            MainScreenUIState()
        }

        // --- navega de volta a tela inicial ---
//        navController.navigate(AppScreens.MoviesScreen.name) {
//            popUpTo(AppScreens.MoviesScreen.name) {
//                inclusive = true
//            }
//        }
    }

    fun navigateBack(navController: NavController) {
        _appUIState.update {
            MainScreenUIState()
        }
        navController.popBackStack()
    }
}