/*
* Copyright 2017 by xamoom GmbH <apps@xamoom.com>
*
* This file is part of some open source application.
*
* Some open source application is free software: you can redistribute
* it and/or modify it under the terms of the GNU General Public
* License as published by the Free Software Foundation, either
* version 2 of the License, or (at your option) any later version.
*
* Some open source application is distributed in the hope that it will
* be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
* of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with xamoom-android-sdk. If not, see <http://www.gnu.org/licenses/>.
*
* author: Raphael Seher <raphael@xamoom.com>
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
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class MovingBarsDrawable extends Drawable implements ValueAnimator.AnimatorUpdateListener {
  private Path mPath;
  private Paint mPaint;
  private ArrayList<Path> mPaths = new ArrayList<>(3);

  private ValueAnimator mAnimator;
  private int mHeight;

  public MovingBarsDrawable() {
    mPath = new Path();
    mPaths.add(mPath);

    mPaint = new Paint();
    mPaint.setColor(0xff000000);
    mPaint.setStrokeWidth(30);
    mPaint.setStyle(Paint.Style.STROKE);
    mHeight = 20;
  }

  public void startAnimating() {
    Rect b = getBounds();
    mAnimator = ValueAnimator.ofInt(mHeight, b.height());
    mAnimator.setDuration(800);
    mAnimator.addUpdateListener(this);
    mAnimator.addListener(mAnimatorListener);
    mAnimator.start();
  }

  @Override
  public void draw(@NonNull Canvas canvas) {
    mPath.moveTo(5, 0);
    mPath.lineTo(5, mHeight);
    canvas.drawPath(mPath, mPaint);
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
    mPath.reset();
    mHeight = (int) mAnimator.getAnimatedValue();

    invalidateSelf();
  }
}
