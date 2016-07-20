package com.xamoom.android.xamoomcontentblocks.ViewHolders;

import android.content.Intent;
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

  public void setupContentBlock(final ContentBlock contentBlock) {
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

    mRootLayout.setVisibility(View.VISIBLE);
    switch (contentBlock.getLinkType()) {
      case 0:
        mRootLayout.setBackgroundResource(R.color.facebook_linkblock_background_color);
        mIcon.setImageResource(R.drawable.ic_facebook);
        mTitleTextView.setTextColor(Color.WHITE);
        mContentTextView.setTextColor(Color.WHITE);
        break;
      case 1:
        mRootLayout.setBackgroundResource(R.color.twitter_linkblock_background_color);
        mIcon.setImageResource(R.drawable.ic_twitter);
        mTitleTextView.setTextColor(Color.WHITE);
        mContentTextView.setTextColor(Color.WHITE);
        break;
      case 2:
        mRootLayout.setBackgroundResource(R.color.default_linkblock_background_color);
        mIcon.setImageResource(R.drawable.ic_web);
        mTitleTextView.setTextColor(Color.parseColor("#333333"));
        mContentTextView.setTextColor(Color.parseColor("#333333"));
        break;
      case 3:
        mRootLayout.setBackgroundResource(R.color.amazon_linkblock_background_color);
        mIcon.setImageResource(R.drawable.ic_cart);
        mTitleTextView.setTextColor(Color.BLACK);
        mContentTextView.setTextColor(Color.BLACK);
        break;
      case 4:
        mRootLayout.setBackgroundResource(R.color.default_linkblock_background_color);
        mIcon.setImageResource(R.drawable.ic_wikipedia);
        mTitleTextView.setTextColor(Color.BLACK);
        mContentTextView.setTextColor(Color.BLACK);
        break;
      case 5:
        mRootLayout.setBackgroundResource(R.color.linkedin_linkblock_background_color);
        mIcon.setImageResource(R.drawable.ic_linkedin_box);
        mTitleTextView.setTextColor(Color.WHITE);
        mContentTextView.setTextColor(Color.WHITE);
        break;
      case 6:
        mRootLayout.setBackgroundResource(R.color.flickr_linkblock_background_color);
        mIcon.setImageResource(R.drawable.ic_flickr9);
        mTitleTextView.setTextColor(Color.WHITE);
        mContentTextView.setTextColor(Color.WHITE);
        break;
      case 7:
        mRootLayout.setBackgroundResource(R.color.soundcloud_linkblock_background_color);
        mIcon.setImageResource(R.drawable.ic_soundcloud);
        mTitleTextView.setTextColor(Color.WHITE);
        mContentTextView.setTextColor(Color.WHITE);
        break;
      case 8:
        mRootLayout.setBackgroundResource(R.color.default_linkblock_background_color);
        mIcon.setImageResource(R.drawable.ic_itunes);
        mTitleTextView.setTextColor(Color.parseColor("#333333"));
        mContentTextView.setTextColor(Color.parseColor("#333333"));
        break;
      case 9:
        mRootLayout.setBackgroundResource(R.color.youtube_linkblock_background_color);
        mIcon.setImageResource(R.drawable.ic_youtube_play);
        mTitleTextView.setTextColor(Color.WHITE);
        mContentTextView.setTextColor(Color.WHITE);
        break;
      case 10:
        mRootLayout.setBackgroundResource(R.color.googleplus_linkblock_background_color);
        mIcon.setImageResource(R.drawable.ic_google_plus);
        mTitleTextView.setTextColor(Color.WHITE);
        mContentTextView.setTextColor(Color.WHITE);
        break;
      case 11:
        mRootLayout.setBackgroundResource(R.color.default_linkblock_background_color);
        mIcon.setImageResource(R.drawable.ic_phone);
        mTitleTextView.setTextColor(Color.parseColor("#333333"));
        mContentTextView.setTextColor(Color.parseColor("#333333"));
        break;
      case 12:
        mRootLayout.setBackgroundResource(R.color.default_linkblock_background_color);
        mIcon.setImageResource(R.drawable.ic_email);
        mTitleTextView.setTextColor(Color.parseColor("#333333"));
        mContentTextView.setTextColor(Color.parseColor("#333333"));
        break;
      case 13:
        mRootLayout.setBackgroundResource(R.color.spotify_linkblock_background_color);
        mIcon.setImageResource(R.drawable.ic_spotify);
        mTitleTextView.setTextColor(Color.WHITE);
        mContentTextView.setTextColor(Color.WHITE);
        break;
      case 14:
        mRootLayout.setBackgroundResource(R.color.googlemaps_linkblock_background_color);
        mIcon.setImageResource(R.drawable.ic_navigation);
        mTitleTextView.setTextColor(Color.WHITE);
        mContentTextView.setTextColor(Color.WHITE);
        break;
      case 15:
        mRootLayout.setBackgroundResource(R.color.appstore_linkblock_background_color);
        mIcon.setImageResource(R.drawable.ic_apple);
        mTitleTextView.setTextColor(Color.WHITE);
        mContentTextView.setTextColor(Color.WHITE);
        break;
      case 16:
        mRootLayout.setBackgroundResource(R.color.playstore_downloadBlock_background_color);
        mIcon.setImageResource(R.drawable.ic_android);
        mTitleTextView.setTextColor(Color.WHITE);
        mContentTextView.setTextColor(Color.WHITE);
        break;
      case 17:
        mRootLayout.setBackgroundResource(R.color.windowsstore_downloadBlock_background_color);
        mIcon.setImageResource(R.drawable.ic_windows);
        mTitleTextView.setTextColor(Color.WHITE);
        mContentTextView.setTextColor(Color.WHITE);
        break;
      default:
        mRootLayout.setBackgroundResource(R.color.default_linkblock_background_color);
        mIcon.setImageResource(R.drawable.ic_web);
        mTitleTextView.setTextColor(Color.parseColor("#333333"));
        mContentTextView.setTextColor(Color.parseColor("#333333"));
        break;
    }
  }
}