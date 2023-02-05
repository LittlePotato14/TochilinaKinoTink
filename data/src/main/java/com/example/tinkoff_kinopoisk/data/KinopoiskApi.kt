package com.example.tinkoff_kinopoisk.data

import com.example.tinkoff_kinopoisk.domain.models.ExtendedMovie
import com.example.tinkoff_kinopoisk.domain.models.FilmsResponse
import com.example.tinkoff_kinopoisk.domain.models.Top100Response
import retrofit2.Response
import retrofit2.http.*

/**
 * API for app network operations
 */
internal interface KinopoiskApi {

    @GET("films/top")
    suspend fun getPopularMovies(
        @Header ("X-API-KEY") apiKey: String,
        @Query("type") type: String,
        @Query("page") page: Int
    ): Response<Top100Response>

    @GET("films/{id}")
    suspend fun getMovieInfo(
        @Header ("X-API-KEY") apiKey: String,
        @Path("id") movieId: Int
    ): Response<ExtendedMovie>


    @GET("films/search-by-keyword")
    suspend fun searchMoviesByKeyword(
        @Header ("X-API-KEY") apiKey: String,
        @Query("keyword") keyword: String,
        @Query("page") page: Int
    ): Response<FilmsResponse>
}