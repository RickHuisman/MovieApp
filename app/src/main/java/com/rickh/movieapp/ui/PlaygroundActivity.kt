package com.rickh.movieapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.rickh.movieapp.R
import com.rickh.movieapp.ui.profile.UserProfileSheetView
import com.rickh.movieapp.ui.widgets.ToolbarExpandableSheet
import kotlinx.android.synthetic.main.activity_playground.*

class PlaygroundActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playground)

//        setupToolbarSheet()
        button.setOnClickListener { showUserProfileSheet() }
    }

//    private fun setupToolbarSheet() {
//        toolbar_sheet.hideOnOutsideClick(content_container)
//        toolbar_sheet.setStateChangeListener { state ->
//            when (state) {
//                ToolbarExpandableSheet.State.EXPANDING -> {}
//                ToolbarExpandableSheet.State.EXPANDED -> {}
//                ToolbarExpandableSheet.State.COLLAPSING -> {}
//                ToolbarExpandableSheet.State.COLLAPSED -> {
//                    toolbar_sheet.removeAllViews()
//                    toolbar_sheet.collapse()
//                }
//            }
//        }
//    }

    private fun showUserProfileSheet() {
        val sheet = UserProfileSheetView(this).showIn(toolbar_sheet)
        sheet.post { toolbar_sheet.expand() }
    }

    override fun onBackPressed() {
        if (!toolbar_sheet.isCollapsed) {
            toolbar_sheet.collapse()
        } else {
            super.onBackPressed()
        }
    }
}