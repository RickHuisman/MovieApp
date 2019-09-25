package com.rickh.movieapp.ui.movies

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.rickh.movieapp.R
import info.movito.themoviedbapi.model.MovieDb


class MoviesGridAdapter(
    private val columns: Int,
    private val context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    ListPreloader.PreloadModelProvider<MovieDb> {

    var movies: List<MovieDb> = emptyList()
        set(newMovies) {
            val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
                MovieDiffCallback(field, newMovies)
            )

            field = newMovies
            diffResult.dispatchUpdatesTo(this)
        }

    private val shotLoadingPlaceholders: Array<ColorDrawable?>
    private var showLoadingMore = false
    private val loadingMoreItemPosition: Int
        get() = if (showLoadingMore) itemCount - 1 else RecyclerView.NO_POSITION


    init {
        setHasStableIds(true)

        val placeholderColors = context.resources.getIntArray(R.array.loading_placeholders_dark)
        shotLoadingPlaceholders = arrayOfNulls(placeholderColors.size)
        placeholderColors.indices.forEach {
            shotLoadingPlaceholders[it] = ColorDrawable(placeholderColors[it])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        return when (viewType) {
            TYPE_MOVIE -> MovieViewHolder(
                layoutInflater.inflate(R.layout.item_movie, parent, false)
            )
            TYPE_LOADING_MORE -> LoadingMoreHolder(
                layoutInflater.inflate(R.layout.infinite_loading, parent, false)
            )
            else -> throw IllegalStateException("Unsupported View type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_MOVIE -> (holder as MovieViewHolder).bind(
                movies[position],
                shotLoadingPlaceholders[position % shotLoadingPlaceholders.size]!!
            )
            TYPE_LOADING_MORE -> (holder as LoadingMoreHolder).bind(position, showLoadingMore)
        }
    }

    private fun getItem(position: Int): MovieDb? {
        return if (position < 0 || position >= movies.size) null else movies[position]
    }

    fun getItemColumnSpan(position: Int): Int {
        return if (getItemViewType(position) == TYPE_LOADING_MORE) columns else 1
    }

    override fun getItemViewType(position: Int): Int {
        if (position < movies.size && movies.isNotEmpty()) {
            return TYPE_MOVIE
        }
        return TYPE_LOADING_MORE
    }

    override fun getItemId(position: Int): Long {
        return if (getItemViewType(position) == TYPE_LOADING_MORE) {
            -1L
        } else {
            movies[position].id.toLong()
        }
    }

    override fun getItemCount(): Int {
        return movies.size + if (showLoadingMore) 1 else 0
    }

    fun dataStartedLoading() {
        if (showLoadingMore) return
        showLoadingMore = true
        notifyItemInserted(loadingMoreItemPosition)
    }

    fun dataFinishedLoading() {
        if (!showLoadingMore) return
        val loadingPos = loadingMoreItemPosition
        showLoadingMore = false
        notifyItemRemoved(loadingPos)
    }

    override fun getPreloadItems(position: Int): List<MovieDb> {
        val movie = getItem(position)
        return if (movie is MovieDb) {
            listOf(movie)
        } else {
            emptyList()
        }
    }

    override fun getPreloadRequestBuilder(movie: MovieDb): RequestBuilder<*>? {
        return Glide.with(context).load(
            context.getString(R.string.tmdb_base_img_url, movie.posterPath)
        )
    }

    private class MovieViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private var moviePoster: ImageView = itemView.findViewById(R.id.movie_poster)

        fun bind(movie: MovieDb, placeholder: ColorDrawable) {
            Glide.with(moviePoster)
                .load(
                    moviePoster.context.getString(R.string.tmdb_base_img_url, movie.posterPath)
                )
                .placeholder(placeholder)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .centerCrop()
                .into(MovieTarget(moviePoster))
        }
    }

    private class LoadingMoreHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val progress = itemView as ProgressBar

        fun bind(position: Int, showLoadingMore: Boolean) {
            progress.visibility =
                if (position > 0 && showLoadingMore) View.VISIBLE else View.INVISIBLE
        }
    }

    class MovieDiffCallback(
        private var oldMovieList: List<MovieDb>,
        private var newMovieList: List<MovieDb>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldMovieList[oldItemPosition].id == newMovieList[newItemPosition].id
        }

        override fun getOldListSize(): Int {
            return oldMovieList.size
        }

        override fun getNewListSize(): Int {
            return newMovieList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldMovieList[oldItemPosition] == newMovieList[newItemPosition]
        }
    }

    companion object {
        private const val TYPE_MOVIE = 0
        private const val TYPE_LOADING_MORE = -1
    }
}
