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
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xamoom.android.xamoomcontentblocks.Config;
import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Storage.FileManager;

import java.io.File;
import java.io.IOException;

/**
 * DownloadBlock
 */
public class ContentBlock8ViewHolder extends RecyclerView.ViewHolder {
  private static final int[] ATTRS = {
      R.attr.calendar_background_color, R.attr.calendar_tint_color,
      R.attr.contact_background_color, R.attr.contact_tint_color,
      R.attr.default_background_color, R.attr.default_tint_color
  };
  private static final int CALENDAR_BACKGROUND_COLOR = 0;
  private static final int CALENDAR_TINT_COLOR = 1;
  private static final int CONTACT_BACKGROUND_COLOR = 2;
  private static final int CONTACT_TINT_COLOR = 3;
  private static final int DEFAULT_BACKGROUND_COLOR = 4;
  private static final int DEFAULT_TINT_COLOR = 5;

  private Fragment mFragment;
  private TextView mTitleTextView;
  private TextView mContentTextView;
  private ImageView mIconImageView;
  private LinearLayout mRootLayout;
  private FileManager mFileManager;

  public ContentBlock8ViewHolder(View itemView, Fragment fragment) {
    super(itemView);
    mRootLayout = (LinearLayout) itemView.findViewById(R.id.downloadBlockLinearLayout);
    mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
    mContentTextView = (TextView) itemView.findViewById(R.id.contentTextView);
    mIconImageView = (ImageView) itemView.findViewById(R.id.iconImageView);
    mFragment = fragment;
    mFileManager = FileManager.getInstance(fragment.getContext());
  }

  public void setupContentBlock(final ContentBlock contentBlock, final boolean offline) {
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
        Uri fileUri = null;
        if (!offline) {
          fileUri = Uri.parse(contentBlock.getFileId());
          Intent i = new Intent(Intent.ACTION_VIEW, fileUri);
          mFragment.getActivity().startActivity(i);
          return;
        }

        startShareIntent(contentBlock.getFileId());
      }
    });

    TypedArray ta = mFragment.getContext()
        .obtainStyledAttributes(R.style.ContentBlocksTheme_Download, R.styleable.Download);

    int backgroundColor = 0;
    int tintColor = 0;
    switch (contentBlock.getDownloadType()) {
      case 0:
        backgroundColor = ta.getResourceId(R.styleable.Download_contact_background_color, 0);
        tintColor = ta.getColor(R.styleable.Download_contact_tint_color, Color.BLACK);
        mIconImageView.setImageResource(R.drawable.ic_account_plus);
        break;
      case 1:
        backgroundColor = ta.getResourceId(R.styleable.Download_calendar_background_color, 0);
        tintColor = ta.getColor(R.styleable.Download_calendar_tint_color, Color.BLACK);
        mIconImageView.setImageResource(R.drawable.ic_calendar);
        break;
      default:
        backgroundColor = ta.getResourceId(R.styleable.Download_download_default_background_color, 0);
        tintColor = ta.getColor(R.styleable.Download_download_default_tint_color, Color.BLACK);
        mIconImageView.setImageResource(R.drawable.ic_web);
        break;
    }

    mRootLayout.setBackgroundResource(backgroundColor);
    mIconImageView.setColorFilter(tintColor);
    mTitleTextView.setTextColor(tintColor);
    mContentTextView.setTextColor(tintColor);
  }

  private void startShareIntent(String fileUrl) {
    File file = null;
    try {
      file = mFileManager.getFile(fileUrl);
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (file == null) {
      fileNotFoundToast();
      return;
    }

    Uri fileUri = FileProvider.getUriForFile(mFragment.getContext(),
        Config.AUTHORITY, file);

    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setDataAndType(fileUri, mFragment.getContext().getContentResolver().getType(fileUri));
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

    mFragment.getActivity().startActivity(intent);
  }

  private void fileNotFoundToast() {
    Toast.makeText(mFragment.getContext(), R.string.file_not_found, Toast.LENGTH_LONG).show();
  }

  public void setFileManager(FileManager fileManager) {
    mFileManager = fileManager;
  }
}
