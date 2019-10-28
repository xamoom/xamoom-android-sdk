/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomcontentblocks.ViewHolders;

import android.graphics.Color;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
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
  private String mLinkColor = null;
  private String mTextColor = null;
  private int childrenMargin;

  public ContentBlock0ViewHolder(View itemView) {
    super(itemView);
    mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
    mHtmlTextView = (HtmlTextView) itemView.findViewById(R.id.htmlTextView);
    mHtmlTextView.setRemoveTrailingNewLines(true);
    childrenMargin = itemView.getContext().getResources().getDimensionPixelSize(R.dimen.contentblock_children_margin);
  }

  public void setupContentBlock(ContentBlock contentBlock, boolean offline, @Nullable int maxLines){
    mTitleTextView.setVisibility(View.VISIBLE);
    mHtmlTextView.setVisibility(View.VISIBLE);
    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mHtmlTextView.getLayoutParams();
    params.setMargins(0,childrenMargin,0, 0);
    mHtmlTextView.setLayoutParams(params);

    if(contentBlock.getTitle() != null && !contentBlock.getTitle().equalsIgnoreCase("")) {
      mTitleTextView.setTextSize(mTextSize);
      mTitleTextView.setText(contentBlock.getTitle());

      if (mTextColor != null) {
        mTitleTextView.setTextColor(Color.parseColor(mTextColor));
      }
    } else {
      mTitleTextView.setVisibility(View.GONE);
      params = (LinearLayout.LayoutParams) mHtmlTextView.getLayoutParams();
      params.setMargins(0,0,0,0);
      mHtmlTextView.setLayoutParams(params);
    }

    if((contentBlock.getText() != null) && !(contentBlock.getText().equalsIgnoreCase("<p><br></p>"))) {

      if (maxLines > 0) {
        mHtmlTextView.setMaxLines(maxLines);
        mHtmlTextView.setEllipsize(TextUtils.TruncateAt.END);
      }

      mHtmlTextView.setHtmlText(contentBlock.getText());
    } else {
      mHtmlTextView.setVisibility(View.GONE);
    }

    if (mStyle != null && mStyle.getForegroundFontColor() != null) {
      mTitleTextView.setTextColor(Color.parseColor(mTextColor));
      mHtmlTextView.setTextColor(Color.parseColor(mTextColor));
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
