package com.xamoom.android.xamoomcontentblocks.ViewHolders;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

  public void setupContentBlock(final ContentBlock contentBlock, final boolean offline) {
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
        "com.xamoom.android.xamoomsdk.fileprovider", file);
    Intent shareIntent = ShareCompat.IntentBuilder.from(mFragment.getActivity())
        .setType(mFragment.getContext().getContentResolver().getType(fileUri))
        .setStream(fileUri)
        .getIntent();
    shareIntent.setData(fileUri);
    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

    mFragment.getActivity().startActivity(shareIntent);
  }

  private void fileNotFoundToast() {
    // TODO: convert to string xml
    Toast.makeText(mFragment.getContext(), "File not found", Toast.LENGTH_LONG).show();
  }

  public void setFileManager(FileManager fileManager) {
    mFileManager = fileManager;
  }
}