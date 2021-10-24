package com.example.liststopwatchjava;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

public class CustomScrollingLayoutCallback extends LinearLayoutManager {

    private static final float MAX_ICON_PROGRESS = 0.65f;
    private float progressToCenter;
    private static final float MILLISECONDS_PER_INCH = 50f;
    private Context mContext;

    public CustomScrollingLayoutCallback(Context context) {
        super(context);
    }

    public CustomScrollingLayoutCallback(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }
/*
    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView,
                                       RecyclerView.State state, final int position) {

        LinearSmoothScroller smoothScroller =
                new LinearSmoothScroller(mContext) {

                    //This controls the direction in which smoothScroll looks
                    //for your view
                    @Override
                    public PointF computeScrollVectorForPosition
                    (int targetPosition) {
                        return CustomScrollingLayoutCallback.this
                                .computeScrollVectorForPosition(targetPosition);
                    }

                    //This returns the milliseconds it takes to
                    //scroll one pixel.
                    @Override
                    protected float calculateSpeedPerPixel
                    (DisplayMetrics displayMetrics) {
                        return MILLISECONDS_PER_INCH/displayMetrics.densityDpi;
                    }
                };

        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }
*/
    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int orientation = getOrientation();

        if (orientation == VERTICAL) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                float centerOffset = ((float) child.getHeight() / 2.0f) / (float) getHeight();
                float yRelativeToCenterOffset;

                if(child.getY() < 0)
                    yRelativeToCenterOffset = centerOffset;
                else
                    yRelativeToCenterOffset = (child.getY() / getHeight()) + centerOffset;

                // Normalize for center
                progressToCenter = Math.abs(0.5f - yRelativeToCenterOffset);
                // Adjust to the maximum scale
                progressToCenter = Math.min(progressToCenter, MAX_ICON_PROGRESS);

                child.setScaleX(1 - progressToCenter);
                child.setScaleY(1 - progressToCenter);
            }
            return super.scrollVerticallyBy(dy, recycler, state);
        } else {
            return 0;
        }
    }
}
