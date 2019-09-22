package com.rickh.movieapp.tmdb.models


data class Movie(
    val id: Int,
    val title: String,
    val popularity: Double,
    val vote_count: Int,
    val vote_average: Double,
    val video: Boolean,
    val poster_path: String,
    val backdrop_path: String?,
    val adult: Boolean,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val release_date: String
//  TODO val genre_ids
)