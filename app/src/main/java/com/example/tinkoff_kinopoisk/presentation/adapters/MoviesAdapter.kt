package com.example.tinkoff_kinopoisk.presentation.adapters

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.tinkoff_kinopoisk.R
import com.example.tinkoff_kinopoisk.domain.models.Movie

internal class MoviesAdapter(val items: MutableList<Movie>, val openMovie: (Movie) -> Unit, val toggleInFavourites: (Movie, Int) -> Unit) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>(){
    private var context: Context? = null

    override fun getItemViewType(position: Int): Int {
        return R.layout.movie_raw_object
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    fun addItems(newItems: List<Movie>){
        items.addAll(newItems)
    }

    fun removeItem(position: Int){
        items.removeAt(position)
    }

    fun makeItemSaved(position: Int){
        items[position].isFavourite = true
    }

    fun makeItemRemoved(position: Int){
        items[position].isFavourite = false
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
            toggleInFavourites(items[position], position)
            true
        }

        context?.let{
            if (items[position].posterPreview !== null) {
                val byteArray = items[position].posterPreview
                Glide.with(it)
                    .load(BitmapDrawable(it.resources, BitmapFactory.decodeByteArray(byteArray, 0, byteArray!!.size)))
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(25)))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.color.dark_gray)
                    .into(holder.poster)
            } else {
                Glide.with(it)
                    .load(items[position].posterUrlPreview)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(25)))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.color.dark_gray)
                    .into(holder.poster)
            }
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