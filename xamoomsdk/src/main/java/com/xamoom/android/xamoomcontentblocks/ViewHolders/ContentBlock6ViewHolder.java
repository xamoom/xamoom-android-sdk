/*
 * Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the root of this project.
 */

package com.xamoom.android.xamoomcontentblocks.ViewHolders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.collection.LruCache;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xamoom.android.xamoomcontentblocks.XamoomContentFragment;
import com.xamoom.android.xamoomsdk.APICallback;
import com.xamoom.android.xamoomsdk.APIPasswordCallback;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.Enums.ContentFlags;
import com.xamoom.android.xamoomsdk.Enums.ContentReason;
import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Spot;
import com.xamoom.android.xamoomsdk.Resource.Style;
import com.xamoom.android.xamoomsdk.Storage.FileManager;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;

import at.rags.morpheus.Error;
import retrofit2.Call;

/**
 * ContentBlock
 */
public class ContentBlock6ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
  private XamoomContentFragment.OnXamoomContentFragmentInteractionListener mListener;
  private Context mContext;
  private TextView mTitleTextView;
  private TextView mDescriptionTextView;
  private RelativeLayout timeLayout;
  private RelativeLayout locationLayout;
  private TextView timeText;
  private TextView locationText;
  private ImageView mContentThumbnailImageView;
  private ProgressBar mProgressBar;
  private EnduserApi mEnduserApi;
  private Content mContent;
  private Integer mTextColor = null;
  private FileManager mFileManager;
  private LruCache<String, Content> mContentCache;
  private Call mCall;
  private RequestManager mGlide;
  private SharedPreferences sharedPreferences;

  public ContentBlock6ViewHolder(View itemView, Context context, EnduserApi enduserApi,
                                 LruCache<String, Content> contentCache,
                                 XamoomContentFragment.OnXamoomContentFragmentInteractionListener listener) {
    super(itemView);
    mListener = listener;
    mContext = context;
    mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
    mDescriptionTextView = (TextView) itemView.findViewById(R.id.descriptionTextView);
    timeLayout = (RelativeLayout) itemView.findViewById(R.id.content_event_time_layout);
    locationLayout = (RelativeLayout) itemView.findViewById(R.id.content_event_location_layout);
    timeText = (TextView) itemView.findViewById(R.id.content_event_time_textview);
    locationText = (TextView) itemView.findViewById(R.id.content_event_location_textview);
    mContentThumbnailImageView = (ImageView) itemView.findViewById(R.id.contentThumbnailImageView);
    mProgressBar = (ProgressBar) itemView.findViewById(R.id.contentProgressBar);
    mEnduserApi = enduserApi;
    mContentCache = contentCache;
    mFileManager = FileManager.getInstance(context);
    mGlide = Glide.with(context);
    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

    itemView.setOnClickListener(this);
  }

  public void setupContentBlock(ContentBlock contentBlock, boolean offline) {
    mEnduserApi.setOffline(offline);
    mContentThumbnailImageView.setImageDrawable(null);
    mTitleTextView.setText(null);
    mDescriptionTextView.setText(null);
    if (mCall != null) {
      mCall.cancel();
    }

    mProgressBar.setVisibility(View.VISIBLE);

    mContent = mContentCache.get(contentBlock.getContentId());
    if (mContent != null) {
      displayContent(mContent, offline);
      mProgressBar.setVisibility(View.GONE);
    } else {
      loadContent(contentBlock.getContentId(), offline);
    }
  }

  private void loadContent(final String contentId, final boolean offline) {
    if (mEnduserApi == null) {
      throw new NullPointerException("EnduserApi is null.");
    }



    String langPickerLanguage = sharedPreferences.getString("current_language_code", null);
    if(langPickerLanguage != null) mEnduserApi.setLanguage(langPickerLanguage);
    else mEnduserApi.setLanguage(mEnduserApi.getSystemLanguage());
    mCall = mEnduserApi.getContent(contentId, EnumSet.of(ContentFlags.PREVIEW),
            ContentReason.LINKED_CONTENT, null, new APIPasswordCallback<Content, List<Error>>() {
              @Override
              public void finished(Content result) {
                if (result == null) {
                  return;
                }

                Content resultContent = result;
                Spot currentRelatedSpot = result.getRelatedSpot();
                if(currentRelatedSpot != null && currentRelatedSpot.getId() != null && currentRelatedSpot.getName() == null) {
                  mEnduserApi.getSpot(currentRelatedSpot.getId(), new APICallback<Spot, List<Error>>() {
                    @Override
                    public void finished(Spot result) {
                      resultContent.setRelatedSpot(result);
                      finishContentLoading(resultContent, contentId, offline);
                    }

                    @Override
                    public void error(List<Error> error) {
                      if (error != null && error.get(0) != null) {
                        Log.e("XamoomContentBlocks", error.get(0).getCode() +
                                "\n Error Title: " + error.get(0).getTitle() +
                                "\n Detail: " + error.get(0).getDetail() +
                                "\n SpotId: " + currentRelatedSpot.getId());

                        if (error.get(0).getCode().equalsIgnoreCase("10000")) { // return when canceled
                          return;
                        }
                        mProgressBar.setVisibility(View.GONE);
                      }
                    }
                  });
                } else {
                  finishContentLoading(resultContent, contentId, offline);
                }
              }

              @Override
              public void error(List<Error> error) {
                if (error != null && error.get(0) != null) {
                  Log.e("XamoomContentBlocks", error.get(0).getCode() +
                          "\n Error Title: " + error.get(0).getTitle() +
                          "\n Detail: " + error.get(0).getDetail() +
                          "\n ContentId: " + contentId);

                  if (error.get(0).getCode().equalsIgnoreCase("10000")) { // return when canceled
                    return;
                  }
                  mProgressBar.setVisibility(View.GONE);
                }
              }

              @Override
              public void passwordRequested() {
                //DO nothing
              }
            });
  }

  private void finishContentLoading(Content result, String contentId, boolean offline) {
    mProgressBar.setVisibility(View.GONE);
    mContent = result;
    if (contentId != null) {
      mContentCache.put(contentId, result);
    }
    displayContent(result, offline);
  }


  private void displayContent(Content content, boolean offline) {
    mTitleTextView.setText(content.getTitle());
    mDescriptionTextView.setText(content.getDescription());


    Date contentDateTime = content.getFromDate();
    Spot relatedSpot = content.getRelatedSpot();

    int descriptionLinesCount = 3;

    if(contentDateTime != null) {
      String langPickerLanguage = sharedPreferences.getString("current_language_code", null);
      Locale locale;
      if(langPickerLanguage != null) {
        locale = new Locale(langPickerLanguage);
      } else {
        locale = new Locale("en");
      }

      @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM, hh:mm", locale);
      timeText.setText(sdf.format(contentDateTime));
      timeLayout.setVisibility(View.VISIBLE);
      descriptionLinesCount -=1;
    }

    if(relatedSpot != null && relatedSpot.getName() != null) {
      locationText.setText(relatedSpot.getName());
      locationLayout.setVisibility(View.VISIBLE);
      descriptionLinesCount -=1;
    }
    mDescriptionTextView.setMaxLines(descriptionLinesCount);

    if (mTextColor != null) {
      mTitleTextView.setTextColor(mTextColor);
      mDescriptionTextView.setTextColor(mTextColor);
    }

    String filePath = null;
    if (offline) {
      String imageUrl = mFileManager.getFilePath(content.getPublicImageUrl());
      if (imageUrl != null) {
        filePath = imageUrl;
      }
    } else {
      filePath = content.getPublicImageUrl();
    }

    mGlide.load(filePath)
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