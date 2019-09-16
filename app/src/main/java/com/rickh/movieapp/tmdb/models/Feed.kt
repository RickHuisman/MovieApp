package com.rickh.movieapp.tmdb.models

import com.squareup.moshi.Json

data class Feed(
    val page: Int,
    @Json(name = "total_results") val totalResults: Int,
    @Json(name = "total_pages") val totalPages: Int,
    val results: List<Movie>
)