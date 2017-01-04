package com.xamoom.android.xamoomsdk.xamoomcontentblocks.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.xamoom.android.xamoomcontentblocks.Adapters.ContentBlockHeaderAdapter;
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
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "AndroidManifest.xml")
public class ContentBlockHeaderAdapterTest {

  Activity activity;

  @Before
  public void setup() {
    activity = Robolectric.buildActivity(Activity.class).create().get();
  }


  @Test
  public void testConstructor() {
    assertNotNull(new ContentBlockHeaderAdapter());
  }

  @Test
  public void testIsForViewType() {
    List<ContentBlock> contentBlocks = new ArrayList();
    ContentBlock contentBlock = new ContentBlock();
    contentBlock.setBlockType(-1);
    contentBlocks.add(contentBlock);

    ContentBlockHeaderAdapter contentBlockHeaderAdapter = new ContentBlockHeaderAdapter();

    assertTrue(contentBlockHeaderAdapter.isForViewType(contentBlocks, 0));
  }

  @Test
  public void testOnCreateViewHolder() {
    List<ContentBlock> contentBlocks = new ArrayList();
    ContentBlock contentBlock = new ContentBlock();
    contentBlock.setBlockType(-1);
    contentBlocks.add(contentBlock);

    ContentBlockHeaderAdapter adapter = new ContentBlockHeaderAdapter();
    ViewGroup recycleView = (ViewGroup) View.inflate(activity, R.layout.content_block_0_layout, null);

    RecyclerView.ViewHolder vh = adapter.onCreateViewHolder(recycleView, null, null, null, null, null, false, null, null);

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
    ContentBlockHeaderAdapter adapter = new ContentBlockHeaderAdapter();

    adapter.onBindViewHolder(contentBlocks, 0, mockViewholder, style, false);

    Mockito.verify(mockViewholder).setupContentBlock(Matchers.eq(contentBlock), Matchers.eq(false));
    Mockito.verify(mockViewholder).setStyle(Matchers.eq(style));
  }
}
