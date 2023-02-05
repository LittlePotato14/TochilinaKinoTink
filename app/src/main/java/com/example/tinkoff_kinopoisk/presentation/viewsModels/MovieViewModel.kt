package com.example.tinkoff_kinopoisk.presentation.viewsModels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tinkoff_kinopoisk.data.MovieRepository
import com.example.tinkoff_kinopoisk.data.MyDatabaseHelper
import com.example.tinkoff_kinopoisk.domain.models.Country
import com.example.tinkoff_kinopoisk.domain.models.ExtendedMovie
import com.example.tinkoff_kinopoisk.domain.models.Genre
import com.example.tinkoff_kinopoisk.domain.models.Movie
import com.example.tinkoff_kinopoisk.domain.usecase.GetMovieInformationUseCase
import com.example.tinkoff_kinopoisk.domain.usecase.GetPopularMoviesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


internal class MovieViewModel(val app: Application) : AndroidViewModel(app) {
    private val _movie = MutableLiveData<ExtendedMovie>()
    val movie: LiveData<ExtendedMovie> = _movie

    fun getMovieInfo(movieId: Int) {
        val useCase = GetMovieInformationUseCase(MovieRepository())

        viewModelScope.launch {
            val result = useCase.execute(movieId)

            withContext(Dispatchers.Main) {
                if (result.isSuccess)
                    result.getOrNull()?.let {
                        _movie.value = it
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

    fun getMovieByIdFromDb(kinopoiskId: Int) {
        // get saved to favourites
        val myDBHelper = MyDatabaseHelper(app)
        val cursor = myDBHelper.getMovieById(kinopoiskId)
        if (cursor != null)
            while (cursor.moveToNext())
                _movie.value = ExtendedMovie(
                    cursor.getString(2),
                    "",
                    cursor.getString(6),
                    cursor.getString(4).split(", ").map { Genre(it) },
                    cursor.getString(5).split(", ").map { Country(it) },
                    cursor.getBlob(7))
    }
}


