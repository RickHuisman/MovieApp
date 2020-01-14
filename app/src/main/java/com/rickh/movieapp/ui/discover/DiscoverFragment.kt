package com.rickh.movieapp.ui.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.rickh.movieapp.R
import com.rickh.movieapp.tmdb.LoginRepository
import timber.log.Timber

class DiscoverFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val loggedInUser = LoginRepository.getInstance(requireContext()).user
        Timber.d("Logged in user = $loggedInUser")
        Toast.makeText(context, "Work in progress", Toast.LENGTH_SHORT).show()
        return inflater.inflate(R.layout.fragment_discover, container, false)
    }
}