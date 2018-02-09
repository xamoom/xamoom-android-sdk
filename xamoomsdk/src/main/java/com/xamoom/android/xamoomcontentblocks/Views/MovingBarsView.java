/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomcontentblocks.Views;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v13.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

public class MovingBarsView extends View {

  private MovingBarsDrawable mMovingBarsDrawable;

  public MovingBarsView(Context context) {
    super(context);
  }

  public MovingBarsView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);

    mMovingBarsDrawable = new MovingBarsDrawable(context, attrs);
    Configuration config = getResources().getConfiguration();
    mMovingBarsDrawable.currentLayoutDirection = config.getLayoutDirection();
    this.setBackground(mMovingBarsDrawable);
  }

  public void startAnimation() {
    mMovingBarsDrawable.startAnimating();
  }

  public void stopAnimation() {
    mMovingBarsDrawable.stopAnimation();
  }
}
