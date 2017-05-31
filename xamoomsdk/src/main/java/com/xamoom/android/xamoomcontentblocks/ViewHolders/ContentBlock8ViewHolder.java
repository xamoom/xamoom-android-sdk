/*
* Copyright 2017 by xamoom GmbH <apps@xamoom.com>
*
* This file is part of some open source application.
*
* Some open source application is free software: you can redistribute
* it and/or modify it under the terms of the GNU General Public
* License as published by the Free Software Foundation, either
* version 2 of the License, or (at your option) any later version.
*
* Some open source application is distributed in the hope that it will
* be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
* of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with xamoom-android-sdk. If not, see <http://www.gnu.org/licenses/>.
*
* author: Raphael Seher <raphael@xamoom.com>
*/

package com.xamoom.android.xamoomcontentblocks.ViewHolders;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
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

    switch (contentBlock.getDownloadType()) {
      case 0:
        mRootLayout.setBackgroundResource(R.color.vcf_downloadBlock_background_color);
        mIconImageView.setImageResource(R.drawable.ic_account_plus);
        mIconImageView.setColorFilter(Color.WHITE);
        mTitleTextView.setTextColor(Color.WHITE);
        mContentTextView.setTextColor(Color.WHITE);
        break;
      case 1:
        mRootLayout.setBackgroundResource(R.color.ical_downloadBlock_background_color);
        mIconImageView.setImageResource(R.drawable.ic_calendar);
        mIconImageView.setColorFilter(Color.WHITE);
        mTitleTextView.setTextColor(Color.WHITE);
        mContentTextView.setTextColor(Color.WHITE);
        break;
      default:
        mRootLayout.setBackgroundResource(R.color.default_linkblock_background_color);
        mIconImageView.setImageResource(R.drawable.ic_web);
        mIconImageView.setColorFilter(Color.parseColor("#333333"));
        mTitleTextView.setTextColor(Color.parseColor("#333333"));
        mContentTextView.setTextColor(Color.parseColor("#333333"));
        break;
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
