package com.example.tinkoff_kinopoisk.presentation.viewsModels

import android.app.Application
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.tinkoff_kinopoisk.core.ImageConverter
import com.example.tinkoff_kinopoisk.data.MyDatabaseHelper
import com.example.tinkoff_kinopoisk.domain.models.Country
import com.example.tinkoff_kinopoisk.domain.models.Genre
import com.example.tinkoff_kinopoisk.domain.models.Movie
import com.example.tinkoff_kinopoisk.domain.usecase.GetMovieInformationUseCase
import com.example.tinkoff_kinopoisk.domain.usecase.GetMoviesByKeywordUseCase
import com.example.tinkoff_kinopoisk.domain.usecase.GetPopularMoviesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(val app: Application) : AndroidViewModel(app) {

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies
    var isSearch: Boolean = false
    private var page = 1
    private var pagesCount = 0
    private val savedMovies = mutableListOf<Movie>()

    private val _toggleFavouriteMovie = MutableLiveData<ToggleFavouriteMovie>()
    val toggleFavouriteMovie: LiveData<ToggleFavouriteMovie> = _toggleFavouriteMovie

    val toggleInFavourites: (Movie, Int) -> Unit = { it1, it2 ->
        val myDB = MyDatabaseHelper(app)
        if (it1.isFavourite != true) {
            val useCase =
                GetMovieInformationUseCase(com.example.tinkoff_kinopoisk.data.MovieRepository())

            viewModelScope.launch {
                val result = useCase.execute(it1.filmId)

                val movieResult = result.getOrNull()

                if (movieResult != null) {
                    withContext(Dispatchers.IO) {

                        try {
                            val poster = Glide.with(app)
                                .asBitmap()
                                .load(movieResult.posterUrl)
                                .submit()
                                .get()

                            val posterPreview = Glide.with(app)
                                .asBitmap()
                                .load(it1.posterUrlPreview)
                                .submit()
                                .get()

                            withContext(Dispatchers.Main) {
                                if (result.isSuccess && myDB.addMovieToFavourites(
                                        it1,
                                        movieResult.description,
                                        ImageConverter.bitmapToByteArray(poster),
                                        ImageConverter.bitmapToByteArray(posterPreview),
                                    )
                                )
                                    _toggleFavouriteMovie.value = ToggleFavouriteMovie(
                                        it2,
                                        success = true,
                                        isSaving = true
                                    )
                                else
                                    _toggleFavouriteMovie.value = ToggleFavouriteMovie(
                                        it2,
                                        success = false,
                                        isSaving = true
                                    )
                            }
                        } catch (e: java.lang.Exception) {
                            print(e.message)
                        }
                    }
                }
            }
        } else {
            viewModelScope.launch {
                withContext(Dispatchers.Main) {
                    if (myDB.removeMovieFromFavourites(it1))
                        _toggleFavouriteMovie.value = ToggleFavouriteMovie(
                            it2,
                            success = true,
                            isSaving = false
                        )
                    else
                        _toggleFavouriteMovie.value = ToggleFavouriteMovie(
                            it2,
                            success = false,
                            isSaving = false
                        )

                }
            }
        }
    }

    init {
        _movies.value = listOf()
    }

    fun getMovies() {
        val useCase = GetPopularMoviesUseCase(com.example.tinkoff_kinopoisk.data.MovieRepository())

        viewModelScope.launch {
            val result = useCase.execute(page)

            withContext(Dispatchers.Main) {
                if (result.isSuccess)
                    result.getOrNull()?.let {

                        // get saved to favourites
                        val myDBHelper = MyDatabaseHelper(app)
                        val cursor = myDBHelper.getAllSavedMovies()
                        if (cursor != null)
                            while (cursor.moveToNext())
                                savedMovies.add(
                                    Movie(
                                        cursor.getInt(1),
                                        cursor.getString(2),
                                        "",
                                        cursor.getInt(3),
                                        cursor.getString(4).split(", ").map { Genre(it) },
                                        cursor.getString(5).split(", ").map { Country(it) },
                                        true,
                                        cursor.getBlob(7))
                                )

                        // check for favourites
                        it.films.forEach { i ->
                            i.isFavourite = findInFavourites(i.filmId)
                        }

                        // update movies list
                        _movies.value = it.films
                        pagesCount = it.pagesCount
                    }
                else if (result.exceptionOrNull() != null)
                    Toast.makeText(
                        getApplication(),
                        result.exceptionOrNull()?.message,
                        Toast.LENGTH_LONG
                    ).show()
            }
        }
    }

    fun getMovies(query: String, isNewSearch: Boolean?) {
        if (isNewSearch != null) {
            isSearch = isNewSearch
        }
        val useCase = GetMoviesByKeywordUseCase(com.example.tinkoff_kinopoisk.data.MovieRepository())

        viewModelScope.launch {
            val result = useCase.execute(query, page)

            withContext(Dispatchers.Main) {
                if (result.isSuccess)
                    result.getOrNull()?.let {

                        // get saved to favourites
                        val myDBHelper = MyDatabaseHelper(app)
                        val cursor = myDBHelper.getAllSavedMovies()
                        if (cursor != null)
                            while (cursor.moveToNext())
                                savedMovies.add(
                                    Movie(
                                        cursor.getInt(1),
                                        cursor.getString(2),
                                        "",
                                        cursor.getInt(3),
                                        cursor.getString(4).split(", ").map { Genre(it) },
                                        cursor.getString(5).split(", ").map { Country(it) },
                                        true,
                                        cursor.getBlob(7))
                                )

                        // check for favourites
                        it.films.forEach { i ->
                            i.isFavourite = findInFavourites(i.filmId)
                        }

                        // update movies list
                        _movies.value = it.films
                        pagesCount = it.pagesCount
                    }
                else if (result.exceptionOrNull() != null)
                    Toast.makeText(
                        getApplication(),
                        result.exceptionOrNull()?.message,
                        Toast.LENGTH_LONG
                    ).show()
            }
        }
    }

    private fun findInFavourites(movieId: Int): Boolean {
        savedMovies.forEach { i ->
            if (i.filmId == movieId)
                return true
        }
        return false
    }

    fun getNextPagePopularMovies() {
        if (page + 1 <= pagesCount) {
            page++
            getMovies()
        }
    }

    fun getNextPageSearchMovies(query: String) {
        if (page + 1 <= pagesCount) {
            page++
            getMovies(query, false)
        }
    }

}

data class ToggleFavouriteMovie(
    var position: Int,
    var success: Boolean,
    var isSaving: Boolean
)