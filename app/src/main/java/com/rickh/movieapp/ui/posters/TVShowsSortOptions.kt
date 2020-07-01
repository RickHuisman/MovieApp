package com.rickh.movieapp.ui.posters

import com.rickh.movieapp.R

enum class TVShowsSortOptions(
    private val menuItemId: Int
) {
    POPULAR(R.id.action_tv_shows_sorting_popular),
    TOP_RATED(R.id.action_tv_shows_sorting_top_rated),
    ON_TV(R.id.action_tv_shows_sorting_on_tv),
    AIRING_TODAY(R.id.action_tv_shows_sorting_airing_today);

    fun menuItemId(): Int {
        return menuItemId
    }

    companion object {
        private val ALL = arrayOf(
            POPULAR,
            TOP_RATED,
            ON_TV,
            AIRING_TODAY
        )

        fun findItem(menuItemId: Int): TVShowsSortOptions {
            for (sortOption in ALL) {
                if (sortOption.menuItemId == menuItemId) {
                    return sortOption
                }
            }
            throw IllegalArgumentException("No sort option for menu item id: $menuItemId")
        }
    }
}