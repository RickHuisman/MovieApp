package com.rickh.movieapp.ui.profile

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.rickh.movieapp.R
import com.rickh.movieapp.ui.widgets.ToolbarExpandableSheet
import timber.log.Timber

class UserProfileSheetView(context: Context) : FrameLayout(context) {

    private var parentSheet: ToolbarExpandableSheet? = null

    fun showIn(toolbarSheet: ToolbarExpandableSheet): UserProfileSheetView {
        val sheet = UserProfileSheetView(toolbarSheet.context)
        toolbarSheet.addView(
            sheet,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        sheet.setParentSheet(toolbarSheet)
        return sheet
    }

    init {
        View.inflate(context, R.layout.view_user_profile_sheet, this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        Timber.d("onAttachedToWindow()")
    }

    private fun setParentSheet(parentSheet: ToolbarExpandableSheet) {
        this.parentSheet = parentSheet
    }
}