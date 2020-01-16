package com.rickh.movieapp.ui.user

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.github.zagum.expandicon.ExpandIconView.LESS
import com.rickh.movieapp.R
import com.rickh.movieapp.data.login.LoginRepository
import com.rickh.movieapp.ui.widgets.ToolbarExpandableSheet
import kotlinx.android.synthetic.main.view_user_profile_sheet.view.*

class UserProfileSheetView(context: Context) : FrameLayout(context) {

    private var parentSheet: ToolbarExpandableSheet? = null
    private var loginRepository = LoginRepository.getInstance(context)

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

        loginRepository.user?.let { user ->
            setupToolbar(user.username)
            setupLogout()
        }

        userprofile_rated.setOnClickListener {
            context.startActivity(Intent(context, UserRatedActivity::class.java))
        }
    }

    private fun setupToolbar(username: String) {
        toolbar_title.text = username
        toolbar_title_arrow.setState(LESS, true)
        toolbar_title.setOnClickListener {
            parentSheet?.collapse()
        }
    }

    private fun setupLogout() {
        var confirmed = false
        userprofile_logout.setOnClickListener {
            userprofile_logout.text = context.getString(R.string.userprofile_confirm_logout)

            if (confirmed) {
                loginRepository.logout()
                parentSheet?.collapse()
            }
            confirmed = true
        }
    }

    private fun setParentSheet(parentSheet: ToolbarExpandableSheet) {
        this.parentSheet = parentSheet
    }
}