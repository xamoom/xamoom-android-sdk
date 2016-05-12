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
   *
   * @param items
   * @param position
   * @return
   */
  public boolean isForViewType(@NonNull T items, int position);

  /**
   *
   * @param parent
   * @return
   */
  @NonNull public RecyclerView.ViewHolder onCreateViewHolder(
      ViewGroup parent, Fragment fragment, EnduserApi enduserApi, String youtubeApiKey,
      LruCache bitmapCache, LruCache contentCache, boolean showContentLinks,
      ContentBlock3ViewHolder.OnContentBlock3ViewHolderInteractionListener
          onContentBlock3ViewHolderInteractionListener,
      XamoomContentFragment.OnXamoomContentFragmentInteractionListener
          onXamoomContentFragmentInteractionListener);

  /**
   *
   * @param items
   * @param position
   * @param holder
   */
  public void onBindViewHolder(@NonNull T items, int position,
                               @NonNull RecyclerView.ViewHolder holder, Style style);
}
