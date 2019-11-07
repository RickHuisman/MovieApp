package com.rickh.movieapp.ui.movies

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder
import com.rickh.movieapp.R
import com.rickh.movieapp.ui.PosterViewHolder

/**
 * RecyclerView.Adapter for displaying movies and tvshows posters
 */
class PosterAdapter(
    private val columns: Int,
    private val activity: Activity
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    ListPreloader.PreloadModelProvider<PosterItem> {

    var items: List<PosterItem> = emptyList()
        set(newItems) {
            if (field.isNotEmpty()) {
                val diffResult = DiffUtil.calculateDiff(
                    PosterDiffCallback(field, newItems)
                )
                field = newItems
                diffResult.dispatchUpdatesTo(this)
            } else {
                field = newItems
            }
        }

    private val shotLoadingPlaceholders: Array<ColorDrawable?>
    private var showLoadingMore = false
    private val loadingMoreItemPosition: Int
        get() = if (showLoadingMore) itemCount - 1 else RecyclerView.NO_POSITION


    init {
        setHasStableIds(true)

        val placeholderColors = activity.resources.getIntArray(R.array.loading_placeholders_dark)
        shotLoadingPlaceholders = arrayOfNulls(placeholderColors.size)
        placeholderColors.indices.forEach {
            shotLoadingPlaceholders[it] = ColorDrawable(placeholderColors[it])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(activity)
        return when (viewType) {
            TYPE_POSTER -> PosterViewHolder(
                layoutInflater.inflate(R.layout.item_poster, parent, false)
            )
            TYPE_LOADING_MORE -> LoadingMoreHolder(
                layoutInflater.inflate(R.layout.infinite_loading, parent, false)
            )
            else -> throw IllegalStateException("Unsupported View type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_POSTER -> (holder as PosterViewHolder).bind(
                items[position],
                shotLoadingPlaceholders[position % shotLoadingPlaceholders.size]!!,
                activity
            )
            TYPE_LOADING_MORE -> (holder as LoadingMoreHolder).bind(position, showLoadingMore)
        }
    }

    private fun getItem(position: Int): PosterItem? {
        return if (position < 0 || position >= items.size) null else items[position]
    }

    fun getItemColumnSpan(position: Int): Int {
        return if (getItemViewType(position) == TYPE_LOADING_MORE) columns else 1
    }

    override fun getItemViewType(position: Int): Int {
        if (position < items.size && items.isNotEmpty()) {
            return TYPE_POSTER
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

    override fun getPreloadItems(position: Int): List<PosterItem> {
        val item = getItem(position)
        return if (item is PosterItem) {
            listOf(item)
        } else {
            emptyList()
        }
    }

    override fun getPreloadRequestBuilder(item: PosterItem): RequestBuilder<*>? {
        return Glide.with(activity).load(
            activity.getString(R.string.tmdb_base_img_url, item.posterPath)
        )
    }

    private class LoadingMoreHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val progress = itemView as ProgressBar

        fun bind(position: Int, showLoadingMore: Boolean) {
            progress.visibility =
                if (position > 0 && showLoadingMore) View.VISIBLE else View.INVISIBLE
        }
    }

    class PosterDiffCallback(
        private var oldItemsList: List<PosterItem>,
        private var newItemsList: List<PosterItem>
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
        private const val TYPE_POSTER = 0
        private const val TYPE_LOADING_MORE = -1
    }
}
