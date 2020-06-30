package com.rickh.movieapp.ui.posters

import com.rickh.movieapp.R

enum class MoviesSortOptions(
    private val menuItemId: Int
) {
    POPULAR(R.id.action_movies_sorting_popular),
    TOP_RATED(R.id.action_movies_sorting_top_rated),
    UPCOMING(R.id.action_movies_sorting_upcoming),
    NOW_PLAYING(R.id.action_movies_sorting_now_playing);

    fun menuItemId(): Int {
        return menuItemId
    }

    companion object {
        private val ALL = arrayOf(
            POPULAR,
            TOP_RATED,
            UPCOMING,
            NOW_PLAYING
        )

        fun findItem(menuItemId: Int): MoviesSortOptions {
            for (sortOption in ALL) {
                if (sortOption.menuItemId == menuItemId) {
                    return sortOption
                }
            }
            throw IllegalArgumentException("No sort option for menu item id: $menuItemId")
        }
    }
}