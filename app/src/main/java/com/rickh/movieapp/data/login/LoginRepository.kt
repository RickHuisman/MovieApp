package com.rickh.movieapp.data.login

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.rickh.movieapp.data.tmdb.Result

class LoginRepository(context: Context) {

    private val localDataSource = LoginLocalDataSource(
        UserLocalDataSource.getInstance(context)
    )
    private val remoteDataSource = LoginRemoteDataSource()

    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    val userLoggedInObserver: MutableLiveData<LoggedInUser?> by lazy {
        MutableLiveData<LoggedInUser?>()
    }

    init {
        user = localDataSource.getLoggedInUser()
    }

    fun logout() {
        user = null

        localDataSource.logout()
        remoteDataSource.logout()
    }

    fun login(username: String, password: String): Result<LoggedInUser> {
        val result = remoteDataSource.login(username, password)

        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }
        return result
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        user = loggedInUser
        localDataSource.setLoggedInUser(loggedInUser)
        userLoggedInObserver.postValue(loggedInUser)
    }

    companion object {
        @Volatile
        private var INSTANCE: LoginRepository? = null

        fun getInstance(context: Context): LoginRepository {
            return INSTANCE
                ?: synchronized(this) {
                    INSTANCE
                        ?: LoginRepository(context).also { INSTANCE = it }
                }
        }
    }
}