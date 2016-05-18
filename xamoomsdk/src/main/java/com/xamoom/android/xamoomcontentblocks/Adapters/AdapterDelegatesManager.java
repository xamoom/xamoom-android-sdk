package com.xamoom.android.xamoomcontentblocks.Adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock3ViewHolder;
import com.xamoom.android.xamoomcontentblocks.XamoomContentFragment;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.Resource.Style;

import java.util.ArrayList;

public class AdapterDelegatesManager<T> {
  private static final String TAG = AdapterDelegate.class.getSimpleName();
  private static final int FALLBACK_VIEWTYPE = -2;

  private SparseArrayCompat<AdapterDelegate> adapterDelegates = new SparseArrayCompat<>();

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
    Log.d(TAG, "Tried to load viewtype " + viewType + " adapter");
    RecyclerView.ViewHolder vh = delegate.onCreateViewHolder(parent, fragment, enduserApi,
        youtubeApiKey, bitmapCache, contentCache, showContentLinks,
        onContentBlock3ViewHolderInteractionListener, onXamoomContentFragmentInteractionListener);
    return vh;

    //TODO: check if vh is null
  }

  public void onBindViewHolder(@NonNull T items, int position,
                               @NonNull RecyclerView.ViewHolder viewHolder, Style style) {
    AdapterDelegate<T> delegate = adapterDelegates.get(viewHolder.getItemViewType());
    delegate.onBindViewHolder(items, position, viewHolder, style);
  }

  public SparseArrayCompat<AdapterDelegate> getAdapterDelegates() {
    return adapterDelegates;
  }
}
