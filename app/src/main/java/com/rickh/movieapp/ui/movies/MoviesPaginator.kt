package com.rickh.movieapp.ui.movies

import com.omertron.themoviedbapi.model.movie.MovieInfo
import com.rickh.movieapp.tmdb.MoviesRepository
import com.rickh.movieapp.tmdb.Result
import com.rickh.movieapp.ui.people.Paginator

class MoviesPaginator : Paginator<MovieInfo>() {

    private var sortOption = MoviesSortOptions.TOP_RATED

    override fun loadData() {
        val result = when (sortOption) {
            MoviesSortOptions.POPULAR -> MoviesRepository.getPopular(pageIndex)
            MoviesSortOptions.TOP_RATED -> MoviesRepository.getTopRated(pageIndex)
            MoviesSortOptions.UPCOMING -> MoviesRepository.getUpcoming(pageIndex)
            MoviesSortOptions.NOW_PLAYING -> MoviesRepository.getNowPlaying(pageIndex)
        }

        when (result) {
            is Result.Success -> items.postValue(getItemsForDisplay(result))
            is Result.Error -> items.postValue(result)
        }
    }

    fun setSortMode(sortOption: MoviesSortOptions) {
        clearList()
        this.sortOption = sortOption
        loadMore()
    }
}