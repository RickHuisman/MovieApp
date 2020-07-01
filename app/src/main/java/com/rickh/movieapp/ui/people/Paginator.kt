package com.rickh.movieapp.ui.people

import androidx.lifecycle.MutableLiveData
import com.omertron.themoviedbapi.model.media.MediaBasic
import com.rickh.movieapp.data.tmdb.Result
import com.rickh.movieapp.ui.posters.PosterItem
import kotlinx.coroutines.*

abstract class Paginator<T> {
    var pageIndex: Int = 1
    val items = MutableLiveData<Result<List<T>>>()
    val loadingProgress = MutableLiveData<Boolean>()

    private var job = Job()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    init {
        loadMore()
    }

    fun loadMore() {
        scope.launch {
            loadingProgress.postValue(true)

            val resultForDisplay = when (val result = loadData()) {
                is Result.Success -> {
                    pageIndex++
                    getItemsForDisplay(result as Result<List<T>>)
                }
                is Result.Error -> result
            }
            items.postValue(resultForDisplay)

            loadingProgress.postValue(false)
        }
    }

    protected abstract fun loadData(): Result<Any>

    private fun getItemsForDisplay(newResult: Result<List<T>>): Result<List<T>> {
        val oldResult = items.value
        if (oldResult is Result.Success) {
            val oldItems = oldResult.data
            if (newResult is Result.Success) {
                val itemsToBeDisplayed = oldItems.toMutableList()
                itemsToBeDisplayed.addAll(newResult.data)
                return Result.Success(itemsToBeDisplayed)
            }
        }
        return newResult
    }

    // TODO move this function to a helper class
    fun toPosterItems(items: List<MediaBasic>): List<PosterItem> {
        return items.map {
            PosterItem(
                it.id.toLong(),
                it.posterPath,
                it.mediaType
            )
        }
    }

    protected fun clearList() {
        items.value = Result.Success(emptyList())
        pageIndex = 1
    }

    fun cancelLoading() {
        scope.cancel()
    }
}