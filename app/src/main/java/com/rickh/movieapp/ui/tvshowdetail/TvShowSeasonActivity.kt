package com.rickh.movieapp.ui.tvshowdetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.omertron.themoviedbapi.model.tv.TVInfo
import com.rickh.movieapp.R
import com.rickh.movieapp.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_tv_show_episodes.*

class TvShowSeasonActivity : AppCompatActivity() {

    private lateinit var viewModel: TvShowSeasonViewModel
    private lateinit var seasonPagerAdapter: SeasonPagerAdapter
    private lateinit var fragmentContainer: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tv_show_episodes)

        viewModel = ViewModelProvider(this).get(TvShowSeasonViewModel::class.java)

        fragmentContainer = findViewById(R.id.season_fragment_container)

        if (intent.hasExtra(INTENT_TV_SHOW)) {
            val tvShow = intent.getSerializableExtra(INTENT_TV_SHOW) as TVInfo
            setupSpinner(tvShow)
        }
    }

    private fun setupSpinner(tvShow: TVInfo) {
        with(seasons_spinner) {
            adapter = SeasonSpinnerAdapter(context, tvShow.seasons)
            dropDownVerticalOffset = ViewUtils.dpToPix(context, 8)
            onItemSelectedListener = spinnerOnItemSelected
        }

        seasonPagerAdapter = SeasonPagerAdapter(tvShow.id, tvShow.seasons, supportFragmentManager)
    }

    private val spinnerOnItemSelected = object : OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>) {

        }

        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
            setCurrentFragment(position)
        }
    }

    private fun setCurrentFragment(position: Int) {
        val currentFragment = seasonPagerAdapter.instantiateItem(
            fragmentContainer,
            position
        ) as Fragment
        seasonPagerAdapter.setPrimaryItem(season_fragment_container, position, currentFragment)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.season_fragment_container, currentFragment)
            .commit()
    }

    companion object {
        private const val INTENT_TV_SHOW = "TV_SHOW"

        fun newIntent(context: Context, tvShow: TVInfo): Intent {
            val intent = Intent(context, TvShowSeasonActivity()::class.java)
            intent.putExtra(INTENT_TV_SHOW, tvShow)
            return intent
        }
    }
}