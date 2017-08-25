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
import android.text.Html;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.xamoom.htmltextview.HtmlTextView;
import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Style;

/**
 * Displays the text ContentBlock.
 */
public class ContentBlock0ViewHolder extends RecyclerView.ViewHolder {
  private TextView mTitleTextView;
  private HtmlTextView mHtmlTextView;
  private Style mStyle;
  private float mTextSize = 22.0f;
  private String mLinkColor = "#0000FF";
  private String mTextColor = "#000000";

  public ContentBlock0ViewHolder(View itemView) {
    super(itemView);
    mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
    mHtmlTextView = (HtmlTextView) itemView.findViewById(R.id.htmlTextView);
    //mWebView.setBackgroundColor(Color.TRANSPARENT);
  }

  public void setupContentBlock(ContentBlock contentBlock, boolean offline){
    mTitleTextView.setVisibility(View.VISIBLE);
    mHtmlTextView.setVisibility(View.VISIBLE);

    if(contentBlock.getTitle() != null && !contentBlock.getTitle().equalsIgnoreCase("")) {
      mTitleTextView.setTextSize(mTextSize);
      mTitleTextView.setText(contentBlock.getTitle());

      if (mTextColor != null) {
        mTitleTextView.setTextColor(Color.parseColor(mTextColor));
      }
    } else {
      mTitleTextView.setVisibility(View.GONE);
      LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mHtmlTextView.getLayoutParams();
      params.setMargins(0,0,0,0);
      mHtmlTextView.setLayoutParams(params);
    }

    if((contentBlock.getText() != null) && !(contentBlock.getText().equalsIgnoreCase("<p><br></p>"))) {
      mHtmlTextView.setHtmlText(contentBlock.getText());
    } else {
      mHtmlTextView.setVisibility(View.GONE);
    }
  }

  public void setStyle(Style style) {
    mStyle = style;

    if (style == null) {
      return;
    }

    if (style.getHighlightFontColor() != null) {
      mLinkColor = style.getHighlightFontColor();
    }

    if (style.getForegroundFontColor() != null) {
      mTextColor = style.getForegroundFontColor();
    }
  }

  public void setTextSize(float textSize) {
    mTextSize = textSize;
  }
}
