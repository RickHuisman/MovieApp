package com.rickh.movieapp

import android.app.Application
import timber.log.Timber


class MovieAppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}