package com.rickh.movieapp.ui.posters

import com.rickh.movieapp.R

enum class SortOptions(
    private val menuItemId: Int
) {
    MOVIES_POPULAR(R.id.action_movies_sorting_popular),
    MOVIES_TOP_RATED(R.id.action_movies_sorting_top_rated),
    MOVIES_UPCOMING(R.id.action_movies_sorting_upcoming),
    MOVIES_NOW_PLAYING(R.id.action_movies_sorting_now_playing),

    TV_SHOWS_POPULAR(R.id.action_tv_shows_sorting_popular),
    TV_SHOWS_TOP_RATED(R.id.action_tv_shows_sorting_top_rated),
    TV_SHOWS_ON_TV(R.id.action_tv_shows_sorting_on_tv),
    TV_SHOWS_AIRING_TODAY(R.id.action_tv_shows_sorting_airing_today);

    fun menuItemId(): Int {
        return menuItemId
    }

    companion object {
        private val ALL = arrayOf(
            MOVIES_POPULAR,
            MOVIES_TOP_RATED,
            MOVIES_UPCOMING,
            MOVIES_NOW_PLAYING,
            TV_SHOWS_POPULAR,
            TV_SHOWS_TOP_RATED,
            TV_SHOWS_ON_TV,
            TV_SHOWS_AIRING_TODAY
        )

        fun findItem(menuItemId: Int): SortOptions {
            for (sortOption in ALL) {
                if (sortOption.menuItemId == menuItemId) {
                    return sortOption
                }
            }
            throw IllegalArgumentException("No sort option for menu item id: $menuItemId")
        }
    }
}