package com.example.tinkoff_kinopoisk.presentation.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tinkoff_kinopoisk.R
import com.example.tinkoff_kinopoisk.databinding.ActivityMainBinding
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

        // ToDo set listener to favourites button

        binding.recycler.layoutManager = LinearLayoutManager(this)

        mainViewModel.movies.observe(this) {
            adapter = MoviesAdapter(it ?: listOf())
            binding.recycler.adapter = adapter
        }

        /* ToDo mainViewModel.updateRecycler.observe(viewLifecycleOwner){
        binding.recycler.adapter?.notifyItemChanged(it)
        }*/
    }
}


