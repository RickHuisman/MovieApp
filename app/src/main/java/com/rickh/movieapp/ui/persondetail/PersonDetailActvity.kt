package com.rickh.movieapp.ui.persondetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.omertron.themoviedbapi.model.credits.CreditBasic
import com.omertron.themoviedbapi.model.person.PersonCreditList
import com.omertron.themoviedbapi.model.person.PersonFind
import com.rickh.movieapp.R
import com.rickh.movieapp.tmdb.Result
import kotlinx.android.synthetic.main.activity_person_detail.*
import org.threeten.bp.LocalDateTime
import timber.log.Timber

class PersonDetailActivity : AppCompatActivity() {

    private lateinit var viewModel: PersonDetailViewModel

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

        biography_container.setOnClickListener {
            biography_textview.toggle()
        }

        viewModel.getPersonInfo(person.id).observe(this, Observer {
            when (it) {
                is Result.Success -> {
                    setBiography(it.data.biography)
                }
                is Result.Error -> {
                    Timber.d(it.exception)
                    // TODO showErrorState()
                }
            }
        })

        viewModel.getCredits(person.id).observe(this, Observer {
            when (it) {
                is Result.Success -> {
                    setFilmography(it.data)
                }
                is Result.Error -> {
                    Timber.d(it.exception)
                }
            }
        })
    }

    private fun setBiography(biography: String) {
        biography_textview.text = biography
    }

    private fun setFilmography(credits: PersonCreditList<CreditBasic>) {
        setFilmographyFilters(credits)

//        credits = credits.cast.union(credits.crew)
//        val date = LocalDate.parse(credit.releaseDate)

        filmography_recyclerview.apply {
            isNestedScrollingEnabled = false
            adapter = KnownForAdapter(getSortedCreditList(credits))
            layoutManager = LinearLayoutManager(context)
        }
    }

    val dateTimeStrToLocalDateTime: (String) -> LocalDateTime = {
        LocalDateTime.parse(it)
    }

    private fun getSortedCreditList(credits: PersonCreditList<CreditBasic>): List<CreditBasic> {
//        val test = credits.cast.sortedBy { dateTimeStrToLocalDateTime(it) }
        val creditList = credits.cast
        return creditList
    }

    private fun setFilmographyFilters(credits: PersonCreditList<CreditBasic>) {
        val filters = mutableSetOf<String>()
        credits.crew.forEach {
            filters.add(it.department)
        }
        if (credits.cast.size > 0) filters.add("Acting")

        filters_recyclerview.apply {
            adapter = FiltersAdapter(filters.sorted())
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }
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