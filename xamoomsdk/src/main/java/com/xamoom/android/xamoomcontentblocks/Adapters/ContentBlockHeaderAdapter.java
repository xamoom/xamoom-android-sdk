/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomcontentblocks.Adapters;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xamoom.android.xamoomcontentblocks.ListManager;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock3ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentHeaderViewHolder;
import com.xamoom.android.xamoomcontentblocks.XamoomContentFragment;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Style;

import java.util.ArrayList;
import java.util.List;

public class ContentBlockHeaderAdapter implements AdapterDelegate<List<ContentBlock>> {
  private static final int BLOCK_TYPE = -1;

  @Override
  public boolean isForViewType(@NonNull List<ContentBlock> items, int position) {
    ContentBlock cb = items.get(position);
    return cb.getBlockType() == BLOCK_TYPE;
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(
      ViewGroup parent, Fragment fragment, EnduserApi enduserApi, String youtubeApiKey,
      LruCache bitmapCache, LruCache contentCache, boolean showContentLinks, ListManager listManager, AdapterDelegatesManager adapterDelegatesManager,
      ContentBlock3ViewHolder.OnContentBlock3ViewHolderInteractionListener onContentBlock3ViewHolderInteractionListener,
      XamoomContentFragment.OnXamoomContentFragmentInteractionListener onXamoomContentFragmentInteractionListener, @Nullable ArrayList<String> urls, @Nullable String mapboxStyleString,
      @Nullable String navigationButtonTintColorString, @Nullable String contentButtonTextColorString, @Nullable String navigationMode) {
    View view = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.content_header_layout, parent, false);
    return new ContentHeaderViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull List<ContentBlock> items, int position,
                               @NonNull RecyclerView.ViewHolder holder, Style style, boolean offline) {
    ContentBlock cb = items.get(position);
    ContentHeaderViewHolder newHolder = (ContentHeaderViewHolder) holder;
    newHolder.setStyle(style);
    newHolder.setTextSize(26.0f);
    newHolder.setupContentBlock(cb, offline);
  }
}
