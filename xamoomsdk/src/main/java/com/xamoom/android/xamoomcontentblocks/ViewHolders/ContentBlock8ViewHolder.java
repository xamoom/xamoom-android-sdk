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

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Storage.DownloadError;
import com.xamoom.android.xamoomsdk.Storage.DownloadManager;
import com.xamoom.android.xamoomsdk.Storage.FileManager;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

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

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
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
      case 2:
        backgroundColor = ta.getResourceId(R.styleable.Download_gpx_background_color, 0);
        tintColor = ta.getColor(R.styleable.Download_gpx_tint_color, Color.BLACK);
        mIconImageView.setImageResource(R.drawable.ic_gpx);
        break;
      default:
        backgroundColor = ta.getResourceId(R.styleable.Download_download_default_background_color, 0);
        tintColor = ta.getColor(R.styleable.Download_download_default_tint_color, Color.BLACK);
        mIconImageView.setImageResource(R.drawable.ic_web);
        break;
    }

    if(mFragment.getContext().getString(R.string.is_background_image).equals("true")) {
      mRootLayout.setBackground(mFragment.getContext().getDrawable(R.drawable.background_image));
    } else {
      mRootLayout.setBackgroundResource(backgroundColor);
    }

    mIconImageView.setColorFilter(tintColor);
    mTitleTextView.setTextColor(tintColor);
    mContentTextView.setTextColor(tintColor);
  }

  private void startShareIntent(String fileUrl) {

    DownloadManager m = new DownloadManager(mFileManager);

    try {
      m.saveFileFromUrl(new URL(fileUrl), false, new DownloadManager.OnDownloadManagerCompleted() {
        @Override
        public void completed(String urlString) {
          try {
            File file = mFileManager.getFile(urlString);
            if (file == null && !file.exists()) {
              fileNotFoundToast();
              return;
            }

            String providerAuthorities = mFragment.getContext().getPackageName() + ".xamoomsdk.fileprovider";
            Uri fileUri = FileProvider.getUriForFile(mFragment.getContext(),
                    providerAuthorities, file);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(fileUri, mFragment.getContext().getContentResolver().getType(fileUri));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(Intent.EXTRA_STREAM, fileUri);
            mFragment.getActivity().startActivity(Intent.createChooser(intent, "ABC"));
          } catch (IOException e) {
            e.printStackTrace();
          }
        }

        @Override
        public void failed(String urlString, DownloadError downloadError) {
          fileNotFoundToast();
        }
      });
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
  }

  private void fileNotFoundToast() {
    Toast.makeText(mFragment.getContext(), R.string.file_not_found, Toast.LENGTH_LONG).show();
  }

  public void setFileManager(FileManager fileManager) {
    mFileManager = fileManager;
  }
}
