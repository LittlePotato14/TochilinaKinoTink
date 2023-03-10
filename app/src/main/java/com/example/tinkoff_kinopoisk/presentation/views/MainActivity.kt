package com.example.tinkoff_kinopoisk.presentation.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tinkoff_kinopoisk.databinding.ActivityMainBinding
import com.example.tinkoff_kinopoisk.domain.models.Movie
import com.example.tinkoff_kinopoisk.presentation.adapters.MoviesAdapter
import com.example.tinkoff_kinopoisk.presentation.viewsModels.MainViewModel


class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Open search page
        binding.searchButton.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        // Open favourites page
        binding.favouritesButton.setOnClickListener {
            startActivity(Intent(this, FavouritesActivity::class.java))
            finish()
        }

        mainViewModel.getMovies()

        initMoviesRecycle(binding, mainViewModel)
    }

    fun initMoviesRecycle(binding: ActivityMainBinding, mainViewModel: MainViewModel) {
        binding.recycler.layoutManager = LinearLayoutManager(this)

        val openMovie: (Movie) -> Unit = {
            val intent = Intent(this, MovieActivity::class.java)
            val b = Bundle()
            b.putInt("movieId", it.filmId)
            intent.putExtras(b)
            startActivity(intent)
        }

        val adapter = MoviesAdapter(mutableListOf(), openMovie, mainViewModel.toggleInFavourites)
        binding.recycler.adapter = adapter

        mainViewModel.movies.observe(this) {
                if (it.isNotEmpty()) {
                    binding.spinner.visibility = View.GONE
                }
                val oldSize = adapter.itemCount
                adapter.addItems(it)
                adapter.notifyItemRangeInserted(oldSize, it.size)
        }

        // Save or remove movie from favourites
        mainViewModel.toggleFavouriteMovie.observe(this) {
            if (it.success) {
                if (it.isSaving) {
                    adapter.makeItemSaved(it.position)
                    Toast.makeText(this, "?????????? ???????????????? ?? ??????????????????", Toast.LENGTH_SHORT).show()
                } else {
                    adapter.makeItemRemoved(it.position)
                    Toast.makeText(this, "?????????? ???????????? ???? ????????????????????", Toast.LENGTH_SHORT).show()
                }

                adapter.notifyItemChanged(it.position)
            } else
                if (it.isSaving) {
                    Toast.makeText(this, "???? ?????????????? ?????????????????? ??????????", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        this,
                        "???? ?????????????? ?????????????? ?????????? ???? ????????????????????",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        // Infinite scroll (pagination)
        binding.recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (recyclerView.canScrollVertically(-1) &&
                    !recyclerView.canScrollVertically(1) &&
                    newState == RecyclerView.SCROLL_STATE_IDLE
                ) {
                    mainViewModel.getNextPagePopularMovies()
                }
            }
        })
    }
}


