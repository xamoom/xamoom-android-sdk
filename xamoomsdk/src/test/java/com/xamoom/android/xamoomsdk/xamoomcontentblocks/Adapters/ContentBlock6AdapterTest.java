package com.xamoom.android.xamoomsdk.xamoomcontentblocks.Adapters;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.xamoom.android.xamoomcontentblocks.Adapters.ContentBlock6Adapter;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock6ViewHolder;
import com.xamoom.android.xamoomcontentblocks.XamoomContentFragment;
import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.Helper.ContentFragmentActivity;
import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Style;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.eq;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "AndroidManifest.xml")
public class ContentBlock6AdapterTest {
  
  Activity activity;

  private void addFragmentToActivity(Fragment fragment) {
    ContentFragmentActivity activity = Robolectric.buildActivity( ContentFragmentActivity.class )
        .create()
        .start()
        .resume()
        .get();

    FragmentManager fragmentManager = activity.getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.add(fragment, null);
    fragmentTransaction.commit();
  }

  @Before
  public void setup() {
    activity = Robolectric.buildActivity(Activity.class).create().get();
  }


  @Test
  public void testConstructor() {
    assertNotNull(new ContentBlock6Adapter());
  }

  @Test
  public void testIsForViewType() {
    List<ContentBlock> contentBlocks = new ArrayList();
    ContentBlock contentBlock = new ContentBlock();
    contentBlock.setBlockType(6);
    contentBlocks.add(contentBlock);

    ContentBlock6Adapter ContentBlock6Adapter = new ContentBlock6Adapter();

    assertTrue(ContentBlock6Adapter.isForViewType(contentBlocks, 0));
  }

  @Test
  public void testOnCreateViewHolder() {
    List<ContentBlock> contentBlocks = new ArrayList();
    ContentBlock contentBlock = new ContentBlock();
    contentBlock.setBlockType(6);
    contentBlocks.add(contentBlock);

    ContentBlock6Adapter adapter = new ContentBlock6Adapter();
    ViewGroup recycleView = (ViewGroup) View.inflate(activity, R.layout.content_block_6_layout, null);

    XamoomContentFragment fragment = XamoomContentFragment.newInstance("");
    addFragmentToActivity(fragment);

    RecyclerView.ViewHolder vh = adapter.onCreateViewHolder(recycleView, fragment, null, null, null, null, false, null, null);

    assertNotNull(vh);
    assertEquals(vh.getClass(), ContentBlock6ViewHolder.class);
  }

  @Test
  public void testOnBindViewHolder() {
    List<ContentBlock> contentBlocks = new ArrayList();
    ContentBlock contentBlock = new ContentBlock();
    contentBlock.setBlockType(6);
    contentBlocks.add(contentBlock);
    Style style = new Style();
    style.setForegroundFontColor("#000000");
    ContentBlock6ViewHolder mockViewholder = Mockito.mock(ContentBlock6ViewHolder.class);
    ContentBlock6Adapter adapter = new ContentBlock6Adapter();

    adapter.onBindViewHolder(contentBlocks, 0, mockViewholder, style, false);

    Mockito.verify(mockViewholder).setupContentBlock(eq(contentBlock), eq(false));
  }
}
