package com.xamoom.android.xamoomcontentblocks.ViewHolders;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Style;

/**
 * EbookBlock
 */
public class ContentBlock5ViewHolder extends RecyclerView.ViewHolder {

  private Fragment mFragment;
  private LinearLayout mRootLayout;
  private TextView mTitleTextView;
  private TextView mContentTextView;

  public ContentBlock5ViewHolder(View itemView, Fragment fragment) {
    super(itemView);
    mFragment = fragment;
    mRootLayout = (LinearLayout) itemView.findViewById(R.id.linkBlockLinearLayout);
    mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
    mContentTextView = (TextView) itemView.findViewById(R.id.contentTextView);
  }

  public void setupContentBlock(final ContentBlock contentBlock) {
    if(contentBlock.getTitle() != null && !contentBlock.getTitle().equalsIgnoreCase("")) {
      mTitleTextView.setText(contentBlock.getTitle());
    } else {
      mTitleTextView.setText(null);
    }

    if(contentBlock.getArtists() != null && !contentBlock.getArtists().equalsIgnoreCase("")) {
      mContentTextView.setText(contentBlock.getArtists());
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
  }
}