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
import com.rickh.movieapp.data.tmdb.Result
import kotlinx.android.synthetic.main.activity_person_detail.*
import kotlinx.android.synthetic.main.activity_person_detail.error
import kotlinx.android.synthetic.main.activity_person_detail.haulerView
import kotlinx.android.synthetic.main.activity_person_detail.loading
import org.threeten.bp.LocalDate

class PersonDetailActivity : AppCompatActivity(), FilterChangedCallback {

    private lateinit var viewModel: PersonDetailViewModel
    private var activeFilters: MutableSet<String> = mutableSetOf()
    private lateinit var creditsAdapter: PersonCreditsAdapter
    private lateinit var filtersAdapter: FiltersAdapter

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

        filtersAdapter = FiltersAdapter(this@PersonDetailActivity)
        creditsAdapter = PersonCreditsAdapter()

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
        setCreditList(person.credits)
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

    private fun setCreditList(credits: PersonCreditList<CreditBasic>) {
        setCreditListFilters(credits)

        creditsAdapter.credits = getSortedCreditList(credits)

        filmography_recyclerview.apply {
            adapter = creditsAdapter
            isNestedScrollingEnabled = false
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

    private fun setCreditListFilters(credits: PersonCreditList<CreditBasic>) {
        activeFilters = getCreditListFilters(credits)
        filtersAdapter.filters = activeFilters.sorted()

        filters_recyclerview.apply {
            adapter = filtersAdapter
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }
    }

    private fun getCreditListFilters(credits: PersonCreditList<CreditBasic>): MutableSet<String> {
        val filters = mutableSetOf<String>()
        credits.crew.forEach { filters.add(it.department) }
        if (credits.cast.size > 0) filters.add("Acting")
        return filters
    }

    override fun onFilterChanged(filter: String, active: Boolean) {
        if (active) activeFilters.add(filter) else activeFilters.remove(filter)
        creditsAdapter.filter(filter, active)
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