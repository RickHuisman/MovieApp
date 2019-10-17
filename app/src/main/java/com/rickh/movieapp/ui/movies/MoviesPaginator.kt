package com.rickh.movieapp.ui.movies

import com.omertron.themoviedbapi.enumeration.MediaType
import com.omertron.themoviedbapi.model.movie.MovieInfo
import com.rickh.movieapp.tmdb.MoviesRepository
import com.rickh.movieapp.ui.people.Paginator
import kotlinx.coroutines.launch
import timber.log.Timber

class MoviesPaginator : Paginator<MovieInfo>() {

    private var sortOption = MoviesSortOptions.TOP_RATED

    override fun loadMore() {
        scope.launch {
            loadingProgress.postValue(true)

            val oldItems = items.value.orEmpty()
            val newItems = when (sortOption) {
                MoviesSortOptions.POPULAR -> MoviesRepository.getPopular(pageIndex)
                MoviesSortOptions.TOP_RATED -> MoviesRepository.getTopRated(pageIndex)
                MoviesSortOptions.UPCOMING -> MoviesRepository.getUpcoming(pageIndex)
                MoviesSortOptions.NOW_PLAYING -> MoviesRepository.getNowPlaying(pageIndex)
            }
            items.postValue(getItemsForDisplay(oldItems, newItems))

            pageIndex++
            loadingProgress.postValue(false)
        }
    }

    fun convertMovies(movies: List<MovieInfo>): List<PosterItem> {
        return movies.map {
            PosterItem(
                it.id.toLong(),
                it.posterPath,
                MediaType.MOVIE
            )
        }
    }

    private fun getItemsForDisplay(
        oldItems: List<MovieInfo>,
        newItems: List<MovieInfo>
    ): List<MovieInfo> {
        val itemsToBeDisplayed = oldItems.toMutableList()
        itemsToBeDisplayed.addAll(newItems)
        return itemsToBeDisplayed
    }

    fun setSortMode(sortOption: MoviesSortOptions) {
        Timber.d("setSortMode()")
        clearList()
        this.sortOption = sortOption
        loadMore()
    }

    private fun clearList() {
        items.value = emptyList()
        pageIndex = 1
    }
}