package com.xamoom.android.xamoomcontentblocks.Helper;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

public class LinePagerIndicatorDecoration extends RecyclerView.ItemDecoration {

    private int colorActive = 0xFF666666;
    private int colorInactive = 0xFFAAAAAA;

    private static final float DP = Resources.getSystem().getDisplayMetrics().density;

    /**
     * Height of the space the indicator takes up at the bottom of the view.
     */
    private final int mIndicatorHeight = (int) (DP * 8);

    /**
     * Indicator stroke width.
     */
    private final float mIndicatorStrokeWidth = DP * 2;

    /**
     * Indicator width.
     */
    private final float mIndicatorItemLength = DP * 8;
    /**
     * Padding between indicators.
     */
    private final float mIndicatorItemPadding = DP * 12;

    /**
     * Some more natural animation interpolation
     */
    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

    private final Paint mPaint = new Paint();

    public LinePagerIndicatorDecoration() {
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mIndicatorStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        int itemCount = parent.getAdapter().getItemCount();

        // center horizontally, calculate width and subtract half from center
        float totalLength = mIndicatorItemLength * itemCount;
        float paddingBetweenItems = Math.max(0, itemCount - 1) * mIndicatorItemPadding;
        float indicatorTotalWidth = totalLength + paddingBetweenItems;
        float indicatorStartX = (parent.getWidth() - indicatorTotalWidth) / 2F;

        // center vertically in the allotted space
        float indicatorPosY = parent.getHeight() - mIndicatorHeight/2 - mIndicatorStrokeWidth/2;

        drawInactiveIndicators(c, indicatorStartX, indicatorPosY, itemCount);


        // find active page (which should be highlighted)
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        int activePosition = layoutManager.findFirstVisibleItemPosition();
        if (activePosition == RecyclerView.NO_POSITION) {
            return;
        }

        // find offset of active page (if the user is scrolling)
        final View activeChild = layoutManager.findViewByPosition(activePosition);
        int left = activeChild.getLeft();
        int width = activeChild.getWidth();

        // on swipe the active item will be positioned from [-width, 0]
        // interpolate offset for smooth animation
        float progress = mInterpolator.getInterpolation(left * -1 / (float) width);

        drawHighlights(c, indicatorStartX, indicatorPosY, activePosition, progress, itemCount);
    }

    private void drawInactiveIndicators(Canvas c, float indicatorStartX, float indicatorPosY, int itemCount) {
        mPaint.setColor(colorInactive);

        // width of item indicator including padding
        final float itemWidth = mIndicatorItemLength + mIndicatorItemPadding;

        float start = indicatorStartX;
        for (int i = 0; i < itemCount; i++) {
            // draw the line for every item
            //    c.drawLine(start, indicatorPosY, start + mIndicatorItemLength, indicatorPosY, mPaint);
            c.drawCircle(start, indicatorPosY, (mIndicatorItemLength) / 2, mPaint);
            start += itemWidth;
        }
    }

    private void drawHighlights(Canvas c, float indicatorStartX, float indicatorPosY,
                                int highlightPosition, float progress, int itemCount) {
        mPaint.setColor(colorActive);

        // width of item indicator including padding
        final float itemWidth = mIndicatorItemLength + mIndicatorItemPadding;

        if (progress == 0F) {
            // no swipe, draw a normal indicator
            float highlightStart = indicatorStartX + itemWidth * highlightPosition;
            //    c.drawLine(highlightStart, indicatorPosY,highlightStart + mIndicatorItemLength, indicatorPosY, mPaint);
            c.drawCircle(highlightStart, indicatorPosY, (mIndicatorItemLength) / 2, mPaint);
        } else {
            float highlightStart = indicatorStartX + itemWidth * highlightPosition;

            c.drawCircle(highlightStart, indicatorPosY, (mIndicatorItemLength) / 2, mPaint);
/**
 float highlightStart = indicatorStartX + itemWidth * highlightPosition;
 // calculate partial highlight
 float partialLength = mIndicatorItemLength * progress;

 // draw the cut off highlight
 //    c.drawLine(highlightStart + partialLength, indicatorPosY,highlightStart + mIndicatorItemLength, indicatorPosY, mPaint);
 c.drawCircle(highlightStart + partialLength, indicatorPosY, (mIndicatorItemLength) / 2, mPaint);

 // draw the highlight overlapping to the next item as well
 if (highlightPosition < itemCount - 1) {
 highlightStart += itemWidth;
 //    c.drawLine(highlightStart, indicatorPosY,highlightStart + partialLength, indicatorPosY, mPaint);
 c.drawCircle(highlightStart, indicatorPosY, (mIndicatorItemLength) / 2, mPaint);

 }*/
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = mIndicatorHeight;
    }
}
