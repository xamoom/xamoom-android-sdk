/*
 * Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the root of this project.
 */

package com.xamoom.android.xamoomcontentblocks.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.collection.LruCache;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xamoom.android.xamoomcontentblocks.ListManager;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock0ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock15ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock16ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock3ViewHolder;
import com.xamoom.android.xamoomcontentblocks.XamoomContentFragment;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Style;

import java.util.ArrayList;
import java.util.List;

public class ContentBlock0Adapter implements AdapterDelegate<List<ContentBlock>> {
  private static final int BLOCK_TYPE = 0;

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
          ContentBlock15ViewHolder.OnContentBlock15ViewHolderInteractionListener
                  onContentBlock15ViewHolderInteractionListener,
          ContentBlock16ViewHolder.OnContentBlock16ViewHolderInteractionListener
                  onContentBlock16ViewHolderInteractionListener,
          XamoomContentFragment.OnXamoomContentFragmentInteractionListener onXamoomContentFragmentInteractionListener, @Nullable ArrayList<String> urls, @Nullable ArrayList<String> nonUrls,
          @Nullable String mapboxStyleString, @Nullable String navigationButtonTintColorString, @Nullable String contentButtonTextColorString, @Nullable String navigationMode, Content content) {
    View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.content_block_0_layout, parent, false);
    return new ContentBlock0ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull List<ContentBlock> items, int position,
                               @NonNull RecyclerView.ViewHolder holder, Style style,
                               boolean offline) {
    ContentBlock cb = items.get(position);
    ContentBlock0ViewHolder newHolder = (ContentBlock0ViewHolder) holder;
    newHolder.setStyle(style);
    newHolder.setupContentBlock(cb, offline, 0);
  }

}
