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
import androidx.collection.SparseArrayCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

import com.xamoom.android.xamoomcontentblocks.ListManager;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock15ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock16ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock3ViewHolder;
import com.xamoom.android.xamoomcontentblocks.XamoomContentFragment;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.Style;

import java.util.ArrayList;

public class AdapterDelegatesManager<T> {
  private static final String TAG = AdapterDelegate.class.getSimpleName();
  private static final int FALLBACK_VIEWTYPE = -2;

  private SparseArrayCompat<AdapterDelegate> adapterDelegates = new SparseArrayCompat<>();
  private AdapterDelegate fallbackAdapter;

  public AdapterDelegatesManager<T> addDelegate(int viewType, @NonNull AdapterDelegate<T> delegate) {
    adapterDelegates.put(viewType, delegate);
    return this;
  }

  public int getItemViewType(@NonNull T items, int position) {
    int delegatesCount = adapterDelegates.size();
    for (int i = 0; i < delegatesCount; i++) {
      AdapterDelegate<T> delegate = adapterDelegates.valueAt(i);
      if (delegate.isForViewType(items, position)) {
        return adapterDelegates.keyAt(i);
      }
    }

    return FALLBACK_VIEWTYPE;
  }

  public RecyclerView.ViewHolder onCreateViewHolder(
          ViewGroup parent, int viewType, Fragment fragment, EnduserApi enduserApi,String youtubeApiKey,
          LruCache bitmapCache, LruCache contentCache, boolean showContentLinks, ListManager listManager,
          ContentBlock3ViewHolder.OnContentBlock3ViewHolderInteractionListener
                  onContentBlock3ViewHolderInteractionListener,
          ContentBlock15ViewHolder.OnContentBlock15ViewHolderInteractionListener
                  onContentBlock15ViewHolderInteractionListener,
          XamoomContentFragment.OnXamoomContentFragmentInteractionListener
                  onXamoomContentFragmentInteractionListener, @Nullable ArrayList<String> url, @Nullable ArrayList<String> nonUrl, @Nullable String mapboxStyleString, @Nullable String navigationButtonTintColorString, @Nullable String contentButtonTextColorString, @Nullable String navigationMode, Content content) {

    AdapterDelegate<T> delegate = adapterDelegates.get(viewType);

    if (delegate == null && fallbackAdapter != null) {
      if (fallbackAdapter != null) {
        delegate = fallbackAdapter;
      } else {
        throw new NullPointerException("No adapter registered for viewType " + viewType);
      }
    }

    RecyclerView.ViewHolder vh = delegate.onCreateViewHolder(parent, fragment, enduserApi,
            youtubeApiKey, bitmapCache, contentCache, showContentLinks, listManager, this,
            onContentBlock3ViewHolderInteractionListener, onContentBlock15ViewHolderInteractionListener, onXamoomContentFragmentInteractionListener, url, nonUrl, mapboxStyleString, navigationButtonTintColorString, contentButtonTextColorString, navigationMode, content);

    return vh;
  }

  public void onBindViewHolder(@NonNull T items, int position,
                               @NonNull RecyclerView.ViewHolder viewHolder, Style style,
                               boolean offline) {
    AdapterDelegate<T> delegate = adapterDelegates.get(viewHolder.getItemViewType());

    if (delegate == null && fallbackAdapter != null) {
      if (fallbackAdapter != null) {
        delegate = fallbackAdapter;
      } else {
        throw new NullPointerException("No adapter registered for viewType " + viewHolder.getItemViewType());
      }
    }

    delegate.onBindViewHolder(items, position, viewHolder, style, offline);
  }

  public SparseArrayCompat<AdapterDelegate> getAdapterDelegates() {
    return adapterDelegates;
  }

  public void setAdapterDelegates(SparseArrayCompat<AdapterDelegate> adapterDelegates) {
    this.adapterDelegates = adapterDelegates;
  }

  public void setFallbackAdapter(AdapterDelegate fallbackAdapter) {
    this.fallbackAdapter = fallbackAdapter;
  }

}
