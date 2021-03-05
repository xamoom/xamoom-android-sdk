/*
 * Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the root of this project.
 */

package com.xamoom.android.xamoomcontentblocks.ViewHolders;

import android.content.Intent;
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
import com.xamoom.android.xamoomsdk.Storage.FileManager;

import java.io.File;
import java.io.IOException;

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
    if(isBackgroundImage.equals("true")){
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