package com.example.tinkoff_kinopoisk.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tinkoff_kinopoisk.domain.models.Country
import com.example.tinkoff_kinopoisk.domain.models.ExtendedMovie
import com.example.tinkoff_kinopoisk.domain.models.Genre
import com.example.tinkoff_kinopoisk.domain.models.Movie

class MyDatabaseHelper(private val context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        const val DATABASE_NAME = "Movies.db"
        const val DATABASE_VERSION = 1

        const val TABLE_NAME = "favourite_movies"
        const val COLUMN_ID = "_id"
        const val COLUMN_KINOPOISK_ID = "kinopoisk_id"
        const val COLUMN_TITLE = "movie_title"
        const val COLUMN_YEAR = "movie_year"
        const val COLUMN_GENRES = "movie_genres"
        const val COLUMN_COUNTRIES = "movie_countries"
        const val COLUMN_DESCRIPTION = "movie_description"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val query = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_KINOPOISK_ID INTEGER, $COLUMN_TITLE TEXT, $COLUMN_YEAR INTEGER, $COLUMN_GENRES TEXT, $COLUMN_COUNTRIES TEXT, $COLUMN_DESCRIPTION TEXT);"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.let{
            it.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(it)
        }
    }

    fun addMovieToFavourites(movie: Movie, description: String): Boolean{
        val db = writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_KINOPOISK_ID, movie.filmId)
        cv.put(COLUMN_TITLE, movie.nameRu)
        cv.put(COLUMN_YEAR, movie.year)
        cv.put(COLUMN_GENRES, movie.genres.joinToString(", ", transform = {it.genre}))
        cv.put(COLUMN_COUNTRIES, movie.countries.joinToString(", ", transform = {it.country}))
        cv.put(COLUMN_DESCRIPTION, description)

        return db.insert(TABLE_NAME, null, cv) != (-1).toLong()
    }

    fun getAllSavedMovies(): Cursor?{
        val query = "SELECT * FROM $TABLE_NAME"
        val db = readableDatabase

        var cursor: Cursor? = null
        if(db != null)
            cursor = db.rawQuery(query, null)
        return cursor
    }
}