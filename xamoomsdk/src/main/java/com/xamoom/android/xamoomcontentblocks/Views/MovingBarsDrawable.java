/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomcontentblocks.Views;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.xamoom.android.xamoomsdk.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MovingBarsDrawable extends Drawable implements ValueAnimator.AnimatorUpdateListener {
  private static final String TAG = MovingBarsDrawable.class.getSimpleName();
  private static final int DURATION = 600;
  private static final float MARGIN = 3.7f;
  private static final float STROKE_WIDTH = 9.4f;
  private static final float START_HEIGHT = 7.5f;

  private HashMap<ValueAnimator, Path> mValueAnimators = new HashMap<>(3);
  private ArrayList<Path> mPaths = new ArrayList<>(3);
  private HashMap<Path, Integer> mHeights = new HashMap<>(3);
  private Paint mPaint;
  private Rect mBounds;
  private boolean shouldStop;

  private int strokeWidth;
  private int margin;
  private int startHeight;

  public MovingBarsDrawable(Context context, AttributeSet attrs) {
    mBounds = getBounds();

    float density = context.getResources().getDisplayMetrics().density;

    strokeWidth = (int) (STROKE_WIDTH * density);
    margin = (int) (MARGIN * density);
    startHeight = (int) (START_HEIGHT * density);

    TypedArray a = context.obtainStyledAttributes(
        attrs,
        R.styleable.MovingBarsView);
    mPaint = new Paint();
    mPaint.setColor(a.getColor(R.styleable.MovingBarsView_barColor, Color.BLACK));
    a.recycle();
    mPaint.setStrokeWidth(strokeWidth);
    mPaint.setStyle(Paint.Style.STROKE);

    for (int i = 0; i < 3; i++) {
      Path path = new Path();
      mPaths.add(path);
      mHeights.put(path, startHeight);
    }
  }

  public void startAnimating() {
    shouldStop = false;

    if (mValueAnimators.size() == 0) {
      for (Path path : mPaths) {
        int height = mHeights.get(path);
        int animHeight = calculateAnimationHeight(false);

        ValueAnimator valueAnimator = ValueAnimator.ofInt(height,
            animHeight);
        valueAnimator.setDuration(DURATION);
        valueAnimator.addUpdateListener(this);
        valueAnimator.addListener(mAnimatorListener);

        mValueAnimators.put(valueAnimator, path);
        valueAnimator.start();
      }
    } else {
      for (ValueAnimator animator : mValueAnimators.keySet()) {
        Path path = mValueAnimators.get(animator);
        int height = mHeights.get(path);
        int animHeight = calculateAnimationHeight(false);
        animator.setIntValues(height, animHeight);
        animator.start();
      }
    }
  }

  public void stopAnimation() {
    shouldStop = true;
    startEndAnimation();
  }

  private void startEndAnimation() {
    for (ValueAnimator animator : mValueAnimators.keySet()) {
      Path path = mValueAnimators.get(animator);
      int height = (int) animator.getAnimatedValue();
      animator.setIntValues(height, startHeight);
      animator.start();
    }
  }

  private int calculateAnimationHeight(boolean end) {
    int maxHeight = mBounds.height();

    int animHeight = startHeight;
    if (!end) {
      Random r = new Random();
      animHeight = r.nextInt(maxHeight);
    }

    return animHeight;
  }

  @Override
  public void draw(@NonNull Canvas canvas) {
    int count = 0;
    for (Path path : mPaths) {
      int x = (strokeWidth/2) + (strokeWidth * count) + (margin * count);
      x = getBounds().width() - x; // right to left
      int height = getBounds().height() - mHeights.get(path);

      path.moveTo(x, getBounds().height());
      path.lineTo(x, height);
      canvas.drawPath(path, mPaint);

      count = count + 1;
    }
  }

  @Override
  public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

  }

  @Override
  public void setColorFilter(@Nullable ColorFilter colorFilter) {

  }

  @Override
  public int getOpacity() {
    return PixelFormat.TRANSLUCENT;
  }

  private Animator.AnimatorListener mAnimatorListener = new Animator.AnimatorListener() {
    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
      Path path = mValueAnimators.get(animation);

      if (!shouldStop) {
        int height = mHeights.get(path);
        int animHeight = calculateAnimationHeight(false);

        ((ValueAnimator) animation).setIntValues(height, animHeight);
        animation.start();
      }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
  };

  @Override
  public void onAnimationUpdate(ValueAnimator animator) {
    Path path = mValueAnimators.get(animator);
    path.reset();

    int height = (int) animator.getAnimatedValue();
    mHeights.put(path, height);
    invalidateSelf();
  }
}
