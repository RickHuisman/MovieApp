package com.rickh.movieapp.ui.movies

import com.omertron.themoviedbapi.model.movie.MovieInfo
import com.rickh.movieapp.data.tmdb.MoviesRepository
import com.rickh.movieapp.data.tmdb.Result
import com.rickh.movieapp.ui.people.Paginator

class MoviesPaginator : Paginator<MovieInfo>() {

    private var sortOption = MoviesSortOptions.TOP_RATED

    override fun loadData(): Result<Any> {
        return when (sortOption) {
            MoviesSortOptions.POPULAR -> MoviesRepository.getPopular(pageIndex)
            MoviesSortOptions.TOP_RATED -> MoviesRepository.getTopRated(pageIndex)
            MoviesSortOptions.UPCOMING -> MoviesRepository.getUpcoming(pageIndex)
            MoviesSortOptions.NOW_PLAYING -> MoviesRepository.getNowPlaying(pageIndex)
        }
    }

    fun setSortMode(sortOption: MoviesSortOptions) {
        clearList()
        this.sortOption = sortOption
        loadMore()
    }
}