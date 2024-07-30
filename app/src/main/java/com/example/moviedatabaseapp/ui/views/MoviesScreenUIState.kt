package com.example.moviedatabaseapp.ui.views

import androidx.annotation.StringRes
import com.example.moviedatabaseapp.R
import com.example.moviedatabaseapp.models.Movie

data class MoviesScreenUIState(
    @StringRes
    val title: Int = R.string.movieDescription,
    val moviesList: List<Movie>
)
