package com.rickh.movieapp.ui.movies

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
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
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.rickh.movieapp.R
import com.rickh.movieapp.ui.GridItem
import com.rickh.movieapp.util.AnimUtils
import com.rickh.movieapp.util.ObservableColorMatrix
import timber.log.Timber


class MoviesGridAdapter(
    private val columns: Int,
    private val context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    ListPreloader.PreloadModelProvider<GridItem> {

    var items: List<GridItem> = emptyList()
        set(newItems) {
            val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(
                MovieDiffCallback(field, newItems)
            )

            field = newItems
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
                items[position],
                shotLoadingPlaceholders[position % shotLoadingPlaceholders.size]!!
            )
            TYPE_LOADING_MORE -> (holder as LoadingMoreHolder).bind(position, showLoadingMore)
        }
    }

    private fun getItem(position: Int): GridItem? {
        return if (position < 0 || position >= items.size) null else items[position]
    }

    fun getItemColumnSpan(position: Int): Int {
        return if (getItemViewType(position) == TYPE_LOADING_MORE) columns else 1
    }

    override fun getItemViewType(position: Int): Int {
        if (position < items.size && items.isNotEmpty()) {
            return TYPE_MOVIE
        }
        return TYPE_LOADING_MORE
    }

    override fun getItemId(position: Int): Long {
        return if (getItemViewType(position) == TYPE_LOADING_MORE) {
            -1L
        } else {
            items[position].id
        }
    }

    override fun getItemCount(): Int {
        return items.size + if (showLoadingMore) 1 else 0
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

    override fun getPreloadItems(position: Int): List<GridItem> {
        val item = getItem(position)
        return if (item is GridItem) {
            listOf(item)
        } else {
            emptyList()
        }
    }

    override fun getPreloadRequestBuilder(item: GridItem): RequestBuilder<*>? {
        return Glide.with(context).load(
            context.getString(R.string.tmdb_base_img_url, item.posterPath)
        )
    }

    private class MovieViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private var poster: ImageView = itemView.findViewById(R.id.movie_poster)

        init {
            darkenImage()
        }

        fun bind(item: GridItem, placeholder: ColorDrawable) {
            Glide.with(poster)
                .load(
                    poster.context.getString(R.string.tmdb_base_img_url, item.posterPath)
                )
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ) = false

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        if (!item.hasFadedIn) {
                            fade()
                            item.hasFadedIn = true
                        }
                        return false
                    }
                })
                .placeholder(placeholder)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(MovieTarget(poster))

            itemView.setOnLongClickListener {
                val popup = MovieDetailPopup(poster.context)
                popup.showWithAnchor(poster)

                Timber.d("${item.id}")
                true
            }
        }

        private fun fade() {
            poster.setHasTransientState(true)
            val cm = ObservableColorMatrix()
            ObjectAnimator.ofFloat(cm, ObservableColorMatrix.SATURATION, 0f, 1f).apply {
                addUpdateListener {
                    // Setting the saturation overwrites any darkening so need to reapply.
                    // Just animating the color matrix does not invalidate the
                    // drawable so need this update listener.  Also have to create a
                    // new CMCF as the matrix is immutable :(
                    darkenImage(cm)
                }
                duration = 2000L
                interpolator = AnimUtils.getFastOutSlowInInterpolator(poster.context)
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        poster.setHasTransientState(false)
                    }
                })
                start()
            }
        }

        private fun darkenImage(colorMatrix: ColorMatrix = ColorMatrix()) {
            poster.colorFilter = ColorMatrixColorFilter(colorMatrix)
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
        private var oldItemsList: List<GridItem>,
        private var newItemsList: List<GridItem>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItemsList[oldItemPosition].id == newItemsList[newItemPosition].id
        }

        override fun getOldListSize(): Int {
            return oldItemsList.size
        }

        override fun getNewListSize(): Int {
            return newItemsList.size
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItemsList[oldItemPosition] == newItemsList[newItemPosition]
        }
    }

    companion object {
        private const val TYPE_MOVIE = 0
        private const val TYPE_LOADING_MORE = -1
    }
}
