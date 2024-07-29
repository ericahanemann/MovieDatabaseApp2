package com.example.moviedatabaseapp.data

import com.squareup.moshi.Json

data class Movie(
    val id: Int,
    val title: String,
    val releaseDate: String?,  //opcional
    val overview: String,
    @Json(name = "vote_average")
    val evaluationNote: Double,
    val runtime: Int?,        // opcional
    @Json(name = "original_language")
    val language: String,
    @Json(name = "poster_path")
    val poster: String
)
