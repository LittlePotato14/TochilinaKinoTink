package com.example.tinkoff_kinopoisk.presentation.viewsModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tinkoff_kinopoisk.data.MyDatabaseHelper
import com.example.tinkoff_kinopoisk.domain.models.Country
import com.example.tinkoff_kinopoisk.domain.models.ExtendedMovie
import com.example.tinkoff_kinopoisk.domain.models.Genre
import com.example.tinkoff_kinopoisk.domain.models.Movie

internal class FavouritesViewModel(app: Application) : AndroidViewModel(app) {
    private val _extendedMovies = MutableLiveData<List<ExtendedMovie>>()
    val extendedMovies: LiveData<List<ExtendedMovie>> = _extendedMovies

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    init {
        val myDBHelper = MyDatabaseHelper(app)
        val cursor = myDBHelper.getAllSavedMovies()
        if (cursor != null) {
            val listOfMovies = mutableListOf<Movie>()
            val listOfExtendedMovies = mutableListOf<ExtendedMovie>()

            while(cursor.moveToNext()) {
                listOfMovies.add(Movie(cursor.getInt(1), cursor.getString(2), "", cursor.getInt(3), cursor.getString(4).split(", ").map { Genre(it) }, cursor.getString(5).split(", ").map { Country(it) }))
                listOfExtendedMovies.add(ExtendedMovie(cursor.getString(2), "", cursor.getString(6), cursor.getString(4).split(", ").map { Genre(it) }, cursor.getString(5).split(", ").map { Country(it) }))
            }

            _extendedMovies.value = listOfExtendedMovies
            _movies.value = listOfMovies
        }
    }
}