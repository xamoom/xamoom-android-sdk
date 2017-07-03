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
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MovingBarsDrawable extends Drawable implements ValueAnimator.AnimatorUpdateListener {
  private static final String TAG = MovingBarsDrawable.class.getSimpleName();
  private static final int DURATION = 600;
  private static final int MARGIN = 10;
  private static final int STROKE_WIDTH = 25;
  private static final int START_HEIGHT = 20;

  private HashMap<ValueAnimator, Path> mValueAnimators = new HashMap<>(3);
  private ArrayList<Path> mPaths = new ArrayList<>(3);
  private HashMap<Path, Integer> mHeights = new HashMap<>(3);
  private Paint mPaint;
  private Rect mBounds;
  private boolean shouldStop;

  public MovingBarsDrawable() {
    mPaint = new Paint();
    mPaint.setColor(0xff000000);
    mPaint.setStrokeWidth(STROKE_WIDTH);
    mPaint.setStyle(Paint.Style.STROKE);

    for (int i = 0; i < 3; i++) {
      Path path = new Path();
      mPaths.add(path);
      mHeights.put(path, START_HEIGHT);
    }

    mBounds = getBounds();
  }

  public void startAnimating() {
    shouldStop = false;

    for (Path path : mPaths) {
      ValueAnimator valueAnimator = createAnimator(path, false);
      valueAnimator.start();
    }
  }

  public void stopAnimation() {
    shouldStop = true;
    for (ValueAnimator animator : mValueAnimators.keySet()) {
      animator.end();
      onAnimationUpdate(animator);
    }

    startEndAnimation();
  }

  private void restartAnimator(Path path) {
    ValueAnimator valueAnimator = createAnimator(path, false);
    valueAnimator.start();
  }

  private void startEndAnimation() {
    for (Path path : mPaths) {
      ValueAnimator valueAnimator = createAnimator(path, true);
      valueAnimator.start();
    }
  }

  private ValueAnimator createAnimator(Path path, boolean end) {
    int height = mHeights.get(path);
    int maxHeight = mBounds.height();

    int animHeight = START_HEIGHT;
    if (!end) {
      Random r = new Random();
      animHeight = r.nextInt(maxHeight);
    }

    ValueAnimator valueAnimator = ValueAnimator.ofInt(height,
        animHeight);
    valueAnimator.setDuration(DURATION);
    valueAnimator.addUpdateListener(this);
    valueAnimator.addListener(mAnimatorListener);
    mValueAnimators.put(valueAnimator, path);

    return valueAnimator;
  }

  @Override
  public void draw(@NonNull Canvas canvas) {
    int count = 0;
    for (Path path : mPaths) {
      int x = (STROKE_WIDTH/2) + (STROKE_WIDTH * count) + (MARGIN * count);
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
        restartAnimator(path);
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
