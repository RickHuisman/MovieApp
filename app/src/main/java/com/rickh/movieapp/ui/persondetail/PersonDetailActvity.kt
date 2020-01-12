package com.rickh.movieapp.ui.persondetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.omertron.themoviedbapi.model.credits.CreditBasic
import com.omertron.themoviedbapi.model.credits.CreditMovieBasic
import com.omertron.themoviedbapi.model.credits.CreditTVBasic
import com.omertron.themoviedbapi.model.person.PersonCreditList
import com.omertron.themoviedbapi.model.person.PersonFind
import com.rickh.movieapp.R
import com.rickh.movieapp.tmdb.Result
import kotlinx.android.synthetic.main.activity_person_detail.*
import kotlinx.android.synthetic.main.activity_person_detail.error
import kotlinx.android.synthetic.main.activity_person_detail.haulerView
import kotlinx.android.synthetic.main.activity_person_detail.loading
import kotlinx.android.synthetic.main.fragment_poster_grid.*
import org.threeten.bp.LocalDate
import timber.log.Timber

class PersonDetailActivity : AppCompatActivity(), OnFilterChanged {

    private lateinit var viewModel: PersonDetailViewModel
    private var activeFilters: MutableSet<String> = mutableSetOf()
    private var personCreditsAdapter: PersonCreditsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_detail)

        viewModel = ViewModelProviders.of(this).get(PersonDetailViewModel::class.java)

        val person: PersonFind = intent.getSerializableExtra(INTENT_PERSON_ID) as PersonFind

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_close_24dp)
            title = person.name
        }

        haulerView.setOnDragDismissedListener { finish() }
        error.setOnRetryClickListener(View.OnClickListener { loadPerson(person.id) })

        loadPerson(person.id)
    }

    private fun loadPerson(personId: Int) {
        showLoading(true)
        viewModel.getPerson(personId).observe(this, Observer {
            when (it) {
                is Result.Success -> showPersonSuccess(it.data)
                is Result.Error -> showError()
            }
            showLoading(false)
        })
    }

    private fun showPersonSuccess(person: PersonInfoAndCredits) {
        content.visibility = View.VISIBLE

        setBiography(person.personInfo.biography)
        setFilmography(person.credits)
    }

    private fun showLoading(show: Boolean) {
        loading.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showError() {
        error.visibility = View.VISIBLE
    }

    private fun setBiography(biography: String) {
        biography_textview.text = if (biography.isEmpty()) "-" else biography
        biography_container.setOnClickListener { biography_textview.toggle() }
    }

    private fun setFilmography(credits: PersonCreditList<CreditBasic>) {
        setFilmographyFilters(credits)

        personCreditsAdapter = PersonCreditsAdapter(getSortedCreditList(credits))

        filmography_recyclerview.apply {
            isNestedScrollingEnabled = false
            adapter = personCreditsAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    val dateTimeStrToLocalDateTime: (String?) -> LocalDate = {
        if (!it.isNullOrEmpty()) LocalDate.parse(it) else LocalDate.MAX
    }

    private fun getSortedCreditList(credits: PersonCreditList<CreditBasic>): List<CreditBasic> {
        val creditList = credits.cast.union(credits.crew).toList()

        return creditList.sortedByDescending {
            when (it) {
                is CreditMovieBasic -> dateTimeStrToLocalDateTime(it.releaseDate)
                is CreditTVBasic -> dateTimeStrToLocalDateTime(it.firstAirDate)
                else -> throw IllegalArgumentException()
            }
        }
    }

    private fun setFilmographyFilters(credits: PersonCreditList<CreditBasic>) {
        val filters = mutableSetOf<String>()
        credits.crew.forEach {
            filters.add(it.department)
        }
        if (credits.cast.size > 0) filters.add("Acting")

        activeFilters = filters

        filters_recyclerview.apply {
            adapter = FiltersAdapter(filters.sorted(), this@PersonDetailActivity)
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }
    }

    override fun filterChanged(filter: String, active: Boolean) {
        if (active) {
            // Add filter
            activeFilters.add(filter)
        } else {
            // Remove filter
            activeFilters.remove(filter)
        }
        personCreditsAdapter?.filter(activeFilters)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return false
    }

    companion object {
        private const val INTENT_PERSON_ID = "person"

        fun newIntent(context: Context, person: PersonFind): Intent {
            val intent = Intent(context, PersonDetailActivity()::class.java)
            intent.putExtra(INTENT_PERSON_ID, person)
            return intent
        }
    }
}