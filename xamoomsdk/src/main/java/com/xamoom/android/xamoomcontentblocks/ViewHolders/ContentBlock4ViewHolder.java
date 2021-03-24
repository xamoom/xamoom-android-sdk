/*
 * Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the root of this project.
 */

package com.xamoom.android.xamoomcontentblocks.ViewHolders;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xamoom.android.XamoomContentWebViewActivity;
import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;

import java.util.ArrayList;

/**
 * LinkBlock
 */
public class ContentBlock4ViewHolder extends RecyclerView.ViewHolder {

  private Fragment mFragment;
  private LinearLayout mRootLayout;
  private TextView mTitleTextView;
  private TextView mContentTextView;
  private ImageView mIcon;
  private ArrayList<String> urls;

  public ContentBlock4ViewHolder(View itemView, Fragment fragment, @Nullable ArrayList<String> urls) {
    super(itemView);
    mFragment = fragment;
    mRootLayout = (LinearLayout) itemView.findViewById(R.id.linkBlockLinearLayout);
    mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
    mContentTextView = (TextView) itemView.findViewById(R.id.contentTextView);
    mIcon = (ImageView) itemView.findViewById(R.id.iconImageView);
    this.urls = urls;
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public void setupContentBlock(final ContentBlock contentBlock, boolean offline) {
    if(contentBlock.getTitle() != null && !contentBlock.getTitle().equalsIgnoreCase("")) {
      mTitleTextView.setText(contentBlock.getTitle());
    } else {
      mTitleTextView.setText(null);
    }

    if(contentBlock.getText() != null && !contentBlock.getText().equalsIgnoreCase("")) {
      mContentTextView.setText(contentBlock.getText());
    } else {
      mContentTextView.setText(null);
    }

    mRootLayout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String contentUrl = contentBlock.getLinkUrl();

        if (urls == null) {
          Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(contentBlock.getLinkUrl()));
          mFragment.getActivity().startActivity(i);
          return;
        }

        boolean openInternal = false;

        for (int i = 0; i < urls.size(); i++) {
          String url = urls.get(i);
          if(contentUrl.contains("mailto")) {
            break;
          }
          if (contentUrl.contains(url)) {
            openInternal = true;
            break;
          }
        }
        if (openInternal) {
          Intent intent = new Intent(mFragment.getActivity(), XamoomContentWebViewActivity.class);
          intent.putExtra("url", contentUrl);
          mFragment.getActivity().startActivity(intent);
        } else {
          Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(contentBlock.getLinkUrl()));
          mFragment.getActivity().startActivity(i);
        }
      }
    });
    mIcon.setColorFilter(Color.BLACK);

    TypedArray ta = mFragment.getContext()
            .obtainStyledAttributes(R.style.ContentBlocksTheme_Links, R.styleable.Links);

    int backgroundColor = 0;
    int tintColor = 0;
    mRootLayout.setVisibility(View.VISIBLE);
    switch (contentBlock.getLinkType()) {
      case 0:
        backgroundColor = ta.getResourceId(R.styleable.Links_facebook_background_color, 0);
        tintColor = ta.getColor(R.styleable.Links_facebook_tint_color, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_facebook);
        break;
      case 1:
        backgroundColor = ta.getResourceId(R.styleable.Links_twitter_background_color, 0);
        tintColor = ta.getColor(R.styleable.Links_twitter_tint_color, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_twitter);
        break;
      case 2:
        backgroundColor = ta.getResourceId(R.styleable.Links_web_background_color, 0);
        tintColor = ta.getColor(R.styleable.Links_web_tint_color, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_web);
        break;
      case 3:
        backgroundColor = ta.getResourceId(R.styleable.Links_shop_background_color, 0);
        tintColor = ta.getColor(R.styleable.Links_shop_tint_color, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_cart);
        break;
      case 4:
        backgroundColor = ta.getResourceId(R.styleable.Links_wikipedia_background_color, 0);
        tintColor = ta.getColor(R.styleable.Links_wikipedia_tint_color, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_wikipedia);
        break;
      case 5:
        backgroundColor = ta.getResourceId(R.styleable.Links_linkedin_background_color, 0);
        tintColor = ta.getColor(R.styleable.Links_linkedin_tint_color, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_linkedin_box);
        break;
      case 6:
        backgroundColor = ta.getResourceId(R.styleable.Links_flickr_background_color, 0);
        tintColor = ta.getColor(R.styleable.Links_flickr_tint_color, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_flickr9);
        break;
      case 7:
        backgroundColor = ta.getResourceId(R.styleable.Links_soundcloud_background_color, 0);
        tintColor = ta.getColor(R.styleable.Links_soundcloud_tint_color, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_soundcloud);
        break;
      case 8:
        backgroundColor = ta.getResourceId(R.styleable.Links_itunes_background_color, 0);
        tintColor = ta.getColor(R.styleable.Links_itunes_tint_color, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_itunes);
        break;
      case 9:
        backgroundColor = ta.getResourceId(R.styleable.Links_youtube_background_color, 0);
        tintColor = ta.getColor(R.styleable.Links_youtube_tint_color, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_youtube_play);
        break;
      case 10:
        backgroundColor = ta.getResourceId(R.styleable.Links_google_background_color, 0);
        tintColor = ta.getColor(R.styleable.Links_google_tint_color, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_google_plus);
        break;
      case 11:
        backgroundColor = ta.getResourceId(R.styleable.Links_phone_background_color, 0);
        tintColor = ta.getColor(R.styleable.Links_phone_tint_color, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_phone);
        break;
      case 12:
        backgroundColor = ta.getResourceId(R.styleable.Links_email_background_color, 0);
        tintColor = ta.getColor(R.styleable.Links_email_tint_color, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_email);
        break;
      case 13:
        backgroundColor = ta.getResourceId(R.styleable.Links_spotify_background_color, 0);
        tintColor = ta.getColor(R.styleable.Links_spotify_tint_color, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_spotify);
        break;
      case 14:
        backgroundColor = ta.getResourceId(R.styleable.Links_navigation_background_color, 0);
        tintColor = ta.getColor(R.styleable.Links_navigation_tint_color, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_navigation);
        break;
      case 15:
        backgroundColor = ta.getResourceId(R.styleable.Links_apple_background_color, 0);
        tintColor = ta.getColor(R.styleable.Links_apple_tint_color, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_apple);
        break;
      case 16:
        backgroundColor = ta.getResourceId(R.styleable.Links_android_background_color, 0);
        tintColor = ta.getColor(R.styleable.Links_android_tint_color, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_google_play_brands);
        break;
      case 17:
        backgroundColor = ta.getResourceId(R.styleable.Links_windows_background_color, 0);
        tintColor = ta.getColor(R.styleable.Links_windows_tint_color, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_windows);
        break;
      case 18:
        backgroundColor = ta.getResourceId(R.styleable.Links_instagram_background_color, 0);
        tintColor = ta.getColor(R.styleable.Links_instagram_tint_color, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_instagram);
        break;
      case 19:
        backgroundColor = ta.getResourceId(R.styleable.Links_sms_background_color, 0);
        tintColor = ta.getColor(R.styleable.Links_sms_tint_color, Color.WHITE);
        mIcon.setImageResource(R.drawable.ic_sms);
        break;
      case 20:
        backgroundColor = ta.getResourceId(R.styleable.Links_whatsapp_background_color, 0);
        tintColor = ta.getColor(R.styleable.Links_whatsapp_tint_color, Color.WHITE);
        mIcon.setImageResource(R.drawable.ic_whatsapp);
        break;
      default:
        backgroundColor = ta.getResourceId(R.styleable.Links_default_background_color, 0);
        tintColor = ta.getColor(R.styleable.Links_default_tint_color, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_web);
        break;
    }

    ta.recycle();

    String isBackgroundImage = PreferenceManager.getDefaultSharedPreferences(mFragment.getContext()).getString("is_background_image", null);
    if(isBackgroundImage.equals("true")){
      mRootLayout.setBackground(mFragment.getContext().getDrawable(R.drawable.background_image));
    } else {
      mRootLayout.setBackgroundResource(backgroundColor);
    }
    mIcon.setColorFilter(tintColor);
    mTitleTextView.setTextColor(tintColor);
    mContentTextView.setTextColor(tintColor);
  }

  public LinearLayout getRootLayout() {
    return mRootLayout;
  }

  public TextView getTitleTextView() {
    return mTitleTextView;
  }

  public TextView getContentTextView() {
    return mContentTextView;
  }

  public ImageView getIcon() {
    return mIcon;
  }
}