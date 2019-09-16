package com.rickh.movieapp.tmdb

import com.rickh.movieapp.tmdb.models.Feed
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val TMDB_API_KEY = "ea75e60dfc33c0ff92d6e331c9debbea"

interface TmdbApiService {

    @GET("movie/top_rated?api_key=$TMDB_API_KEY&language=en-US&")
    suspend fun getTopRatedMovies(@Query("page") page: Int):
            Feed
}