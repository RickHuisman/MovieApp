package com.rickh.movieapp.ui.discover

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.rickh.movieapp.R
import timber.log.Timber

class DiscoverFilterSheetView(context: Context) : FrameLayout(context) {

    private var parentSheet: ToolbarExpandableSheet? = null

    init {
        View.inflate(context, R.layout.view_discover_filter_sheet, this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        Timber.d("onAttachedToWindow()")
    }

    fun setParentSheet(parentSheet: ToolbarExpandableSheet) {
        this.parentSheet = parentSheet
    }

    companion object {
        fun showIn(toolbarSheet: ToolbarExpandableSheet): DiscoverFilterSheetView {
            val filterSheet = DiscoverFilterSheetView(toolbarSheet.context)
            toolbarSheet.addView(
                filterSheet,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            filterSheet.setParentSheet(toolbarSheet)
            return filterSheet
        }
    }
}