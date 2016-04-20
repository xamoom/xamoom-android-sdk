package com.xamoom.android.xamoomsdk;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.test.suitebuilder.annotation.SmallTest;
import android.view.View;
import android.view.ViewGroup;

import com.xamoom.android.xamoomcontentblocks.ContentBlockAdapter;
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
import com.xamoom.android.xamoomsdk.Helper.ContentFragmentActivity;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Style;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertNull;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;
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
        ,style, new EnduserApi("apikey"), false, "apikey");

    assertNotNull(adapter);
    assertEquals(adapter.getLinkColor(), "FFF000");
    assertEquals(adapter.getFontColor(), "FFFFFF");
    assertEquals(adapter.getBackgroundColor(), "000000");
  }

  @Test
  public void testGetItemViewType() {
    ContentBlockAdapter adapter = new ContentBlockAdapter(null, contentBlockList, null, null, false, null);

    assertEquals(adapter.getItemViewType(0), 0);
    assertEquals(adapter.getItemViewType(1), 1);
  }

  @Test
  public void testGetItemCount() {
    ContentBlockAdapter adapter = new ContentBlockAdapter(null, contentBlockList, null, null, false, null);

    assertEquals(adapter.getItemCount(), 11);
  }

  @Test
  public void testOnCreateViewHolder() {
    Fragment fragment = new Fragment();
    startVisibleFragment(fragment);

    ContentBlockAdapter adapter = new ContentBlockAdapter(fragment, contentBlockList, null,
        new EnduserApi("api"), false, "apikey");

    List<RecyclerView.ViewHolder> viewHolderList = new ArrayList<>();
    for (int i = -1; i < 9; i++) {
      viewHolderList.add(adapter.onCreateViewHolder(new ViewGroup(RuntimeEnvironment.application) {
        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {

        }
      }, i));
    }

    assertEquals(viewHolderList.get(0).getClass(), ContentHeaderViewHolder.class);
    assertEquals(viewHolderList.get(1).getClass(), ContentBlock0ViewHolder.class);
    assertEquals(viewHolderList.get(2).getClass(), ContentBlock1ViewHolder.class);
    assertEquals(viewHolderList.get(3).getClass(), ContentBlock2ViewHolder.class);
    assertEquals(viewHolderList.get(4).getClass(), ContentBlock3ViewHolder.class);
    assertEquals(viewHolderList.get(5).getClass(), ContentBlock4ViewHolder.class);
    assertEquals(viewHolderList.get(6).getClass(), ContentBlock5ViewHolder.class);
    assertEquals(viewHolderList.get(7).getClass(), ContentBlock6ViewHolder.class);
    assertEquals(viewHolderList.get(8).getClass(), ContentBlock7ViewHolder.class);
    assertEquals(viewHolderList.get(9).getClass(), ContentBlock8ViewHolder.class);
  }

  @Test
  public void testOnBindViewHolderHeader() {
    Fragment fragment = new Fragment();
    startVisibleFragment(fragment);
    ContentBlockAdapter adapter = new ContentBlockAdapter(fragment, contentBlockList, null,
        new EnduserApi("api"), false, "apikey");

    ContentHeaderViewHolder viewHolder = mock(ContentHeaderViewHolder.class);

    adapter.onBindViewHolder(viewHolder, 10);

    verify(viewHolder).setupContentBlock((ContentBlock) anyObject());
  }

  @Test
  public void testOnBindViewHolder0() {
    Fragment fragment = new Fragment();
    startVisibleFragment(fragment);
    ContentBlockAdapter adapter = new ContentBlockAdapter(fragment, contentBlockList, null,
        new EnduserApi("api"), false, "apikey");

    ContentBlock0ViewHolder viewHolder = mock(ContentBlock0ViewHolder.class);

    adapter.onBindViewHolder(viewHolder, 0);

    verify(viewHolder).setupContentBlock((ContentBlock) anyObject());
  }

  @Test
  public void testOnBindViewHolder1() {
    Fragment fragment = new Fragment();
    startVisibleFragment(fragment);
    ContentBlockAdapter adapter = new ContentBlockAdapter(fragment, contentBlockList, null,
        new EnduserApi("api"), false, "apikey");

    ContentBlock1ViewHolder viewHolder = mock(ContentBlock1ViewHolder.class);

    adapter.onBindViewHolder(viewHolder, 1);

    verify(viewHolder).setupContentBlock((ContentBlock) anyObject());
  }

  @Test
  public void testOnBindViewHolder2() {
    Fragment fragment = new Fragment();
    startVisibleFragment(fragment);
    ContentBlockAdapter adapter = new ContentBlockAdapter(fragment, contentBlockList, null,
        new EnduserApi("api"), false, "apikey");

    ContentBlock2ViewHolder viewHolder = mock(ContentBlock2ViewHolder.class);

    adapter.onBindViewHolder(viewHolder, 2);

    verify(viewHolder).setupContentBlock((ContentBlock) anyObject());
  }

  @Test
  public void testOnBindViewHolder3() {
    Fragment fragment = new Fragment();
    startVisibleFragment(fragment);
    ContentBlockAdapter adapter = new ContentBlockAdapter(fragment, contentBlockList, null,
        new EnduserApi("api"), false, "apikey");

    ContentBlock3ViewHolder viewHolder = mock(ContentBlock3ViewHolder.class);

    adapter.onBindViewHolder(viewHolder, 3);

    verify(viewHolder).setupContentBlock((ContentBlock) anyObject());
  }

  @Test
  public void testOnBindViewHolder4() {
    Fragment fragment = new Fragment();
    startVisibleFragment(fragment);
    ContentBlockAdapter adapter = new ContentBlockAdapter(fragment, contentBlockList, null,
        new EnduserApi("api"), false, "apikey");

    ContentBlock4ViewHolder viewHolder = mock(ContentBlock4ViewHolder.class);

    adapter.onBindViewHolder(viewHolder, 4);

    verify(viewHolder).setupContentBlock((ContentBlock) anyObject());
  }

  @Test
  public void testOnBindViewHolder5() {
    Fragment fragment = new Fragment();
    startVisibleFragment(fragment);
    ContentBlockAdapter adapter = new ContentBlockAdapter(fragment, contentBlockList, null,
        new EnduserApi("api"), false, "apikey");

    ContentBlock5ViewHolder viewHolder = mock(ContentBlock5ViewHolder.class);

    adapter.onBindViewHolder(viewHolder, 5);

    verify(viewHolder).setupContentBlock((ContentBlock) anyObject());
  }

  @Test
  public void testOnBindViewHolder6() {
    Fragment fragment = new Fragment();
    startVisibleFragment(fragment);
    ContentBlockAdapter adapter = new ContentBlockAdapter(fragment, contentBlockList, null,
        new EnduserApi("api"), false, "apikey");

    ContentBlock6ViewHolder viewHolder = mock(ContentBlock6ViewHolder.class);

    adapter.onBindViewHolder(viewHolder, 6);

    verify(viewHolder).setupContentBlock((ContentBlock) anyObject());
  }

  @Test
  public void testOnBindViewHolder7() {
    Fragment fragment = new Fragment();
    startVisibleFragment(fragment);
    ContentBlockAdapter adapter = new ContentBlockAdapter(fragment, contentBlockList, null,
        new EnduserApi("api"), false, "apikey");

    ContentBlock7ViewHolder viewHolder = mock(ContentBlock7ViewHolder.class);

    adapter.onBindViewHolder(viewHolder, 7);

    verify(viewHolder).setupContentBlock((ContentBlock) anyObject());
  }

  @Test
  public void testOnBindViewHolder8() {
    Fragment fragment = new Fragment();
    startVisibleFragment(fragment);
    ContentBlockAdapter adapter = new ContentBlockAdapter(fragment, contentBlockList, null,
        new EnduserApi("api"), false, "apikey");

    ContentBlock8ViewHolder viewHolder = mock(ContentBlock8ViewHolder.class);

    adapter.onBindViewHolder(viewHolder, 8);

    verify(viewHolder).setupContentBlock((ContentBlock) anyObject());
  }

  @Test
  public void testOnBindViewHolder9() {
    Fragment fragment = new Fragment();
    startVisibleFragment(fragment);
    ContentBlockAdapter adapter = new ContentBlockAdapter(fragment, contentBlockList, null,
        new EnduserApi("api"), false, "apikey");

    ContentBlock9ViewHolder viewHolder = mock(ContentBlock9ViewHolder.class);

    adapter.onBindViewHolder(viewHolder, 9);

    verify(viewHolder).setupContentBlock((ContentBlock) anyObject());
  }

  @Test
  public void testOnDetachedFromRecyclerView() {
    Fragment fragment = new Fragment();

    ContentBlockAdapter adapter = new ContentBlockAdapter(fragment, contentBlockList, null,
        new EnduserApi("api"), false, "apikey");

    adapter.onDetachedFromRecyclerView(new RecyclerView(RuntimeEnvironment.application));

    assertNull(adapter.getFragment());
    assertNull(adapter.getEnduserApi());
    assertNull(adapter.getContentBlocks());
  }

}
