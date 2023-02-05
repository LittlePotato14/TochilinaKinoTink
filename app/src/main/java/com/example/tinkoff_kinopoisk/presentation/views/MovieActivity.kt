package com.example.tinkoff_kinopoisk.presentation.views

import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.tinkoff_kinopoisk.R
import com.example.tinkoff_kinopoisk.databinding.ActivityMovieBinding
import com.example.tinkoff_kinopoisk.presentation.viewsModels.MovieViewModel

class MovieActivity : AppCompatActivity() {
    private var _binding: ActivityMovieBinding? = null
    private val binding get() = _binding!!
    private val movieViewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val b = intent.extras
        b?.let {
            val movieId = it.getInt("movieId");

            movieViewModel.getMovieByIdFromDb(movieId)

            if (movieViewModel.movie.value == null) {
                movieViewModel.getMovieInfo(movieId)
            }
        }

        movieViewModel.movie.observe(this){ movieInfo ->
            if (movieInfo.poster != null) {
                val byteArray = movieInfo.poster
                Glide.with(this)
                    .load(BitmapDrawable(this.resources, BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.color.dark_gray)
                    .into(binding.poster)
            } else {
                Glide.with(this)
                    .load(movieInfo.posterUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.color.dark_gray)
                    .into(binding.poster)
            }

            binding.title.text = movieInfo.nameRu
            binding.description.text = movieInfo.description
            val genres = "<b>" + "Жанры: " + "</b>" + movieInfo.genres.joinToString(", ", transform = {it.genre})
            binding.genres.text = HtmlCompat.fromHtml(genres, HtmlCompat.FROM_HTML_MODE_COMPACT )
            val countries = "<b>" + "Страны: " + "</b>" + movieInfo.countries.joinToString(", ", transform = {it.country})
            binding.countries.text = HtmlCompat.fromHtml(countries, HtmlCompat.FROM_HTML_MODE_COMPACT )
        }
    }
}