package com.rickh.movieapp.ui.people

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder
import com.rickh.movieapp.R
import info.movito.themoviedbapi.model.people.PersonPeople

/**
 * RecyclerView.Adapter for displaying popular people
 */
class PopularPeopleAdapter(
    private val context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    ListPreloader.PreloadModelProvider<PersonPeople> {

    var people: List<PersonPeople> = emptyList()
        set(newItems) {
            val diffCallback = PeopleDiffCallback(people, newItems)
            val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(diffCallback)

            field = newItems
            diffResult.dispatchUpdatesTo(this)
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

    private fun getItem(position: Int): PersonPeople? {
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

    override fun getPreloadItems(position: Int): List<PersonPeople> {
        val item = getItem(position)
        return if (item is PersonPeople) {
            listOf(item)
        } else {
            emptyList()
        }
    }

    override fun getPreloadRequestBuilder(item: PersonPeople): RequestBuilder<*>? {
        return Glide.with(context).load(
            context.getString(R.string.tmdb_base_img_url, item.profilePath)
        )
    }

    class PersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image: ImageView = itemView.findViewById(R.id.image)
        private val name: TextView = itemView.findViewById(R.id.name)
        private val detail: TextView = itemView.findViewById(R.id.detail)

        fun bind(person: PersonPeople) {
            Glide.with(image)
                .load("https://image.tmdb.org/t/p/original${person.profilePath}")
                .centerCrop()
                .circleCrop()
                .into(image)

            name.text = person.name
            detail.text = "Jumanji: Welcome to the Jungle · Fast & Furious 6 · Moana"
        }
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