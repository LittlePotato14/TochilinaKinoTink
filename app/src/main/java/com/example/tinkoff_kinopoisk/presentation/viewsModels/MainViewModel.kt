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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>> = _movies

    init {
        getMovies()
    }

    fun addToFavourites(recyclerItemId: Int){
        //ToDo
    }

    fun getMovies(){
        _movies.value = listOf(
            Movie(0, "Звездные войники", "", "", 2002, listOf(Genre("Фантастика"), Genre("Боевик")), listOf(Country("USA"))),
            Movie(0, "Звездные войники2", "", "", 2002, listOf(Genre("Фантастика"), Genre("Боевик")), listOf(Country("USA"))),
            Movie(0, "Звездные войники3", "", "", 2002, listOf(Genre("Фантастика"), Genre("Боевик")), listOf(Country("USA"))),
            Movie(0, "Звездные войники4", "", "", 2002, listOf(Genre("Фантастика"), Genre("Боевик")), listOf(Country("USA"))),
            Movie(0, "Звездные войники5", "", "", 2002, listOf(), listOf(Country("USA"))),
            )
        /*val useCase = GetEventsUseCase(UserRepository())

        mSettings = (getApplication() as Context).getSharedPreferences(MySettings.APP_PREFERENCES, AppCompatActivity.MODE_PRIVATE)

        val jwt = mSettings.getString(MySettings.APP_PREFERENCES_TOKEN, "") ?: ""

        viewModelScope.launch {
            val result = useCase.execute(jwt)

            withContext(Dispatchers.Main) {
                if (result.isSuccess)
                    _events.value = result.getOrNull()
                else if(result.exceptionOrNull() != null)
                    Toast.makeText(getApplication(), (result.exceptionOrNull() as Exception).toMessage(getApplication()), Toast.LENGTH_LONG).show()
            }
        }*/
    }

}