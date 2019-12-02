package com.rickh.movieapp.ui.persondetail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.omertron.themoviedbapi.model.person.PersonFind
import com.rickh.movieapp.R
import com.rickh.movieapp.tmdb.Result
import kotlinx.android.synthetic.main.activity_person_detail.*
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

        viewModel.getPersonInfo(person.id).observe(this, Observer {
            when(it) {
                is Result.Success -> {
                    setBiography(it.data.biography)
                }
                is Result.Error -> {
                    Timber.d(it.exception)
                    // TODO showErrorState()
                }
            }
        })
    }

    private fun setBiography(biography: String) {
        biography_textview.text = biography
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