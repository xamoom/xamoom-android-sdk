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
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;

/**
 * LinkBlock
 */
public class ContentBlock4ViewHolder extends RecyclerView.ViewHolder {
  private static final int[] ATTRS = {
      R.attr.facebook_background_color, R.attr.facebook_tint_color,
      R.attr.twitter_background_color, R.attr.twitter_tint_color,
      R.attr.web_background_color, R.attr.web_tint_color,
      R.attr.shop_background_color, R.attr.shop_tint_color,
      R.attr.wikipedia_background_color, R.attr.wikipedia_tint_color,
      R.attr.linkedin_background_color, R.attr.linkedin_tint_color,
      R.attr.flickr_background_color, R.attr.flickr_tint_color,
      R.attr.soundcloud_background_color, R.attr.soundcloud_tint_color,
      R.attr.itunes_background_color, R.attr.itunes_tint_color,
      R.attr.youtube_background_color, R.attr.youtube_tint_color,
      R.attr.google_background_color, R.attr.google_tint_color,
      R.attr.phone_background_color, R.attr.phone_tint_color,
      R.attr.email_background_color, R.attr.email_tint_color,
      R.attr.spotify_background_color, R.attr.spotify_tint_color,
      R.attr.navigation_background_color, R.attr.navigation_tint_color,
      R.attr.apple_background_color, R.attr.apple_tint_color,
      R.attr.android_background_color, R.attr.android_tint_color,
      R.attr.windows_background_color, R.attr.windows_tint_color,
      R.attr.instagram_background_color, R.attr.instagram_tint_color,
      R.attr.default_background_color, R.attr.default_tint_color
  };
  private static final int FACEBOOK_BACKGROUND_COLOR = 0;
  private static final int FACEBOOK_TINT_COLOR = 1;
  private static final int TWITTER_BACKGROUND_COLOR = 2;
  private static final int TWITTER_TINT_COLOR = 3;
  private static final int WEB_BACKGROUND_COLOR = 4;
  private static final int WEB_TINT_COLOR = 5;
  private static final int SHOP_BACKGROUND_COLOR = 6;
  private static final int SHOP_TINT_COLOR = 7;
  private static final int WIKIPEDIA_BACKGROUND_COLOR = 8;
  private static final int WIKIPEDIA_TINT_COLOR = 9;
  private static final int LINKEDIN_BACKGROUND_COLOR = 10;
  private static final int LINKEDIN_TINT_COLOR = 11;
  private static final int FLICKR_BACKGROUND_COLOR = 12;
  private static final int FLICKR_TINT_COLOR = 13;
  private static final int SOUNDCLOUD_BACKGROUND_COLOR = 14;
  private static final int SOUNDCLOUD_TINT_COLOR = 15;
  private static final int ITUNES_BACKGROUND_COLOR = 16;
  private static final int ITUNES_TINT_COLOR = 17;
  private static final int YOUTUBE_BACKGROUND_COLOR = 18;
  private static final int YOUTUBE_TINT_COLOR = 19;
  private static final int GOOGLE_BACKGROUND_COLOR = 20;
  private static final int GOOGLE_TINT_COLOR = 21;
  private static final int PHONE_BACKGROUND_COLOR = 22;
  private static final int PHONE_TINT_COLOR = 23;
  private static final int EMAIL_BACKGROUND_COLOR = 24;
  private static final int EMAIL_TINT_COLOR = 25;
  private static final int SPOTIFY_BACKGROUND_COLOR = 26;
  private static final int SPOTIFY_TINT_COLOR = 27;
  private static final int NAVIGATION_BACKGROUND_COLOR = 28;
  private static final int NAVIGATION_TINT_COLOR = 29;
  private static final int APPLE_BACKGROUND_COLOR = 30;
  private static final int APPLE_TINT_COLOR = 31;
  private static final int ANDROID_BACKGROUND_COLOR = 32;
  private static final int ANDROID_TINT_COLOR = 33;
  private static final int WINDOWS_BACKGROUND_COLOR = 34;
  private static final int WINDOWS_TINT_COLOR = 35;
  private static final int INSTAGRAM_BACKGROUND_COLOR = 36;
  private static final int INSTAGRAM_TINT_COLOR = 37;
  private static final int DEFAULT_BACKGROUND_COLOR = 38;
  private static final int DEFAULT_TINT_COLOR = 39;

  private Fragment mFragment;
  private LinearLayout mRootLayout;
  private TextView mTitleTextView;
  private TextView mContentTextView;
  private ImageView mIcon;

