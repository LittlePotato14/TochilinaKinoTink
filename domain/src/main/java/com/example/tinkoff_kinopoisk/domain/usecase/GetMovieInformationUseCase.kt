package com.example.tinkoff_kinopoisk.domain.usecase

import com.example.tinkoff_kinopoisk.domain.models.ExtendedMovie
import com.example.tinkoff_kinopoisk.domain.repository.MovieRepository

class GetMovieInformationUseCase (private val movieRepository: MovieRepository) {
    suspend fun execute(movieId: Int): Result<ExtendedMovie> {
        return movieRepository.getMovieInformation(movieId)
    }
}