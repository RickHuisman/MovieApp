package com.rickh.movieapp.ui.people

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder
import com.omertron.themoviedbapi.model.person.PersonFind
import com.rickh.movieapp.R
import timber.log.Timber

/**
 * RecyclerView.Adapter for displaying popular people
 */
class PopularPeopleAdapter(
    private val context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    ListPreloader.PreloadModelProvider<PersonFind> {

    var people: List<PersonFind> = emptyList()
        set(newItems) {
            if (field.isNotEmpty()) {
                val diffResult = DiffUtil.calculateDiff(
                    PeopleDiffCallback(field, newItems)
                )
                field = newItems
                diffResult.dispatchUpdatesTo(this)
            } else {
                field = newItems
            }
        }

    init {
        setHasStableIds(true)
    }

    private var showLoadingMore = false
    private val loadingMoreItemPosition: Int
        get() = if (showLoadingMore) itemCount - 1 else RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        return when (viewType) {
            TYPE_PERSON -> PersonViewHolder(
                layoutInflater.inflate(R.layout.item_person, parent, false)
            )
            TYPE_LOADING_MORE -> LoadingMoreHolder(
                layoutInflater.inflate(R.layout.infinite_loading, parent, false)
            )
            else -> throw IllegalStateException("Unsupported View type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_PERSON -> (holder as PersonViewHolder).bind(people[position])
            TYPE_LOADING_MORE -> (holder as LoadingMoreHolder).bind(position, showLoadingMore)
        }
    }

    private fun getItem(position: Int): PersonFind? {
        return if (position < 0 || position >= people.size) null else people[position]
    }

    override fun getItemId(position: Int): Long {
        return if (getItemViewType(position) == TYPE_LOADING_MORE) {
            -1L
        } else {
            people[position].id.toLong()
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position < people.size && people.isNotEmpty()) {
            return TYPE_PERSON
        }
        return TYPE_LOADING_MORE
    }

    override fun getItemCount(): Int {
        return people.size + if (showLoadingMore) 1 else 0
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

    override fun getPreloadItems(position: Int): List<PersonFind> {
        val item = getItem(position)
        return if (item is PersonFind) {
            listOf(item)
        } else {
            emptyList()
        }
    }

    override fun getPreloadRequestBuilder(item: PersonFind): RequestBuilder<*>? {
        return Glide.with(context).load(
            context.getString(R.string.tmdb_base_img_url, item.profilePath)
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

    companion object {
        private const val TYPE_PERSON = 0
        private const val TYPE_LOADING_MORE = -1
    }
}