  public ContentBlock4ViewHolder(View itemView, Fragment fragment) {
    super(itemView);
    mFragment = fragment;
    mRootLayout = (LinearLayout) itemView.findViewById(R.id.linkBlockLinearLayout);
    mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
    mContentTextView = (TextView) itemView.findViewById(R.id.contentTextView);
    mIcon = (ImageView) itemView.findViewById(R.id.iconImageView);
  }

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
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(contentBlock.getLinkUrl()));
        mFragment.getActivity().startActivity(i);
      }
    });
    mIcon.setColorFilter(Color.BLACK);

    TypedArray ta = mFragment.getContext()
        .obtainStyledAttributes(R.style.ContentBlocksTheme_Links, ATTRS);

    int backgroundColor = 0;
    int tintColor = 0;
    mRootLayout.setVisibility(View.VISIBLE);
    switch (contentBlock.getLinkType()) {
      case 0:
        backgroundColor = ta.getResourceId(FACEBOOK_BACKGROUND_COLOR, 0);
        tintColor = ta.getColor(FACEBOOK_TINT_COLOR, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_facebook);
        break;
      case 1:
        backgroundColor = ta.getResourceId(TWITTER_BACKGROUND_COLOR, 0);
        tintColor = ta.getColor(TWITTER_TINT_COLOR, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_twitter);
        break;
      case 2:
        backgroundColor = ta.getResourceId(WEB_BACKGROUND_COLOR, 0);
        tintColor = ta.getColor(WEB_TINT_COLOR, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_web);
        break;
      case 3:
        backgroundColor = ta.getResourceId(SHOP_BACKGROUND_COLOR, 0);
        tintColor = ta.getColor(SHOP_TINT_COLOR, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_cart);
        break;
      case 4:
        backgroundColor = ta.getResourceId(WIKIPEDIA_BACKGROUND_COLOR, 0);
        tintColor = ta.getColor(WIKIPEDIA_TINT_COLOR, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_wikipedia);
        break;
      case 5:
        backgroundColor = ta.getResourceId(LINKEDIN_BACKGROUND_COLOR, 0);
        tintColor = ta.getColor(LINKEDIN_TINT_COLOR, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_linkedin_box);
        break;
      case 6:
        backgroundColor = ta.getResourceId(FLICKR_BACKGROUND_COLOR, 0);
        tintColor = ta.getColor(FLICKR_TINT_COLOR, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_flickr9);
        break;
      case 7:
        backgroundColor = ta.getResourceId(SOUNDCLOUD_BACKGROUND_COLOR, 0);
        tintColor = ta.getColor(SOUNDCLOUD_TINT_COLOR, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_soundcloud);
        break;
      case 8:
        backgroundColor = ta.getResourceId(ITUNES_BACKGROUND_COLOR, 0);
        tintColor = ta.getColor(ITUNES_TINT_COLOR, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_itunes);
        break;
      case 9:
        backgroundColor = ta.getResourceId(YOUTUBE_BACKGROUND_COLOR, 0);
        tintColor = ta.getColor(YOUTUBE_TINT_COLOR, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_youtube_play);
        break;
      case 10:
        backgroundColor = ta.getResourceId(GOOGLE_BACKGROUND_COLOR, 0);
        tintColor = ta.getColor(GOOGLE_TINT_COLOR, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_google_plus);
        break;
      case 11:
        backgroundColor = ta.getResourceId(PHONE_BACKGROUND_COLOR, 0);
        tintColor = ta.getColor(PHONE_TINT_COLOR, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_phone);
        break;
      case 12:
        backgroundColor = ta.getResourceId(EMAIL_BACKGROUND_COLOR, 0);
        tintColor = ta.getColor(EMAIL_TINT_COLOR, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_email);
        break;
      case 13:
        backgroundColor = ta.getResourceId(SPOTIFY_BACKGROUND_COLOR, 0);
        tintColor = ta.getColor(SPOTIFY_TINT_COLOR, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_spotify);
        break;
      case 14:
        backgroundColor = ta.getResourceId(NAVIGATION_BACKGROUND_COLOR, 0);
        tintColor = ta.getColor(NAVIGATION_TINT_COLOR, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_navigation);
        break;
      case 15:
        backgroundColor = ta.getResourceId(APPLE_BACKGROUND_COLOR, 0);
        tintColor = ta.getColor(APPLE_TINT_COLOR, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_apple);
        break;
      case 16:
        backgroundColor = ta.getResourceId(ANDROID_BACKGROUND_COLOR, 0);
        tintColor = ta.getColor(ANDROID_TINT_COLOR, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_android);
        break;
      case 17:
        backgroundColor = ta.getResourceId(WINDOWS_BACKGROUND_COLOR, 0);
        tintColor = ta.getColor(WINDOWS_TINT_COLOR, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_windows);
        break;
      case 18:
        backgroundColor = ta.getResourceId(INSTAGRAM_BACKGROUND_COLOR, 0);
        tintColor = ta.getColor(INSTAGRAM_TINT_COLOR, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_instagram);
        break;
      default:
        backgroundColor = ta.getResourceId(DEFAULT_BACKGROUND_COLOR, 0);
        tintColor = ta.getColor(DEFAULT_TINT_COLOR, Color.BLACK);
        mIcon.setImageResource(R.drawable.ic_web);
        break;
    }

    ta.recycle();

    mRootLayout.setBackgroundResource(backgroundColor);
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