/*
 * Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the root of this project.
 */

package com.xamoom.android.xamoomcontentblocks.ViewHolders;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xamoom.android.xamoomcontentblocks.Config;
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
 * EbookBlock
 */
public class ContentBlock5ViewHolder extends RecyclerView.ViewHolder {

  private Fragment mFragment;
  private LinearLayout mRootLayout;
  private TextView mTitleTextView;
  private TextView mContentTextView;
  private FileManager mFileManager;

  public ContentBlock5ViewHolder(View itemView, Fragment fragment) {
    super(itemView);
    mFragment = fragment;
    mRootLayout = (LinearLayout) itemView.findViewById(R.id.linkBlockLinearLayout);
    mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
    mContentTextView = (TextView) itemView.findViewById(R.id.contentTextView);
    mFileManager = FileManager.getInstance(mFragment.getContext());
  }

  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  public void setupContentBlock(final ContentBlock contentBlock, final boolean offline) {
    if(contentBlock.getTitle() != null && !contentBlock.getTitle().equalsIgnoreCase("")) {
      mTitleTextView.setText(contentBlock.getTitle());
    } else {
      mTitleTextView.setText(null);
    }

    String isBackgroundImage = PreferenceManager.getDefaultSharedPreferences(mFragment.getContext()).getString("is_background_image", null);
    if(isBackgroundImage != null && isBackgroundImage.equals("true")){
      mRootLayout.setBackgroundDrawable(mFragment.getContext().getDrawable(R.drawable.background_image));
    }

    if(contentBlock.getArtists() != null && !contentBlock.getArtists().equalsIgnoreCase("")) {
      mContentTextView.setText(contentBlock.getArtists());
    } else {
      mContentTextView.setText(null);
    }

    mRootLayout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String fileTitle = contentBlock.getTitle();
        try {
          File file = mFileManager.getFile(contentBlock.getFileId(), fileTitle);
          if (file.exists()) {
            openFileInApp(file);
          } else {
            saveAndOpenFileFromUrl(contentBlock.getFileId(), fileTitle);
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
  }

  private void saveAndOpenFileFromUrl(String fileUrl, String fileName) {
    DownloadManager fileManager = new DownloadManager(mFileManager);
    try {
      fileManager.saveFileFromUrl(new URL(fileUrl), fileName, false, new DownloadManager.OnDownloadManagerCompleted() {
        @Override
        public void completed(String urlString) {
          try {
            File file = mFileManager.getFile(urlString, fileName);
            if (file == null && !file.exists()) {
              fileNotFoundToast();
              return;
            }
            openFileInApp(file);
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

  private void openFileInApp(File file) {
    String providerAuthorities = mFragment.getContext().getPackageName() + ".xamoomsdk.fileprovider";
    Uri fileUri = FileProvider.getUriForFile(mFragment.getContext(),
            providerAuthorities, file);

    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setDataAndType(fileUri, mFragment.getContext().getContentResolver().getType(fileUri));
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    intent.putExtra(Intent.EXTRA_STREAM, fileUri);
    try {
      mFragment.getActivity().startActivity(intent);
    } catch (ActivityNotFoundException e) {
      String PLAY_BOOKS_PACKAGE_NAME = "com.google.android.apps.books";
      mFragment.getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + PLAY_BOOKS_PACKAGE_NAME)));
    }
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
    Intent shareIntent = ShareCompat.IntentBuilder.from(mFragment.getActivity())
            .setType(mFragment.getContext().getContentResolver().getType(fileUri))
            .setStream(fileUri)
            .getIntent();
    shareIntent.setData(fileUri);
    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

    mFragment.getActivity().startActivity(shareIntent);
  }

  private void fileNotFoundToast() {
    Toast.makeText(mFragment.getContext(), R.string.file_not_found, Toast.LENGTH_LONG).show();
  }

  public void setFileManager(FileManager fileManager) {
    mFileManager = fileManager;
  }
}