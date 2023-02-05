package com.example.tinkoff_kinopoisk.presentation.adapters

import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.tinkoff_kinopoisk.R
import com.example.tinkoff_kinopoisk.domain.models.Movie

internal class MoviesAdapter(private var items: List<Movie>, val openMovie: (Movie) -> Unit, val saveToFavourites: (Movie, Int) -> Unit) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>(){
    private var context: Context? = null

    override fun getItemViewType(position: Int): Int {
        return R.layout.movie_raw_object
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    fun getSize() = items.size

    fun addItems(newItems: List<Movie>){
        items = items.plus(newItems)
    }

    fun makeItemSaved(position: Int){
        items[position].isFavourite = true
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context)
            .inflate(viewType, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: MovieViewHolder,
        position: Int
    ) {
        holder.title.text = items[position].nameRu

        var genreYear = ""
        items[position].genres.getOrNull(1)?.let{
            genreYear += it.genre.replaceFirstChar(Char::titlecase) + " "
        }
        genreYear += "(" + items[position].year.toString() + ")"
        holder.genreYear.text = genreYear

        if(items[position].isFavourite == true)
            with(holder.favouritesIcon){
                visibility = View.VISIBLE
                isEnabled = true
            }
        else
            with(holder.favouritesIcon){
                visibility = View.GONE
                isEnabled = false
            }

        /*TODO holder.favouritesIcon.setOnClickListener {
            removeFromFavourites(items[position], position)
        }*/

        holder.card.setOnClickListener {
            openMovie(items[position])
        }

        holder.card.setOnLongClickListener {
            saveToFavourites(items[position], position)
            true
        }

        context?.let{
            Glide.with(it)
                .load(items[position].posterUrlPreview)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(25)))
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.color.dark_gray)
                .into(holder.poster)
        }
    }

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val poster: ImageView = itemView.findViewById(R.id.poster)
        val title: TextView = itemView.findViewById(R.id.title)
        val genreYear: TextView = itemView.findViewById(R.id.genre_year)
        val favouritesIcon: ImageButton = itemView.findViewById(R.id.favouritesIcon)
        val card: CardView = itemView.findViewById(R.id.movie_card)
    }
}