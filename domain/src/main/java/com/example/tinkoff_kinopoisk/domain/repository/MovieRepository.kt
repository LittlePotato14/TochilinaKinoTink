package com.example.tinkoff_kinopoisk.domain.repository

import com.example.tinkoff_kinopoisk.domain.models.ExtendedMovie
import com.example.tinkoff_kinopoisk.domain.models.Top100Response

/**
 * Interface for interacting with user data
 */
interface MovieRepository {
    suspend fun getPopularMovies(page: Int): Result<Top100Response>
    suspend fun getMovieInformation(movieId: Int): Result<ExtendedMovie>
    //ToDo suspend fun searchMovie(keyString: String): Result<List<Movie>>
}