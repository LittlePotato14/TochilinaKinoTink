package com.example.tinkoff_kinopoisk.domain.models

import com.example.tinkoff_kinopoisk.domain.models.Movie

data class Top100Response (
    val films: List<Movie>,
    val pagesCount: Int
)