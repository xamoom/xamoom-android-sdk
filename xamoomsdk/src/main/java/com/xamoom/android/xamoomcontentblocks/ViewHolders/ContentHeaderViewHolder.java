/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomcontentblocks.ViewHolders;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
  private WebView mWebView;
  private String mLinkColor = "00F";
  private Style mStyle;
  private float mTextSize = 18.0f;

  public ContentHeaderViewHolder(View itemView) {
    super(itemView);
    mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
    mWebView = (WebView) itemView.findViewById(R.id.webView);
    mWebView.setBackgroundColor(Color.TRANSPARENT);

    WebSettings webSettings = mWebView.getSettings();
    webSettings.setDefaultFontSize((int) (mTextSize));
    webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
    webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
    webSettings.setAppCacheEnabled(false);
    webSettings.setBlockNetworkImage(true);
    webSettings.setLoadsImagesAutomatically(true);
    webSettings.setGeolocationEnabled(false);
    webSettings.setNeedInitialFocus(false);
    webSettings.setSaveFormData(false);

    //override to open links in Webbrowser
    mWebView.setWebViewClient(new WebViewClient() {
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.getContext().startActivity(
            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        return true;
      }
    });
  }


  public void setupContentBlock(ContentBlock contentblock, boolean offline){
    mTitleTextView.setVisibility(View.VISIBLE);
    mWebView.setVisibility(View.VISIBLE);

    if(contentblock.getTitle() != null) {
      mTitleTextView.setText(contentblock.getTitle());
    } else {
      mTitleTextView.setVisibility(View.GONE);
      LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mWebView.getLayoutParams();
      params.setMargins(0,0,0,0);
      mWebView.setLayoutParams(params);
    }

    if((contentblock.getText() != null) && !(contentblock.getText().equalsIgnoreCase("<p><br></p>"))) {
      String style = "<style type=\"text/css\">html, body {margin: 0; padding: 0dp;} a {color: #"+mLinkColor+"}</style>";
      String htmlAsString = String.format("%s%s", style, contentblock.getText());

      mWebView.loadDataWithBaseURL(null, htmlAsString, "text/html", "UTF-8", null);
    } else {
      mWebView.setVisibility(View.GONE);
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
