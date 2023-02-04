package com.example.tinkoff_kinopoisk.presentation.viewsModels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tinkoff_kinopoisk.data.MovieRepository
import com.example.tinkoff_kinopoisk.domain.models.ExtendedMovie
import com.example.tinkoff_kinopoisk.domain.usecase.GetMovieInformationUseCase
import com.example.tinkoff_kinopoisk.domain.usecase.GetPopularMoviesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


internal class MovieViewModel(app: Application) : AndroidViewModel(app) {
    private val _movie = MutableLiveData<ExtendedMovie>()
    val movie: LiveData<ExtendedMovie> = _movie

    fun getMovieInfo(movieId: Int){
        val useCase = GetMovieInformationUseCase(MovieRepository())

        viewModelScope.launch {
            val result = useCase.execute(movieId)

            withContext(Dispatchers.Main) {
                if (result.isSuccess)
                    result.getOrNull()?.let{
                        _movie.value = it
                    }
                else if(result.exceptionOrNull() != null)
                    Toast.makeText(getApplication(), result.exceptionOrNull()?.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}


