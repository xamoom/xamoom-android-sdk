package com.xamoom.android.xamoomsdk.xamoomcontentblocks;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.xamoom.android.xamoomcontentblocks.Adapters.ContentBlockAdapter;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock0ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock1ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock2ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock3ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock4ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock5ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock6ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock7ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock8ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock9ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentHeaderViewHolder;
import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Style;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNull;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startVisibleFragment;

@RunWith(RobolectricGradleTestRunner.class)
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
  }

  @Test
  public void testConstructor() {
    Style style = new Style();
    style.setBackgroundColor("#000000");
    style.setForegroundFontColor("#FFFFFF");
    style.setHighlightFontColor("#FFF000");


    ContentBlockAdapter adapter = new ContentBlockAdapter(new Fragment(), new ArrayList<ContentBlock>()
        ,style, new EnduserApi("apikey"), false, "apikey", null, null);

    assertNotNull(adapter);
    assertEquals(adapter.getLinkColor(), "FFF000");
    assertEquals(adapter.getFontColor(), "FFFFFF");
    assertEquals(adapter.getBackgroundColor(), "000000");
  }

  @Test
  public void testGetItemViewType() {
    ContentBlockAdapter adapter = new ContentBlockAdapter(null, contentBlockList, null, null, false, null, null, null);

    assertEquals(adapter.getItemViewType(0), 0);
    assertEquals(adapter.getItemViewType(1), 1);
  }

  @Test
  public void testGetItemCount() {
    ContentBlockAdapter adapter = new ContentBlockAdapter(null, contentBlockList, null, null, false, null, null, null);

    assertEquals(adapter.getItemCount(), 11);
  }

  @Test
  public void testOnDetachedFromRecyclerView() {
    Fragment fragment = new Fragment();

    ContentBlockAdapter adapter = new ContentBlockAdapter(fragment, contentBlockList, null,
        new EnduserApi("api"), false, "apikey", null, null);

    adapter.onDetachedFromRecyclerView(new RecyclerView(RuntimeEnvironment.application));

    assertNull(adapter.getFragment());
    assertNull(adapter.getEnduserApi());
    assertNull(adapter.getContentBlocks());
  }

}
