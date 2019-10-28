/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomsdk.xamoomcontentblocks.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.collection.LruCache;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

import com.xamoom.android.xamoomcontentblocks.Adapters.AdapterDelegate;
import com.xamoom.android.xamoomcontentblocks.Adapters.AdapterDelegatesManager;
import com.xamoom.android.xamoomcontentblocks.Adapters.ContentBlock0Adapter;
import com.xamoom.android.xamoomcontentblocks.ListManager;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock0ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock3ViewHolder;
import com.xamoom.android.xamoomcontentblocks.XamoomContentFragment;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Style;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.LinkedList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AdapterDelegatesManagerTest {

  @Test
  public void testConstructor() {
    AdapterDelegatesManager manager = new AdapterDelegatesManager();

    assertNotNull(manager);
    assertNotNull(manager.getAdapterDelegates());
  }

  @Test
  public void testAddDelegate() {
    AdapterDelegatesManager<ContentBlock> manager= new AdapterDelegatesManager();

    AdapterDelegatesManager<ContentBlock> manager2 = manager.addDelegate(0, new AdapterDelegate<ContentBlock>() {
      @Override
      public boolean isForViewType(@NonNull ContentBlock items, int position) {
        return false;
      }

      @NonNull
      @Override
      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, Fragment fragment, EnduserApi enduserApi,
                                                        String youtubeApiKey, LruCache bitmapCache, LruCache contentCache,
                                                        boolean showContentLinks, ListManager listManager, AdapterDelegatesManager adapterDelegatesManager,
                                                        ContentBlock3ViewHolder.OnContentBlock3ViewHolderInteractionListener onContentBlock3ViewHolderInteractionListener,
                                                        XamoomContentFragment.OnXamoomContentFragmentInteractionListener onXamoomContentFragmentInteractionListener) {
        return null;
      }

      @Override
      public void onBindViewHolder(@NonNull ContentBlock items, int position,
                                   @NonNull RecyclerView.ViewHolder holder,
                                   Style style, boolean offline) {

      }
    });

    assertEquals(manager, manager2);
    assertEquals(manager.getAdapterDelegates().size(), 1);
  }

  @Test
  public void testOnCreateViewHolder() {
    AdapterDelegatesManager<List<ContentBlock>> manager= new AdapterDelegatesManager();
    ContentBlock0Adapter mockAdapter = Mockito.mock(ContentBlock0Adapter.class);
    ContentBlock0ViewHolder mockViewHolder = Mockito.mock(ContentBlock0ViewHolder.class);
    manager.addDelegate(0, mockAdapter);

    when(mockAdapter.onCreateViewHolder(any(ViewGroup.class), any(Fragment.class),
        any(EnduserApi.class), anyString(), any(LruCache.class),
        any(LruCache.class), eq(false), any(ListManager.class), any(AdapterDelegatesManager.class), any(ContentBlock3ViewHolder.OnContentBlock3ViewHolderInteractionListener.class),
        any(XamoomContentFragment.OnXamoomContentFragmentInteractionListener.class)))
        .thenReturn(mockViewHolder);

    RecyclerView.ViewHolder vh = manager.onCreateViewHolder(null, 0, null, null, "", null, null, false, null, null, null);


    assertNotNull(vh);
    assertEquals(vh, mockViewHolder);
  }

  @Test
  public void testOnCreateViewHolderFallback() {
    AdapterDelegatesManager<List<ContentBlock>> manager= new AdapterDelegatesManager();
    ContentBlock0Adapter mockAdapter = Mockito.mock(ContentBlock0Adapter.class);
    ContentBlock0ViewHolder mockViewHolder = Mockito.mock(ContentBlock0ViewHolder.class);
    manager.setFallbackAdapter(mockAdapter);

    when(mockAdapter.onCreateViewHolder(any(ViewGroup.class), any(Fragment.class),
        any(EnduserApi.class), anyString(), any(LruCache.class),
        any(LruCache.class), eq(false), any(ListManager.class), any(AdapterDelegatesManager.class), any(ContentBlock3ViewHolder.OnContentBlock3ViewHolderInteractionListener.class),
        any(XamoomContentFragment.OnXamoomContentFragmentInteractionListener.class)))
        .thenReturn(mockViewHolder);

    RecyclerView.ViewHolder vh = manager.onCreateViewHolder(null, 0, null, null, "", null, null, false, null, null, null);

    assertNotNull(vh);
    assertEquals(vh, mockViewHolder);
  }

  @Test(expected = NullPointerException.class)
  public void testOnCreateException() {
    AdapterDelegatesManager<List<ContentBlock>> manager= new AdapterDelegatesManager();

    RecyclerView.ViewHolder vh = manager.onCreateViewHolder(null, 0, null, null, "", null, null, false, null, null, null);
  }

  @Test
  public void testGetItemViewType() {
    AdapterDelegatesManager<List<ContentBlock>> manager= new AdapterDelegatesManager();
    manager.addDelegate(0, new ContentBlock0Adapter());

    List<ContentBlock> items = new LinkedList<>();
    ContentBlock contentBlock = new ContentBlock();
    contentBlock.setBlockType(0);
    items.add(contentBlock);

    int viewType = manager.getItemViewType(items, 0);

    assertEquals(viewType, 0);
  }

  @Test
  public void testGetItemViewTypeFallback() {
    AdapterDelegatesManager<List<ContentBlock>> manager= new AdapterDelegatesManager();
    manager.addDelegate(0, new ContentBlock0Adapter());

    List<ContentBlock> items = new LinkedList<>();
    ContentBlock contentBlock = new ContentBlock();
    contentBlock.setBlockType(1);
    items.add(contentBlock);

    int viewType = manager.getItemViewType(items, 0);

    assertEquals(viewType, -2);
  }

  @Test
  public void testOnBindViewHolder() {
    AdapterDelegatesManager<List<ContentBlock>> manager= new AdapterDelegatesManager();
    ContentBlock0Adapter mockAdapter = Mockito.mock(ContentBlock0Adapter.class);
    ContentBlock0ViewHolder mockViewHolder = Mockito.mock(ContentBlock0ViewHolder.class);
    manager.addDelegate(0, mockAdapter);

    List<ContentBlock> items = new LinkedList<>();
    ContentBlock contentBlock = new ContentBlock();
    contentBlock.setBlockType(0);
    items.add(contentBlock);

    manager.onBindViewHolder(items, 0, mockViewHolder, null, false);

    verify(mockAdapter).onBindViewHolder(anyList(), eq(0), any(RecyclerView.ViewHolder.class),
        any(Style.class), anyBoolean());
  }

  @Test(expected = NullPointerException.class)
  public void testOnBindViewHolderException() {
    AdapterDelegatesManager<List<ContentBlock>> manager= new AdapterDelegatesManager();
    ContentBlock0ViewHolder mockViewHolder = Mockito.mock(ContentBlock0ViewHolder.class);

    List<ContentBlock> items = new LinkedList<>();
    ContentBlock contentBlock = new ContentBlock();
    contentBlock.setBlockType(1);
    items.add(contentBlock);

    manager.onBindViewHolder(items, 0, mockViewHolder, null, false);
  }

  @Test
  public void testOnBindViewHolderFallback() {
    AdapterDelegatesManager<List<ContentBlock>> manager= new AdapterDelegatesManager();
    ContentBlock0Adapter mockAdapter = Mockito.mock(ContentBlock0Adapter.class);
    manager.setFallbackAdapter(mockAdapter);
    ContentBlock0ViewHolder mockViewHolder = Mockito.mock(ContentBlock0ViewHolder.class);

    List<ContentBlock> items = new LinkedList<>();
    ContentBlock contentBlock = new ContentBlock();
    contentBlock.setBlockType(1);
    items.add(contentBlock);

    manager.onBindViewHolder(items, 0, mockViewHolder, null, false);


    verify(mockAdapter).onBindViewHolder(anyList(), eq(0), any(RecyclerView.ViewHolder.class),
        any(Style.class), anyBoolean());
  }
}
