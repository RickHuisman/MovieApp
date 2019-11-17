package com.rickh.movieapp.ui.discover;

import android.content.Context;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.rickh.movieapp.R;

import timber.log.Timber;

// TODO convert to kotlin
public class ToolbarExpandableSheet extends BaseExpandablePageLayout {

    private StateChangeListener stateChangeListener;
    private State currentState;
    private float elevationOnExpand;

    public enum State {
        COLLAPSING,
        COLLAPSED,
        EXPANDING,
        EXPANDED
    }

    public interface StateChangeListener {
        void onStateChange(State newState);
    }

    public ToolbarExpandableSheet(Context context, AttributeSet attrs) {
        super(context, attrs);
//        ButterKnife.bind(this, this); TODO
        elevationOnExpand = context.getResources().getDimensionPixelSize(R.dimen.subreddit_toolbar_sheet_elevation) - getElevation();
        elevationOnExpand = -getElevation();

        // Hide on start.
        currentState = State.COLLAPSED;
        executeOnMeasure(this, new Runnable() {
            @Override
            public void run() {
                setClippedDimensions(getWidth(), 0);
            }
        });

        // Avoid the shadows from showing up above the sheet. This is done by passing in the
        // center location of this sheet as the top-location for the shadow, essentially hiding
        // it behind this sheet.
        setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRect(0, getClippedRect().height() / 2, getClippedRect().width(), getClippedRect().height());
            }
        });
    }

    public boolean isExpandedOrExpanding() {
        return currentState == State.EXPANDED || currentState == State.EXPANDING;
    }

    public boolean isCollapsed() {
        return currentState == State.COLLAPSED;
    }

    public void expand() {
        if (currentState == State.EXPANDED || currentState == State.EXPANDING) {
            return;
        }

        animateDimensions(getWidth(), getHeight());
        animate()
                .setDuration(getAnimationDurationMillis())
                .setInterpolator(getAnimationInterpolator())
                .withStartAction(new Runnable() {
                    @Override
                    public void run() {
                        dispatchStateChangeCallback(State.EXPANDING);
                    }
                })
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        dispatchStateChangeCallback(State.EXPANDED);

                        animate()
                                .translationZ(elevationOnExpand)
                                .setDuration(150)
                                .setInterpolator(new DecelerateInterpolator())
                                .start();
                    }
                })
                .start();
    }

    public void collapse() {
        if (currentState == State.COLLAPSED || currentState == State.COLLAPSING) {
            return;
        }

        setTranslationZ(0f);

        animateDimensions(getWidth(), 0);
        animate()
                .setDuration(getAnimationDurationMillis())
                .setInterpolator(getAnimationInterpolator())
                .withStartAction(new Runnable() {
                    @Override
                    public void run() {
                        dispatchStateChangeCallback(State.COLLAPSING);
                    }
                })
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        dispatchStateChangeCallback(State.COLLAPSED);
                    }
                })
                .start();
    }

    public void hideOnOutsideClick(FrameLayout frameLayout) {
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isExpandedOrExpanding()) {
                    Timber.d("collapse");
                    collapse();
                }
            }
        });

//        frameLayout.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() { TODO remove
//            @Override
//            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
//                if (isExpandedOrExpanding()) {
//                    collapse();
//                    return true;
//                }
//                return super.onInterceptTouchEvent(rv, e);
//            }
//        });
    }

    public void setStateChangeListener(StateChangeListener listener) {
        stateChangeListener = listener;
    }

    public long getCollapseAnimationDuration() {
        return getAnimationDurationMillis();
    }

// ======== PUBLIC APIs END ======== //

    private void dispatchStateChangeCallback(State state) {
        currentState = state;
        stateChangeListener.onStateChange(state);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean touchLiesInsideVisibleSheet = getClippedRect().contains((int) ev.getX(), (int) ev.getY());
        boolean handledBySuper = super.dispatchTouchEvent(ev);
        return touchLiesInsideVisibleSheet || handledBySuper;
    }


    // TODO move this to a utility class

    /**
     * Execute a runnable when a <var>view</var>'s dimensions get measured and is laid out on the screen.
     */
    public static void executeOnMeasure(View view, Runnable onMeasureRunnable) {
        executeOnMeasure(view, false, onMeasureRunnable);
    }

    /**
     * Execute a runnable when a <var>view</var>'s dimensions get measured and is laid out on the screen.
     *
     * @param consumeOnPreDraw When true, the pre-draw event will be consumed so that it never reaches the
     *                         View. This way, the View will not be notified of its size until the next
     *                         draw pass.
     */
    public static void executeOnMeasure(final View view, final boolean consumeOnPreDraw, final Runnable onMeasureRunnable) {
        if (view.isInEditMode() || view.isLaidOut()) {
            onMeasureRunnable.run();
            return;
        }

        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (view.isLaidOut()) {
                    //noinspection deprecation
                    view.getViewTreeObserver().removeOnPreDrawListener(this);

                    onMeasureRunnable.run();

                    if (consumeOnPreDraw) {
                        return false;
                    }

                } else if (view.getVisibility() == View.GONE) {
                    Timber.w("View's visibility is set to Gone. It'll never be measured: %s", view);
                    view.getViewTreeObserver().removeOnPreDrawListener(this);
                }

                return true;
            }
        });
    }
}
