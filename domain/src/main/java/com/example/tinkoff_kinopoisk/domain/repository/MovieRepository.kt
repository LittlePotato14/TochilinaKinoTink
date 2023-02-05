package com.example.tinkoff_kinopoisk.domain.repository

import com.example.tinkoff_kinopoisk.domain.models.ExtendedMovie
import com.example.tinkoff_kinopoisk.domain.models.FilmsResponse
import com.example.tinkoff_kinopoisk.domain.models.Top100Response

/**
 * Interface for interacting with user data
 */
interface MovieRepository {
    suspend fun getPopularMovies(page: Int): Result<Top100Response>
    suspend fun getMoviesByKeyword(keyword: String, page: Int): Result<FilmsResponse>
    suspend fun getMovieInformation(movieId: Int): Result<ExtendedMovie>
}