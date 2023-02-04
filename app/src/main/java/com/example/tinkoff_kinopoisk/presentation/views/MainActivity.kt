package com.example.tinkoff_kinopoisk.presentation.views

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
            if (adapter == null){
                adapter = MoviesAdapter(it)
                binding.recycler.adapter = adapter
            }else{
                val oldSize = adapter!!.getSize()
                adapter!!.addItems(it)
                adapter!!.notifyItemRangeInserted(oldSize, it.size)
            }

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

        /* ToDo mainViewModel.updateRecycler.observe(viewLifecycleOwner){
        binding.recycler.adapter?.notifyItemChanged(it)
        }*/
    }
}


