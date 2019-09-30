package com.rickh.movieapp.ui.widgets

import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.transition.Transition
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.view.*
import android.widget.PopupWindow
import com.rickh.movieapp.R
import com.rickh.movieapp.util.ViewUtils

/**
 * Mimics {@link PopupMenu}'s API 23+ entry animation and enables dismiss-on-outside-touch.
 */
abstract class PopupWindowWithMaterialTransition(context: Context) :
    PopupWindow(context, null, R.style.MovieAppPopupMenu) {

    private val windowManager: WindowManager =
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    fun showWithAnchor(anchorView: View, gravity: Int) {
        val anchorViewLoc = IntArray(2)
        anchorView.getLocationOnScreen(anchorViewLoc)
        showAtLocation(anchorView, gravity, Point(anchorViewLoc[0], anchorViewLoc[1]))
    }

    fun showAtLocation(anchorView: View, gravity: Int, showLocation: Point) {
        checkNotNull(contentView) { "setContentView was not called with a view to display." }

        // PopupWindow has a thin border around the content. This removes it.
        setBackgroundDrawable(null)

        // Dismiss on outside touch.
        isFocusable = true
        isOutsideTouchable = true

        val displayAboveAnchor = false
        val positionToShow = adjustPositionWithAnchorWithoutGoingOutsideWindow(
            showLocation,
            contentView, displayAboveAnchor
        )
        showAtLocation(anchorView, gravity, positionToShow.x, positionToShow.y)

        addBackgroundDimming()
        playPopupEnterTransition(positionToShow, anchorView)
    }

    // Used only for testing.
    fun showWithAnchor(anchorView: View) {
        showWithAnchor(anchorView, Gravity.TOP or Gravity.START)
    }

    override fun showAsDropDown(anchor: View) {
        throw UnsupportedOperationException()
    }

    override fun showAsDropDown(anchor: View, xoff: Int, yoff: Int) {
        throw UnsupportedOperationException()
    }

    override fun showAsDropDown(anchor: View, xoff: Int, yoff: Int, gravity: Int) {
        throw UnsupportedOperationException()
    }

    private fun addBackgroundDimming() {
        val decorView = contentView.rootView
        val params = decorView.layoutParams as WindowManager.LayoutParams
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        params.dimAmount = 0.3f
        windowManager.updateViewLayout(decorView, params)
    }

    private fun playPopupEnterTransition(showLocation: Point, anchorView: View) {
        val popupDecorView = contentView.rootView as ViewGroup
        val enterTransition = TransitionInflater.from(popupDecorView.context)
            .inflateTransition(R.transition.popupwindow_enter)

        // Postpone the enter transition after the first layout pass.
        val observer = popupDecorView.viewTreeObserver
        observer.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val observer = popupDecorView.viewTreeObserver
                observer?.removeOnGlobalLayoutListener(this)

                // Note: EpicenterTranslateClipReveal uses the epicenter's center location for animation.
                val epicenter =
                    calculateTransitionEpicenter(anchorView, popupDecorView, showLocation)
                enterTransition.epicenterCallback = object : Transition.EpicenterCallback() {
                    override fun onGetEpicenter(transition: Transition): Rect {
                        return epicenter
                    }
                }

                val count = popupDecorView.childCount
                for (i in 0 until count) {
                    val child = popupDecorView.getChildAt(i)
                    enterTransition.addTarget(child)
                    child.visibility = View.INVISIBLE
                }

                TransitionManager.beginDelayedTransition(popupDecorView, enterTransition)

                for (i in 0 until count) {
                    val child = popupDecorView.getChildAt(i)
                    child.visibility = View.VISIBLE
                }
            }
        })
    }

    protected abstract fun calculateTransitionEpicenter(
        anchor: View,
        popupDecorView: ViewGroup,
        showLocation: Point
    ): Rect

    private fun adjustPositionWithAnchorWithoutGoingOutsideWindow(
        showLocation: Point,
        contentView: View,
        displayAboveAnchor: Boolean
    ): Point {
        if (contentView.layoutParams != null) {
            contentView.measure(
                View.MeasureSpec.makeMeasureSpec(
                    contentView.layoutParams.width,
                    View.MeasureSpec.EXACTLY
                ),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )

        } else {
            contentView.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
        }
        val contentWidth = contentView.measuredWidth
        val contentHeight = contentView.measuredHeight

        val displaySize = Point()
        windowManager.defaultDisplay.getSize(displaySize)
        val screenWidth = displaySize.x
        val screenHeight = displaySize.y

        val statusBarHeight = ViewUtils.statusBarHeight(contentView.resources)
        val heightAvailableAboveShowPosition = showLocation.y - statusBarHeight

        var xPos = showLocation.x
        var yPos = showLocation.y

        // Show the popup below the content if there's not enough room available above.
        if (contentHeight > heightAvailableAboveShowPosition) {
            yPos = showLocation.y

        } else {
            // Display above the anchor view.
            if (displayAboveAnchor || yPos + contentHeight > screenHeight) {
                yPos = showLocation.y - contentHeight
            }
        }

        // Keep the right edge of the popup on the screen.
        if (xPos + contentWidth > screenWidth) {
            xPos = showLocation.x - (showLocation.x + contentWidth - screenWidth)
        }
        return Point(xPos, yPos)
    }
}