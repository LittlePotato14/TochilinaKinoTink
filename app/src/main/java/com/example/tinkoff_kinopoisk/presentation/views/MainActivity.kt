package com.example.tinkoff_kinopoisk.presentation.views

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tinkoff_kinopoisk.data.MyDatabaseHelper
import com.example.tinkoff_kinopoisk.databinding.ActivityMainBinding
import com.example.tinkoff_kinopoisk.domain.models.Movie
import com.example.tinkoff_kinopoisk.presentation.adapters.MoviesAdapter
import com.example.tinkoff_kinopoisk.presentation.viewsModels.MainViewModel


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by viewModels()
    private var adapter: MoviesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recycler.layoutManager = LinearLayoutManager(this)

        binding.favouritesButton.setOnClickListener {
            startActivity(Intent(this, FavouritesActivity::class.java))
            finish()
        }

        val openMovie: (Movie) -> Unit = {
            val intent = Intent(this, MovieActivity::class.java)
            val b = Bundle()
            b.putInt("movieId", it.filmId)
            intent.putExtras(b)
            startActivity(intent)
        }

        mainViewModel.movies.observe(this) {
            if (adapter == null){
                adapter = MoviesAdapter(it, openMovie, mainViewModel.saveToFavourites)
                binding.recycler.adapter = adapter
            }else{
                val oldSize = adapter!!.getSize()
                adapter!!.addItems(it)
                adapter!!.notifyItemRangeInserted(oldSize, it.size)
            }
        }

        mainViewModel.savingMovie.observe(this){
            if(it.success){
                adapter?.makeItemSaved(it.position)
                adapter?.notifyItemChanged(it.position)
                Toast.makeText(this, "Фильм сохранен в избранные", Toast.LENGTH_SHORT).show()
            }else
                Toast.makeText(this, "Не удалось сохранить фильм", Toast.LENGTH_SHORT).show()
        }

        binding.recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (recyclerView.canScrollVertically(-1) &&
                    !recyclerView.canScrollVertically(1) &&
                    newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mainViewModel.getNextPagePopularMovies()
                }
            }
        })
    }
}


