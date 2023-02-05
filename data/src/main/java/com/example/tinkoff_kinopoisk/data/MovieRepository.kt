package com.example.tinkoff_kinopoisk.data

import com.example.tinkoff_kinopoisk.domain.models.ExtendedMovie
import com.example.tinkoff_kinopoisk.domain.models.FilmsResponse
import com.example.tinkoff_kinopoisk.domain.models.Top100Response
import com.example.tinkoff_kinopoisk.domain.repository.MovieRepository
import kotlinx.coroutines.*
import retrofit2.Response

/**
 * Implementation for user repository to provide operations with user data
 */
class MovieRepository : MovieRepository {
    companion object {
        private const val API_KEY = "e30ffed0-76ab-4dd6-b41f-4c9da2b2735b"
        private const val TOP_100_TYPE = "TOP_100_POPULAR_FILMS"
    }

    override suspend fun getPopularMovies(page: Int): Result<Top100Response> {
        var result: Result<Top100Response>

        withContext(Dispatchers.IO) {
            val response: Response<Top100Response>

            try {
                response = ApiClient.instanceVersion22.getPopularMovies(API_KEY, TOP_100_TYPE, page)
            } catch (e: Exception) {
                result = Result.failure(DataException.InternetException())
                return@withContext
            }

            result = if(!response.isSuccessful)
                Result.failure(DataException.responseCodeToException(response.code()))
            else
                Result.success(response.body()!!)
        }

        return result
    }

    override suspend fun getMoviesByKeyword(keyword: String, page: Int): Result<FilmsResponse> {
        var result: Result<FilmsResponse>

        withContext(Dispatchers.IO) {
            val response: Response<FilmsResponse>

            try {
                response = ApiClient.instanceVersion21.searchMoviesByKeyword(API_KEY, keyword, page)
            } catch (e: Exception) {
                result = Result.failure(DataException.InternetException())
                return@withContext
            }

            result = if(!response.isSuccessful)
                Result.failure(DataException.responseCodeToException(response.code()))
            else
                Result.success(response.body()!!)
        }

        return result
    }

    override suspend fun getMovieInformation(movieId: Int): Result<ExtendedMovie> {
        var result: Result<ExtendedMovie>

        withContext(Dispatchers.IO) {
            val response: Response<ExtendedMovie>

            try {
                response = ApiClient.instanceVersion22.getMovieInfo(API_KEY, movieId)
            } catch (e: Exception) {
                result = Result.failure(DataException.InternetException())
                return@withContext
            }

            result = if(!response.isSuccessful)
                Result.failure(DataException.responseCodeToException(response.code()))
            else
                Result.success(response.body()!!)
        }

        return result
    }
}