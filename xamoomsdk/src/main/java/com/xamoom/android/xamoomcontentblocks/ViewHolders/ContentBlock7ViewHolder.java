/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomcontentblocks.ViewHolders;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Style;

import java.util.HashMap;

/**
 * SoundcloudBlock
 */
public class ContentBlock7ViewHolder extends RecyclerView.ViewHolder {
  private static final String TAG = ContentBlock7ViewHolder.class.getSimpleName();

  private TextView mTitleTextView;
  private WebView mSoundCloudWebview;
  private boolean isSetup = false;
  private Integer mTextColor = null;

  private static HashMap<String, WebView> mWebCache = new HashMap<>();

  @SuppressLint("SetJavaScriptEnabled")
  public ContentBlock7ViewHolder(View itemView) {
    super(itemView);
    mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
    mSoundCloudWebview = (WebView) itemView.findViewById(R.id.soundcloudWebview);
    WebSettings webSettings = mSoundCloudWebview.getSettings();
    webSettings.setJavaScriptEnabled(true);
    mSoundCloudWebview.setWebViewClient(new WebViewClient() {
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        try {
          view.getContext().startActivity(
              new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        } catch (Exception e) {
          Log.e(TAG, e.toString());
        }
        return true;
      }
    });
    mSoundCloudWebview.setBackgroundColor(Color.TRANSPARENT);
    mSoundCloudWebview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
    if (Build.VERSION.SDK_INT >= 19) {
      mSoundCloudWebview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
    } else {
      mSoundCloudWebview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }
  }

  public void setupContentBlock(final ContentBlock contentBlock, boolean offline) {
    mTitleTextView.setVisibility(View.VISIBLE);

    if (mTextColor != null) {
      mTitleTextView.setTextColor(mTextColor);
    }

    if(contentBlock.getTitle() != null && !contentBlock.getTitle().equalsIgnoreCase(""))
      mTitleTextView.setText(contentBlock.getTitle());
    else {
      mTitleTextView.setVisibility(View.GONE);
    }

    if(!isSetup) {
      String soundCloudHTML = "<body style=\"margin: 0; padding: 0\">" +
          "<iframe width='100%%' height='100%%' scrolling='no'" +
          " frameborder='no' src='https://w.soundcloud.com/player/?url=%s&auto_play=false" +
          "&hide_related=true&show_comments=false&show_comments=false" +
          "&show_user=false&show_reposts=false&sharing=false&download=false&buying=false" +
          "&visual=true'></iframe>" +
          "<script src=\"https://w.soundcloud.com/player/api.js\" type=\"text/javascript\"></script>" +
          "</body>";
      String html = String.format(soundCloudHTML, contentBlock.getSoundcloudUrl());
      mSoundCloudWebview.loadData(html, "text/html", "utf-8");
      isSetup = true;

      mSoundCloudWebview.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
        @Override
        public void onViewAttachedToWindow(View v) {
        }

        @Override
        public void onViewDetachedFromWindow(View v) {
          WebView webview = (WebView)v;
          webview.reload();
        }
      });
    }
  }

  public void setStyle(Style style) {
    if (style != null && style.getForegroundFontColor() != null) {
      mTextColor = Color.parseColor(style.getForegroundFontColor());
    }
  }
}