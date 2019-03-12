/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomcontentblocks.Adapters;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.xamoom.android.xamoomcontentblocks.ListManager;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock2ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock3ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock9ViewHolder;
import com.xamoom.android.xamoomcontentblocks.XamoomContentFragment;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Style;

import java.util.ArrayList;
import java.util.List;

/**
 * ContentBlockAdapter will display all the contentBlocks you get from the xamoom cloud.
 *
 * @author Raphael Seher
 */
public class ContentBlockAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private XamoomContentFragment.OnXamoomContentFragmentInteractionListener mOnXamoomContentFragmentInteractionListener;
  private ContentBlock3ViewHolder.OnContentBlock3ViewHolderInteractionListener mOnContentBlock3ViewHolderInteractionListener;
  private Fragment mFragment;
  private AdapterDelegatesManager mDelegatesManager = new AdapterDelegatesManager();
  private List<ContentBlock> mContentBlocks;
  private Style mStyle;
  private String mYoutubeApiKey;
  private EnduserApi mEnduserApi;
  private ListManager mListManager;
  private boolean showContentLinks;
  private boolean offline;
  private ArrayList<String> urlScheme;
  private ContentBlock9Adapter contentBlock9Adapter;

  private String mLinkColor = "00F";
  private String mBackgroundColor = "000";
  private String mFontColor = "FFF";
  private LruCache<String, Bitmap> mBitmapCache = new LruCache<>(8*1024*1024);
  private LruCache<String, Content> mContentCache = new LruCache<>(2*1024*1024);

  /**
   * Constructor for the Adapter.
   * @param fragment Fragment with the recyclerView in it.
   * @param contentBlocks ContentBlocks to display.
   * @param showSpotMapContentLinks Toggle links from your spotmap spots to content.
   * @param youtubeApiKey Youtube api key from Google Developer Console.
   * @param contentBlock3ViewHolderInteractionListener Listener for viewHolder3 interactions.
   */
  public ContentBlockAdapter(Fragment fragment, List<ContentBlock> contentBlocks,
                             boolean showSpotMapContentLinks, String youtubeApiKey,
                             ContentBlock3ViewHolder.OnContentBlock3ViewHolderInteractionListener contentBlock3ViewHolderInteractionListener,
                             ArrayList<String> contentBlockUrlScheme) {
    mOnContentBlock3ViewHolderInteractionListener = contentBlock3ViewHolderInteractionListener;
    mFragment = fragment;
    mContentBlocks = contentBlocks;
    showContentLinks = showSpotMapContentLinks;
    mYoutubeApiKey = youtubeApiKey;
    urlScheme = contentBlockUrlScheme;

    setupAdapters();
  }

  private void setupAdapters() {

    contentBlock9Adapter = new ContentBlock9Adapter();
    mDelegatesManager.addDelegate(-1, new ContentBlockHeaderAdapter());
    mDelegatesManager.addDelegate(0, new ContentBlock0Adapter());
    mDelegatesManager.addDelegate(1, new ContentBlock1Adapter());
    mDelegatesManager.addDelegate(2, new ContentBlock2Adapter());
    mDelegatesManager.addDelegate(3, new ContentBlock3Adapter());
    mDelegatesManager.addDelegate(4, new ContentBlock4Adapter());
    mDelegatesManager.addDelegate(5, new ContentBlock5Adapter());
    mDelegatesManager.addDelegate(6, new ContentBlock6Adapter());
    mDelegatesManager.addDelegate(7, new ContentBlock7Adapter());
    mDelegatesManager.addDelegate(8, new ContentBlock8Adapter());
    mDelegatesManager.addDelegate(9, contentBlock9Adapter);
    mDelegatesManager.addDelegate(11, new ContentBlock11Adapter());
  }

  @Override
  public int getItemViewType(int position) {
    return mContentBlocks.get(position).getBlockType();
  }

  @Override
  public int getItemCount() {
    return mContentBlocks.size();
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return mDelegatesManager.onCreateViewHolder(parent, viewType, mFragment, mEnduserApi, mYoutubeApiKey,
        mBitmapCache, mContentCache, showContentLinks, mListManager,
        mOnContentBlock3ViewHolderInteractionListener, mOnXamoomContentFragmentInteractionListener, urlScheme);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    mDelegatesManager.onBindViewHolder(mContentBlocks, position, holder, mStyle, offline);
  }

  @Override
  public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
    cleanViewHolders(recyclerView);
    mFragment = null;
    mContentBlocks = null;
    mDelegatesManager = null;
    mEnduserApi = null;
    mBitmapCache = null;
    mOnContentBlock3ViewHolderInteractionListener = null;
    mOnXamoomContentFragmentInteractionListener = null;
    super.onDetachedFromRecyclerView(recyclerView);
  }

  @Override
  public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
    super.onViewAttachedToWindow(holder);

    if (holder instanceof ContentBlock9ViewHolder) {
      contentBlock9Adapter.onViewAttachedToWindow(((ContentBlock9ViewHolder) holder));
    }
  }

  @Override
  public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
    super.onViewDetachedFromWindow(holder);

    if (holder instanceof ContentBlock9ViewHolder) {
      contentBlock9Adapter.onViewDettachToWindow(((ContentBlock9ViewHolder) holder));
    }
  }

  private void cleanViewHolders(RecyclerView recyclerView) {
    if (mContentBlocks == null) { return; }

    for (int i = 0; i < mContentBlocks.size(); i++) {
      RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(i);
      if (viewHolder != null && viewHolder.getClass() == ContentBlock2ViewHolder.class) {
        ContentBlock2ViewHolder contentBlock2ViewHolder = (ContentBlock2ViewHolder) viewHolder;
        contentBlock2ViewHolder.unregisterBroadcast();
      }
    }
  }

  public void onSaveInstanceState(Bundle bundle) {
    contentBlock9Adapter.onSavedInstanceState(bundle);
  }

  public void onDestroy() {
    contentBlock9Adapter.onDestroy();
  }

  public void onLowMemory() {
    contentBlock9Adapter.onLowMemory();
  }

  public AdapterDelegatesManager getDelegatesManager() {
    return mDelegatesManager;
  }

  public Fragment getFragment() {
    return mFragment;
  }

  public EnduserApi getEnduserApi() {
    return mEnduserApi;
  }

  public List<ContentBlock> getContentBlocks() {
    return mContentBlocks;
  }

  public void setContentBlocks(List<ContentBlock> contentBlocks) {
    mContentBlocks = contentBlocks;
  }

  public void setYoutubeApiKey(String youtubeApiKey) {
    mYoutubeApiKey = youtubeApiKey;
  }

  public void setOnXamoomContentFragmentInteractionListener(XamoomContentFragment.OnXamoomContentFragmentInteractionListener onXamoomContentFragmentInteractionListener) {
    mOnXamoomContentFragmentInteractionListener = onXamoomContentFragmentInteractionListener;
  }

  public void setEnduserApi(EnduserApi enduserApi) {
    mEnduserApi = enduserApi;
    mListManager = new ListManager(mEnduserApi);
  }

  public void setStyle(Style style) {
    mStyle = style;
  }

  public void setOffline(boolean offline) {
    this.offline = offline;
  }

  public void setContentBlockUrlScheme(ArrayList<String> url) {
    urlScheme = url;
  }

  public void setShowContentLinks(boolean showContentLinks) {
    this.showContentLinks = showContentLinks;
  }
}
