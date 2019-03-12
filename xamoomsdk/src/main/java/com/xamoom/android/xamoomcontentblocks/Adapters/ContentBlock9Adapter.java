/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomcontentblocks.Adapters;

import android.os.Bundle;
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
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock9ViewHolder;
import com.xamoom.android.xamoomcontentblocks.Views.CustomMapView;
import com.xamoom.android.xamoomcontentblocks.XamoomContentFragment;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Style;

import java.util.ArrayList;
import java.util.List;

public class ContentBlock9Adapter implements AdapterDelegate<List<ContentBlock>> {
  private static final int BLOCK_TYPE = 9;
  private ContentBlock9ViewHolder mapholder = null;
  private Bundle bundle = null;

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
      ContentBlock3ViewHolder.OnContentBlock3ViewHolderInteractionListener onContentBlock3ViewHolderInteractionListener, XamoomContentFragment.OnXamoomContentFragmentInteractionListener onXamoomContentFragmentInteractionListener, @Nullable ArrayList<String> urls) {
    View v = LayoutInflater.from(fragment.getContext()).inflate(R.layout.content_block_9_layout, parent, false);
    mapholder = new ContentBlock9ViewHolder(((CustomMapView) v), bundle, enduserApi, fragment, onXamoomContentFragmentInteractionListener);
    mapholder.setShowContentLinks(showContentLinks);
    return mapholder;
  }

  @Override
  public void onBindViewHolder(@NonNull List<ContentBlock> items, int position,
                               @NonNull RecyclerView.ViewHolder holder, Style style, boolean offline) {

    ContentBlock cb = items.get(position);
    ContentBlock9ViewHolder newHolder = (ContentBlock9ViewHolder) holder;
    newHolder.setIsRecyclable(false);
    newHolder.setStyle(style);
    newHolder.setupContentBlock(cb, offline);
  }

  public void onSavedInstanceState(Bundle bundle) {
    this.bundle = bundle;
    if (isMapViewActive()) {
      mapholder.getMapView().onSaveInstanceState(bundle);
    }
  }

  public void onDestroy() {
    if (isMapViewActive()) {
      mapholder.getMapView().onDestroy();
    }
  }

  public void onViewAttachedToWindow(ContentBlock9ViewHolder holder) {
    if (isMapViewActive()) {
      holder.getMapView().onStart();
      holder.getMapView().onResume();
    }
  }

  public void onViewDettachToWindow(ContentBlock9ViewHolder holder) {
    if (isMapViewActive()) {
      holder.getMapView().onPause();
      holder.getMapView().onStop();
      holder.getMapView().onDestroy();
    }
  }

  public void onLowMemory() {
    mapholder.getMapView().onLowMemory();
  }

  private boolean isMapViewActive() {
    return mapholder != null && !mapholder.getMapView().isDestroyed();
  }
}
