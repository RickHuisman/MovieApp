package com.rickh.movieapp.data.login

data class LoggedInUser(
    val sessionId: String,
    val id: Int,
    val name: String,
    val username: String,
    val includeAdult: Boolean,
    val language: String,
    val country: String,
    val avatar: String
)