package com.xamoom.android.xamoomsdk.xamoomcontentblocks.Adapters;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.xamoom.android.xamoomcontentblocks.Adapters.ContentBlock8Adapter;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock8ViewHolder;
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

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "AndroidManifest.xml")
public class ContentBlock8AdapterTest {

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
    assertNotNull(new ContentBlock8Adapter());
  }

  @Test
  public void testIsForViewType() {
    List<ContentBlock> contentBlocks = new ArrayList();
    ContentBlock contentBlock = new ContentBlock();
    contentBlock.setBlockType(8);
    contentBlocks.add(contentBlock);

    ContentBlock8Adapter ContentBlock8Adapter = new ContentBlock8Adapter();

    assertTrue(ContentBlock8Adapter.isForViewType(contentBlocks, 0));
  }

  @Test
  public void testOnCreateViewHolder() {
    List<ContentBlock> contentBlocks = new ArrayList();
    ContentBlock contentBlock = new ContentBlock();
    contentBlock.setBlockType(8);
    contentBlocks.add(contentBlock);

    ContentBlock8Adapter adapter = new ContentBlock8Adapter();
    ViewGroup recycleView = (ViewGroup) View.inflate(activity, R.layout.content_block_8_layout, null);

    XamoomContentFragment fragment = XamoomContentFragment.newInstance("");
    addFragmentToActivity(fragment);

    RecyclerView.ViewHolder vh = adapter.onCreateViewHolder(recycleView, fragment, null, null, null, null, false, null, null);

    assertNotNull(vh);
    assertEquals(vh.getClass(), ContentBlock8ViewHolder.class);
  }

  @Test
  public void testOnBindViewHolder() {
    List<ContentBlock> contentBlocks = new ArrayList();
    ContentBlock contentBlock = new ContentBlock();
    contentBlock.setBlockType(8);
    contentBlocks.add(contentBlock);
    Style style = new Style();
    style.setForegroundFontColor("#000000");
    ContentBlock8ViewHolder mockViewholder = Mockito.mock(ContentBlock8ViewHolder.class);
    ContentBlock8Adapter adapter = new ContentBlock8Adapter();

    adapter.onBindViewHolder(contentBlocks, 0, mockViewholder, style);

    Mockito.verify(mockViewholder).setupContentBlock(Matchers.eq(contentBlock));
  }
}
