/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomsdk.xamoomcontentblocks.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.xamoom.android.xamoomcontentblocks.Adapters.ContentBlock0Adapter;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock0ViewHolder;
import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Style;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "AndroidManifest.xml")
public class ContentBlock0AdapterTest {

  Activity activity;

  @Before
  public void setup() {
    activity = Robolectric.buildActivity(Activity.class).create().get();
  }


  @Test
  public void testConstructor() {
    assertNotNull(new ContentBlock0Adapter());
  }

  @Test
  public void testIsForViewType() {
    List<ContentBlock> contentBlocks = new ArrayList();
    ContentBlock contentBlock = new ContentBlock();
    contentBlock.setBlockType(0);
    contentBlocks.add(contentBlock);

    ContentBlock0Adapter contentBlock0Adapter = new ContentBlock0Adapter();

    assertTrue(contentBlock0Adapter.isForViewType(contentBlocks, 0));
  }

  @Test
  public void testOnCreateViewHolder() {
    List<ContentBlock> contentBlocks = new ArrayList();
    ContentBlock contentBlock = new ContentBlock();
    contentBlock.setBlockType(0);
    contentBlocks.add(contentBlock);

    ContentBlock0Adapter adapter = new ContentBlock0Adapter();
    ViewGroup recycleView = (ViewGroup) View.inflate(activity, R.layout.content_block_0_layout, null);

    RecyclerView.ViewHolder vh = adapter.onCreateViewHolder(recycleView, null, null, null, null, null, false, null, null, null);

    assertNotNull(vh);
    assertEquals(vh.getClass(), ContentBlock0ViewHolder.class);
  }

  @Test
  public void testOnBindViewHolder() {
    List<ContentBlock> contentBlocks = new ArrayList();
    ContentBlock contentBlock = new ContentBlock();
    contentBlock.setBlockType(0);
    contentBlocks.add(contentBlock);
    Style style = new Style();
    style.setForegroundFontColor("#000000");
    ContentBlock0ViewHolder mockViewholder = Mockito.mock(ContentBlock0ViewHolder.class);
    ContentBlock0Adapter adapter = new ContentBlock0Adapter();

    adapter.onBindViewHolder(contentBlocks, 0, mockViewholder, style, false);

    Mockito.verify(mockViewholder).setupContentBlock(Matchers.eq(contentBlock), Matchers.eq(false));
    Mockito.verify(mockViewholder).setStyle(Matchers.eq(style));
  }
}
