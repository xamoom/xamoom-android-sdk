package com.xamoom.android.xamoomsdk.xamoomcontentblocks.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.xamoom.android.xamoomcontentblocks.Adapters.AdapterDelegate;
import com.xamoom.android.xamoomcontentblocks.Adapters.AdapterDelegatesManager;
import com.xamoom.android.xamoomcontentblocks.Adapters.ContentBlock0Adapter;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock0ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock3ViewHolder;
import com.xamoom.android.xamoomcontentblocks.XamoomContentFragment;
import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Style;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.LinkedList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyObject;
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
      public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, Fragment fragment, EnduserApi enduserApi, String youtubeApiKey, LruCache bitmapCache, LruCache contentCache, boolean showContentLinks, ContentBlock3ViewHolder.OnContentBlock3ViewHolderInteractionListener onContentBlock3ViewHolderInteractionListener, XamoomContentFragment.OnXamoomContentFragmentInteractionListener onXamoomContentFragmentInteractionListener) {
        return null;
      }


      @Override
      public void onBindViewHolder(@NonNull ContentBlock items, int position, @NonNull RecyclerView.ViewHolder holder, Style style) {

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
        any(LruCache.class), eq(false), any(ContentBlock3ViewHolder.OnContentBlock3ViewHolderInteractionListener.class),
        any(XamoomContentFragment.OnXamoomContentFragmentInteractionListener.class)))
        .thenReturn(mockViewHolder);

    RecyclerView.ViewHolder vh = manager.onCreateViewHolder(null, 0, null, null, "", null, null, false, null, null);


    assertNotNull(vh);
    assertEquals(vh, mockViewHolder);
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

    ContentBlock0ViewHolder mockViewholder = Mockito.mock(ContentBlock0ViewHolder.class);

    List<ContentBlock> items = new LinkedList<>();
    ContentBlock contentBlock = new ContentBlock();
    contentBlock.setBlockType(1);
    items.add(contentBlock);

    manager.onBindViewHolder(items, 0, mockViewHolder, null);

    verify(mockAdapter).onBindViewHolder(anyList(), eq(0), any(RecyclerView.ViewHolder.class), any(Style.class));
  }
}
