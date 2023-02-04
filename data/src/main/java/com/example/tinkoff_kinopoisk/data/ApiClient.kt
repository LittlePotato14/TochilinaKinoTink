package com.example.tinkoff_kinopoisk.data

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Client for network
 */
internal object ApiClient {
    private const val BASE_URL = "https://kinopoiskapiunofficial.tech/api/"
    private const val VERSION21 = "v2.1/"
    private const val VERSION22 = "v2.2/"

    val instanceVersion21: KinopoiskApi by lazy{
        val gson = GsonBuilder().create()
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BASE_URL + VERSION21)
            .build()
        retrofit.create(KinopoiskApi::class.java)
    }

    val instanceVersion22: KinopoiskApi by lazy{
        val gson = GsonBuilder().create()
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BASE_URL + VERSION22)
            .build()
        retrofit.create(KinopoiskApi::class.java)
    }
}