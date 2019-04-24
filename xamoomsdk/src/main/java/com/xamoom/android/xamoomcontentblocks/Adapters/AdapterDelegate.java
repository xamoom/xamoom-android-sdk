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
import android.view.ViewGroup;

import com.xamoom.android.xamoomcontentblocks.ListManager;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock3ViewHolder;
import com.xamoom.android.xamoomcontentblocks.XamoomContentFragment;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.Resource.Style;

import java.util.ArrayList;

public interface AdapterDelegate<T> {
  /**
   * Returns if the item on position is the right viewType for the adapter.
   *
   * @param items List of contentblock items.
   * @param position Position for recyclerview.
   * @return true if adapter is for viewtype
   */
  boolean isForViewType(@NonNull T items, int position);

  /**
   * Return new instance of custom viewholder.
   *
   * @param parent Parent view.
   * @param fragment Current XamoomContentFragment instance.
   * @param enduserApi Instance of {@link EnduserApi}.
   * @param youtubeApiKey Youtube Api key.
   * @param bitmapCache LruCache for bitmaps.
   * @param contentCache LruCache for contents.
   * @param showContentLinks Toggle links from your spotmap spots to content.
   * @param onContentBlock3ViewHolderInteractionListener Listener for viewHolder3 interactions.
   * @param onXamoomContentFragmentInteractionListener Listener for XamoomContent interactions.
   * @param urls ArrayList of urls, which should be opened in WebView.
   * @param mapboxStyleString The custom mapbox style. Default is street.
   * @param navigationButtonTintColorString The ContentBlock9ViewHolder FAB tinti color as String.
   * @param contentButtonTextColorString The ContentBlock9ViewHolder content button text color.
   * @return Custom contentBlock viewholder.
   */
  @NonNull RecyclerView.ViewHolder onCreateViewHolder(
          ViewGroup parent, Fragment fragment, EnduserApi enduserApi, String youtubeApiKey,
          LruCache bitmapCache, LruCache contentCache, boolean showContentLinks, ListManager listManager, AdapterDelegatesManager adapterDelegatesManager,
          ContentBlock3ViewHolder.OnContentBlock3ViewHolderInteractionListener
          onContentBlock3ViewHolderInteractionListener,
          XamoomContentFragment.OnXamoomContentFragmentInteractionListener
          onXamoomContentFragmentInteractionListener, ArrayList<String> urls, String mapboxStyleString,
          @Nullable String navigationButtonTintColorString, @Nullable String contentButtonTextColorString);

  /**
   * Called before recyclerview shows viewholder.
   *
   * @param items List of contentblock items.
   * @param position Position for recyclerview.
   * @param holder Custom contentBlock viewholder.
   * @param style Style from xamoom to style content.
   * @param offline Boolean to tell if offline.
   */
  void onBindViewHolder(@NonNull T items, int position,
                        @NonNull RecyclerView.ViewHolder holder, Style style, boolean offline);
}
