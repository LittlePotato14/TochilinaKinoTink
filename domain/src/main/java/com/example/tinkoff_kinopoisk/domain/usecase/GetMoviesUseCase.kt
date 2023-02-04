package com.example.tinkoff_kinopoisk.domain.usecase

import com.example.tinkoff_kinopoisk.domain.models.Movie
import com.example.tinkoff_kinopoisk.domain.models.Top100Response
import com.example.tinkoff_kinopoisk.domain.repository.MovieRepository

class GetPopularMoviesUseCase(
    private val movieRepository: MovieRepository
) {
    suspend fun execute(page: Int): Result<Top100Response> {
        return movieRepository.getPopularMovies(page)
    }
}