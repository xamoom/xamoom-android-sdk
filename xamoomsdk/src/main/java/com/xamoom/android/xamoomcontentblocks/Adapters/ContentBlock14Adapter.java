/*
 * Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the root of this project.
 */

package com.xamoom.android.xamoomcontentblocks.Adapters;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.collection.LruCache;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xamoom.android.xamoomcontentblocks.ListManager;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock14ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock15ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock16ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock3ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock9ViewHolder;
import com.xamoom.android.xamoomcontentblocks.Views.CustomMapView;
import com.xamoom.android.xamoomcontentblocks.Views.CustomMapViewWithChart;
import com.xamoom.android.xamoomcontentblocks.XamoomContentFragment;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Style;

import java.util.ArrayList;
import java.util.List;

public class ContentBlock14Adapter implements AdapterDelegate<List<ContentBlock>> {
    private static final int BLOCK_TYPE = 14;
    public ContentBlock14ViewHolder mapholder14 = null;
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
            ContentBlock3ViewHolder.OnContentBlock3ViewHolderInteractionListener onContentBlock3ViewHolderInteractionListener,
            ContentBlock15ViewHolder.OnContentBlock15ViewHolderInteractionListener onContentBlock15ViewHolderInteractionListener,
            ContentBlock16ViewHolder.OnContentBlock16ViewHolderInteractionListener onContentBlock16ViewHolderInteractionListener, XamoomContentFragment.OnXamoomContentFragmentInteractionListener onXamoomContentFragmentInteractionListener,
            @Nullable ArrayList<String> urls, @Nullable ArrayList<String> nonUrls, @Nullable String mapboxStyleString, @Nullable String navigationButtonTintColorString, @Nullable String contentButtonTextColorString, @Nullable String navigationMode, Content content) {
        View v = LayoutInflater.from(fragment.getContext()).inflate(R.layout.content_block_14_layout, parent, false);
        mapholder14 = new ContentBlock14ViewHolder(((CustomMapViewWithChart) v), bundle, enduserApi, fragment, onXamoomContentFragmentInteractionListener, mapboxStyleString, navigationButtonTintColorString, contentButtonTextColorString, navigationMode);
        return mapholder14;
    }

    @Override
    public void onBindViewHolder(@NonNull List<ContentBlock> items, int position,
                                 @NonNull RecyclerView.ViewHolder holder, Style style, boolean offline) {

        ContentBlock cb = items.get(position);
        ContentBlock14ViewHolder newHolder = (ContentBlock14ViewHolder) holder;
        newHolder.setIsRecyclable(false);
        newHolder.setStyle(style);
//        newHolder.setupContentBlock(cb, offline, cb.isShowContentOnSpotmap());
        newHolder.setupContentBlock(cb, offline, true);
    }

    public void onSavedInstanceState(Bundle bundle) {
        this.bundle = bundle;
        if (isMapViewActive()) {
            mapholder14.getMapView().onSaveInstanceState(bundle);
        }
    }

    public void onStart() {
        if (isMapViewActive()) {
            mapholder14.getMapView().onStart();
        }
    }

    public void onStop() {
        if (isMapViewActive()) {
            mapholder14.getMapView().onStop();
        }
    }

    public void onPause() {
        if (isMapViewActive()) {
            mapholder14.getMapView().onPause();
        }
    }

    public void onResume() {
        if (isMapViewActive()) {
            mapholder14.getMapView().onResume();
        }
    }

    public void onDestroy() {
        if (isMapViewActive()) {
            mapholder14.getMapView().onDestroy();
        }
    }

    public void onViewAttachedToWindow(ContentBlock14ViewHolder holder) {
        if (isMapViewActive()) {
            holder.getMapView().onStart();
            holder.getMapView().onResume();
        }
    }

    public void onViewDettachToWindow(ContentBlock14ViewHolder holder) {
        if (isMapViewActive()) {
            holder.getMapView().onPause();
            holder.getMapView().onStop();
            holder.getMapView().onDestroy();
        }
    }

    public void onLowMemory() {
        mapholder14.getMapView().onLowMemory();
    }

    private boolean isMapViewActive() {
        return mapholder14 != null  && !mapholder14.getMapView().isDestroyed();
    }
}