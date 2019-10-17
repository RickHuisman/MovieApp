package com.rickh.movieapp.ui.people

import androidx.lifecycle.MutableLiveData
import com.omertron.themoviedbapi.enumeration.MediaType
import com.omertron.themoviedbapi.model.media.MediaBasic
import com.rickh.movieapp.ui.movies.PosterItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

abstract class Paginator<T> {
    var pageIndex: Int = 1
    val items = MutableLiveData<List<T>>()
    val loadingProgress = MutableLiveData<Boolean>()

    private var job = Job()
    val scope = CoroutineScope(Dispatchers.IO + job)

    abstract fun loadMore()

    fun convertToPosterItems(items: List<MediaBasic>): List<PosterItem> {
        return items.map {
            PosterItem(
                it.id.toLong(),
                it.posterPath,
                MediaType.MOVIE
            )
        }
    }

    fun cancelLoading() {
        scope.cancel()
    }
}