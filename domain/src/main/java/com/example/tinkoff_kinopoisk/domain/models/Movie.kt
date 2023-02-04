package com.example.tinkoff_kinopoisk.domain.models

data class Movie(
    val filmId: Int,
    val nameRu: String,
    val posterUrlPreview: String,
    var year: Int,
    val genres: List<Genre>,
    val countries: List<Country>
)

data class ExtendedMovie(
    val nameRu: String,
    val posterUrl: String,
    val description: String,
    val genres: List<Genre>,
    val countries: List<Country>
)

data class Genre(
    val genre: String
)

data class Country(
    val country: String
)