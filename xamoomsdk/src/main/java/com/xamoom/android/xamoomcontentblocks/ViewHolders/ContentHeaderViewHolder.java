/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomcontentblocks.ViewHolders;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Style;

/**
 * Displays the content heading.
 */
public class ContentHeaderViewHolder extends RecyclerView.ViewHolder {
  private TextView mTitleTextView;
  private TextView mTextView;
  private TextView mCoverImageCopyRight;
  private String mLinkColor = "00F";
  private Style mStyle;
  private float mTextSize = 18.0f;

  public ContentHeaderViewHolder(View itemView) {
    super(itemView);
    mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
    mTextView = (TextView) itemView.findViewById(R.id.textView);
    mCoverImageCopyRight = itemView.findViewById(R.id.coverImageCopyRight);
  }

  public void setupContentBlock(ContentBlock contentblock, boolean offline){
    mTitleTextView.setVisibility(View.VISIBLE);
    mTextView.setVisibility(View.VISIBLE);

    if(contentblock.getTitle() != null) {
      mTitleTextView.setText(contentblock.getTitle());
    } else {
      mTitleTextView.setVisibility(View.GONE);
      LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mTextView.getLayoutParams();
      params.setMargins(0,0,0,0);
      mTextView.setLayoutParams(params);
    }

    if((contentblock.getText() != null) && !(contentblock.getText().equalsIgnoreCase("<p><br></p>"))) {
      mTextView.setText(contentblock.getText());
    } else {
      mTextView.setVisibility(View.GONE);
    }

    if (mStyle != null && mStyle.getForegroundFontColor() != null) {
      mTitleTextView.setTextColor(Color.parseColor(mStyle.getForegroundFontColor()));
      mTextView.setTextColor(Color.parseColor(mStyle.getForegroundFontColor()));
      mCoverImageCopyRight.setTextColor(Color.parseColor(mStyle.getForegroundFontColor()));
    }

    if (contentblock.getCoverImageCopyRight() != null && !contentblock.getCoverImageCopyRight().equals("")) {
      mCoverImageCopyRight.setText(contentblock.getCoverImageCopyRight());
    } else {
      mCoverImageCopyRight.setVisibility(View.GONE);
    }
  }

  public void setLinkColor(String mLinkColor) {
    this.mLinkColor = mLinkColor;
  }

  public void setTextSize(float textSize) {
    mTextSize = textSize;
  }

  public void setStyle(Style style) {
    mStyle = style;
  }
}
