package com.example.tinkoff_kinopoisk.domain.usecase

import com.example.tinkoff_kinopoisk.domain.models.FilmsResponse
import com.example.tinkoff_kinopoisk.domain.models.Movie
import com.example.tinkoff_kinopoisk.domain.models.Top100Response
import com.example.tinkoff_kinopoisk.domain.repository.MovieRepository

class GetMoviesByKeywordUseCase(private val movieRepository: MovieRepository) {
    suspend fun execute(keyword: String, page: Int): Result<FilmsResponse> {
        return movieRepository.getMoviesByKeyword(keyword, page)
    }
}