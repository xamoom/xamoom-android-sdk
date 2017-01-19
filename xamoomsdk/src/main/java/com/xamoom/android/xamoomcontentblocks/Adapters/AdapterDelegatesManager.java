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

package com.xamoom.android.xamoomcontentblocks.Adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock3ViewHolder;
import com.xamoom.android.xamoomcontentblocks.XamoomContentFragment;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.Resource.Style;

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
      LruCache bitmapCache, LruCache contentCache, boolean showContentLinks,
      ContentBlock3ViewHolder.OnContentBlock3ViewHolderInteractionListener
          onContentBlock3ViewHolderInteractionListener,
      XamoomContentFragment.OnXamoomContentFragmentInteractionListener
          onXamoomContentFragmentInteractionListener) {

    AdapterDelegate<T> delegate = adapterDelegates.get(viewType);

    if (delegate == null && fallbackAdapter != null) {
      if (fallbackAdapter != null) {
        delegate = fallbackAdapter;
      } else {
        throw new NullPointerException("No adapter registered for viewType " + viewType);
      }
    }

    RecyclerView.ViewHolder vh = delegate.onCreateViewHolder(parent, fragment, enduserApi,
        youtubeApiKey, bitmapCache, contentCache, showContentLinks,
        onContentBlock3ViewHolderInteractionListener, onXamoomContentFragmentInteractionListener);

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

  public void setFallbackAdapter(AdapterDelegate fallbackAdapter) {
    this.fallbackAdapter = fallbackAdapter;
  }

}
