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
import com.xamoom.android.xamoomsdk.Resource.Style;

/**
 * DownloadBlock
 */
public class ContentBlock8ViewHolder extends RecyclerView.ViewHolder {
  private Fragment mFragment;
  private TextView mTitleTextView;
  private TextView mContentTextView;
  private ImageView mIconImageView;
  private LinearLayout mRootLayout;
  private Style mStyle;

  public ContentBlock8ViewHolder(View itemView, Fragment fragment) {
    super(itemView);
    mRootLayout = (LinearLayout) itemView.findViewById(R.id.downloadBlockLinearLayout);
    mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
    mContentTextView = (TextView) itemView.findViewById(R.id.contentTextView);
    mIconImageView = (ImageView) itemView.findViewById(R.id.iconImageView);
    mFragment = fragment;
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
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(contentBlock.getFileId()));
        mFragment.getActivity().startActivity(i);
      }
    });

    switch (contentBlock.getDownloadType()) {
      case 0:
        mRootLayout.setBackgroundResource(R.color.vcf_downloadBlock_background_color);
        mIconImageView.setImageResource(R.drawable.ic_account_plus);
        mTitleTextView.setTextColor(Color.WHITE);
        mContentTextView.setTextColor(Color.WHITE);
        break;
      case 1:
        mRootLayout.setBackgroundResource(R.color.ical_downloadBlock_background_color);
        mIconImageView.setImageResource(R.drawable.ic_calendar);
        mTitleTextView.setTextColor(Color.WHITE);
        mContentTextView.setTextColor(Color.WHITE);
        break;
      default:
        mRootLayout.setBackgroundResource(R.color.default_linkblock_background_color);
        mIconImageView.setImageResource(R.drawable.ic_web);
        mTitleTextView.setTextColor(Color.parseColor("#333333"));
        mContentTextView.setTextColor(Color.parseColor("#333333"));
        break;
    }
  }

  public void setStyle(Style style) {
    mStyle = style;
  }
}
