package com.example.tinkoff_kinopoisk.presentation.viewsModels

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tinkoff_kinopoisk.domain.models.Country
import com.example.tinkoff_kinopoisk.domain.models.Genre
import com.example.tinkoff_kinopoisk.domain.models.Movie
import com.example.tinkoff_kinopoisk.domain.repository.MovieRepository
import com.example.tinkoff_kinopoisk.domain.usecase.GetPopularMoviesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies
    private var page = 1
    private var pagesCount = 0

    init {
        getMovies()
    }

    fun addToFavourites(recyclerItemId: Int){
        //ToDo
    }

    fun getMovies(){
        val useCase = GetPopularMoviesUseCase(com.example.tinkoff_kinopoisk.data.MovieRepository())

        viewModelScope.launch {
            val result = useCase.execute()

            withContext(Dispatchers.Main) {
                if (result.isSuccess)
                    result.getOrNull()?.let{
                        _movies.value = it.films
                        pagesCount = it.pagesCount
                    }
                else if(result.exceptionOrNull() != null)
                    Toast.makeText(getApplication(), result.exceptionOrNull()?.message, Toast.LENGTH_LONG).show()
            }
        }
    }

}