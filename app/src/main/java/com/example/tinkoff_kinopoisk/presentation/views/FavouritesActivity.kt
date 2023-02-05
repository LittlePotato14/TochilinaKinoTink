package com.example.tinkoff_kinopoisk.presentation.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tinkoff_kinopoisk.databinding.ActivityFavouritesBinding
import com.example.tinkoff_kinopoisk.domain.models.Movie
import com.example.tinkoff_kinopoisk.presentation.adapters.MoviesAdapter
import com.example.tinkoff_kinopoisk.presentation.viewsModels.FavouritesViewModel

class FavouritesActivity : AppCompatActivity() {
    private var _binding: ActivityFavouritesBinding? = null
    private val binding get() = _binding!!
    private val favouritesViewModel: FavouritesViewModel by viewModels()
    private var adapter: MoviesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityFavouritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycler.layoutManager = LinearLayoutManager(this)

        binding.popularButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        val openMovie: (Movie) -> Unit = {
            val intent = Intent(this, MovieActivity::class.java)
            val b = Bundle()
            b.putInt("movieId", it.filmId)
            intent.putExtras(b)
            startActivity(intent)
            // ToDo add parameter description
        }

        favouritesViewModel.movies.observe(this) {
            // we can't dd to favourites anything, we are in favourites already, so we don't need third parameter
            adapter = MoviesAdapter(it.toMutableList(), openMovie, favouritesViewModel.removeFromFavourites)
            binding.recycler.adapter = adapter
        }

        favouritesViewModel.removeFavouriteMovie.observe(this) {
            if (it.success) {
                adapter?.removeItem(it.position)
                Toast.makeText(this, "Фильм удален из избранного", Toast.LENGTH_SHORT).show()

                adapter?.notifyItemRemoved(it.position)
                adapter?.notifyItemRangeChanged(it.position,
                    adapter?.itemCount?.minus(it.position) ?: 0
                )
            } else
                Toast.makeText(this, "Не удалось удалить фильм из избранного", Toast.LENGTH_SHORT)
                    .show()
        }
    }
}