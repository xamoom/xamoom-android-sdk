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

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xamoom.android.xamoomcontentblocks.XamoomContentFragment;
import com.xamoom.android.xamoomsdk.APICallback;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.Enums.ContentFlags;
import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Style;
import com.xamoom.android.xamoomsdk.Storage.FileManager;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;

import at.rags.morpheus.Error;

/**
 * ContentBlock
 */
public class ContentBlock6ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
  private XamoomContentFragment.OnXamoomContentFragmentInteractionListener mListener;
  private Context mContext;
  private TextView mTitleTextView;
  private TextView mDescriptionTextView;
  private ImageView mContentThumbnailImageView;
  private ProgressBar mProgressBar;
  private EnduserApi mEnduserApi;
  private Content mContent;
  private int mTextColor = Color.BLACK;
  private FileManager mFileManager;
  private LruCache<String, Content> mContentCache;

  public ContentBlock6ViewHolder(View itemView, Context context, EnduserApi enduserApi,
                                 LruCache<String, Content> contentCache,
                                 XamoomContentFragment.OnXamoomContentFragmentInteractionListener listener) {
    super(itemView);
    mListener = listener;
    mContext = context;
    mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
    mDescriptionTextView = (TextView) itemView.findViewById(R.id.descriptionTextView);
    mContentThumbnailImageView = (ImageView) itemView.findViewById(R.id.contentThumbnailImageView);
    mProgressBar = (ProgressBar) itemView.findViewById(R.id.contentProgressBar);
    mEnduserApi = enduserApi;
    mContentCache = contentCache;
    mFileManager = FileManager.getInstance(context);

    itemView.setOnClickListener(this);
  }

  public void setupContentBlock(ContentBlock contentBlock, boolean offline) {
    mContentThumbnailImageView.setImageDrawable(null);
    mTitleTextView.setText(null);
    mDescriptionTextView.setText(null);

    mProgressBar.setVisibility(View.VISIBLE);

    mContent = mContentCache.get(contentBlock.getContentId());
    if (mContent != null) {
      displayContent(mContent, offline);
      mProgressBar.setVisibility(View.GONE);
    } else {
      loadContent(contentBlock.getContentId(), offline);
    }

    mEnduserApi.setOffline(offline);
  }

  private void loadContent(final String contentId, final boolean offline) {
    if (mEnduserApi == null) {
      throw new NullPointerException("EnduserApi is null.");
    }

    mEnduserApi.getContent(contentId, EnumSet.of(ContentFlags.PREVIEW),new APICallback<Content, List<at.rags.morpheus.Error>>() {
      @Override
      public void finished(Content result) {
        mProgressBar.setVisibility(View.GONE);
        mContent = result;
        mContentCache.put(contentId, result);
        displayContent(result, offline);
      }

      @Override
      public void error(List<Error> error) {
        mProgressBar.setVisibility(View.GONE);
        if (error != null && error.get(0) != null) {
          Log.e("XamoomContentBlocks", error.get(0).getCode() +
              "\n Error Title: " + error.get(0).getTitle() +
              "\n Detail: " + error.get(0).getDetail() +
              "\n ContentId: " + contentId);
        }
      }
    });
  }

  private void displayContent(Content content, boolean offline) {
    mTitleTextView.setText(content.getTitle());
    mDescriptionTextView.setText(content.getDescription());
    mTitleTextView.setTextColor(mTextColor);
    mDescriptionTextView.setTextColor(mTextColor);

    String filePath = null;
    if (offline) {
      String imageUrl = mFileManager.getFilePath(content.getPublicImageUrl());
      if (imageUrl != null) {
        filePath = imageUrl;
      }
    } else {
      filePath = content.getPublicImageUrl();
    }

    Glide.with(mContext)
        .load(filePath)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .crossFade()
        .centerCrop()
        .into(mContentThumbnailImageView);
  }

  @Override
  public void onClick(View v) {
    if (mContent != null) {
      mListener.clickedContentBlock(mContent);
    }
  }

  public void setStyle(Style style) {
    if (style != null && style.getForegroundFontColor() != null) {
      mTextColor = Color.parseColor(style.getForegroundFontColor());
    }
  }

  public void setFileManager(FileManager fileManager) {
    mFileManager = fileManager;
  }
}