/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomsdk.xamoomcontentblocks;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.xamoom.android.xamoomcontentblocks.Adapters.ContentBlockAdapter;
import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNull;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startVisibleFragment;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "AndroidManifest.xml")
public class ContentBlockAdapterTest {

  private List<ContentBlock> contentBlockList = new ArrayList<ContentBlock>();

  @Before
  public void setup() {
    ContentBlock contentBlock = new ContentBlock();
    contentBlock.setBlockType(0);
    contentBlock.setText("Content Title");
    contentBlockList.add(contentBlock);
    ContentBlock contentBlock1 = new ContentBlock();
    contentBlock1.setBlockType(1);
    contentBlock1.setText("Content Title");
    contentBlockList.add(contentBlock1);
    ContentBlock contentBlock2 = new ContentBlock();
    contentBlock2.setBlockType(2);
    contentBlock2.setText("Content Title");
    contentBlockList.add(contentBlock2);
    ContentBlock contentBlock3 = new ContentBlock();
    contentBlock3.setBlockType(3);
    contentBlock3.setText("Content Title");
    contentBlockList.add(contentBlock3);
    ContentBlock contentBlock4 = new ContentBlock();
    contentBlock4.setBlockType(4);
    contentBlock4.setText("Content Title");
    contentBlockList.add(contentBlock4);
    ContentBlock contentBlock5 = new ContentBlock();
    contentBlock5.setBlockType(5);
    contentBlock5.setText("Content Title");
    contentBlockList.add(contentBlock5);
    ContentBlock contentBlock6 = new ContentBlock();
    contentBlock6.setBlockType(6);
    contentBlock6.setText("Content Title");
    contentBlockList.add(contentBlock6);
    ContentBlock contentBlock7 = new ContentBlock();
    contentBlock7.setBlockType(7);
    contentBlock7.setText("Content Title");
    contentBlockList.add(contentBlock7);
    ContentBlock contentBlock8 = new ContentBlock();
    contentBlock8.setBlockType(8);
    contentBlock8.setText("Content Title");
    contentBlockList.add(contentBlock8);
    ContentBlock contentBlock9 = new ContentBlock();
    contentBlock9.setBlockType(9);
    contentBlock9.setText("Content Title");
    contentBlockList.add(contentBlock9);
    ContentBlock contentBlockHeader = new ContentBlock();
    contentBlockHeader.setBlockType(-1);
    contentBlockHeader.setText("Content Title");
    contentBlockList.add(contentBlockHeader);
    ContentBlock contentBlockLists = new ContentBlock();
    contentBlockLists.setBlockType(11);
    contentBlockLists.setText("Content Title");
    contentBlockList.add(contentBlockLists);
  }

  @Test
  public void testConstructor() {
    ContentBlockAdapter adapter = new ContentBlockAdapter(new Fragment(), new ArrayList<ContentBlock>()
        , false, "apikey", null);

    assertNotNull(adapter);
  }

  @Test
  public void testGetItemViewType() {
    ContentBlockAdapter adapter = new ContentBlockAdapter(null, contentBlockList, false, null, null);

    assertEquals(0, adapter.getItemViewType(0));
    assertEquals(1, adapter.getItemViewType(1));
    assertEquals(2, adapter.getItemViewType(2));
    assertEquals(3, adapter.getItemViewType(3));
    assertEquals(4, adapter.getItemViewType(4));
    assertEquals(5, adapter.getItemViewType(5));
    assertEquals(6, adapter.getItemViewType(6));
    assertEquals(7, adapter.getItemViewType(7));
    assertEquals(8, adapter.getItemViewType(8));
    assertEquals(9, adapter.getItemViewType(9));
    assertEquals(11, adapter.getItemViewType(11));
  }

  @Test
  public void testGetItemCount() {
    ContentBlockAdapter adapter = new ContentBlockAdapter(null, contentBlockList, false, null, null);

    assertEquals(adapter.getItemCount(), 12);
  }

  @Test
  public void testOnDetachedFromRecyclerView() {
    Fragment fragment = new Fragment();

    ContentBlockAdapter adapter = new ContentBlockAdapter(fragment, contentBlockList,
        false, "apikey", null);

    adapter.onDetachedFromRecyclerView(new RecyclerView(RuntimeEnvironment.application));

    assertNull(adapter.getFragment());
    assertNull(adapter.getEnduserApi());
    assertNull(adapter.getContentBlocks());
  }


}
