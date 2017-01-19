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
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock3ViewHolder;
import com.xamoom.android.xamoomcontentblocks.XamoomContentFragment;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.Resource.Style;

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
   * @return Custom contentBlock viewholder.
   */
  @NonNull RecyclerView.ViewHolder onCreateViewHolder(
      ViewGroup parent, Fragment fragment, EnduserApi enduserApi, String youtubeApiKey,
      LruCache bitmapCache, LruCache contentCache, boolean showContentLinks,
      ContentBlock3ViewHolder.OnContentBlock3ViewHolderInteractionListener
          onContentBlock3ViewHolderInteractionListener,
      XamoomContentFragment.OnXamoomContentFragmentInteractionListener
          onXamoomContentFragmentInteractionListener);

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
