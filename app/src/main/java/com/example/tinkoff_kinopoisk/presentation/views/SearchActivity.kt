package com.example.tinkoff_kinopoisk.presentation.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tinkoff_kinopoisk.databinding.ActivitySearchBinding
import com.example.tinkoff_kinopoisk.domain.models.Movie
import com.example.tinkoff_kinopoisk.presentation.adapters.MoviesAdapter
import com.example.tinkoff_kinopoisk.presentation.viewsModels.MainViewModel

class SearchActivity : AppCompatActivity() {
    private var _binding: ActivitySearchBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by viewModels()
    private var query: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Send search request with user query
        binding.editText.setOnKeyListener { view, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                binding.spinner.visibility = View.VISIBLE
                query = (view as EditText).text.toString()
                mainViewModel.getMovies(query!!, true)
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        // Return to tha main activity
        binding.backButton.setOnClickListener { onBackPressed() }

        initMoviesRecycle(binding, mainViewModel)
    }

    fun initMoviesRecycle(binding: ActivitySearchBinding, mainViewModel: MainViewModel) {
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
            val oldSize = adapter.itemCount

            if (mainViewModel.isSearch) {
                binding.spinner.visibility = View.GONE
                adapter.replaceItems(it.toMutableList())
                adapter.notifyDataSetChanged()
            } else {
                adapter.addItems(it)
                adapter.notifyItemRangeInserted(oldSize, it.size)
            }
        }

        mainViewModel.toggleFavouriteMovie.observe(this) {
            if (it.success) {
                if (it.isSaving) {
                    adapter.makeItemSaved(it.position)
                    Toast.makeText(this, "Фильм сохранен в избранные", Toast.LENGTH_SHORT).show()
                } else {
                    adapter.makeItemRemoved(it.position)
                    Toast.makeText(this, "Фильм удален из избранного", Toast.LENGTH_SHORT).show()
                }

                adapter.notifyItemChanged(it.position)
            } else
                if (it.isSaving) {
                    Toast.makeText(this, "Не удалось сохранить фильм", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        this,
                        "Не удалось удалить фильм из избранного",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        binding.recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (recyclerView.canScrollVertically(-1) &&
                    !recyclerView.canScrollVertically(1) &&
                    newState == RecyclerView.SCROLL_STATE_IDLE
                ) {
                    if (query != null)
                        mainViewModel.getNextPageSearchMovies(query!!)
                }
            }
        })
    }
}

