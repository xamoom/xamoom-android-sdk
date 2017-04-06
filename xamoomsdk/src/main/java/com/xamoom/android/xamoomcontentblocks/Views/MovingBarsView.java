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

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class MovingBarsView extends View {

  private MovingBarsDrawable mMovingBarsDrawable;

  public MovingBarsView(Context context) {
    super(context);
  }

  public MovingBarsView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);

    mMovingBarsDrawable= new MovingBarsDrawable();
    this.setBackground(mMovingBarsDrawable);
  }

  public void startAnimation() {
    mMovingBarsDrawable.startAnimating();
  }
}
