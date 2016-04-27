package com.xamoom.android.xamoomsdk;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.xamoom.android.xamoomcontentblocks.XamoomContentFragment;
import com.xamoom.android.xamoomsdk.Helper.ContentFragmentActivity;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class XamoomContentFragmentTest {

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

  @Test
  public void testNewInstanceWithoutContent() {
    XamoomContentFragment fragment = XamoomContentFragment.newInstance("api");

    addFragmentToActivity(fragment);

    assertNotNull(fragment);
    assertNotNull(fragment.getRecyclerView());
    assertNull(fragment.getRecyclerView().getAdapter());
  }

  @Test
  public void testNewInstanceWithContent() {
    com.xamoom.android.xamoomsdk.Resource.Content content = new com.xamoom.android.xamoomsdk.Resource.Content();
    content.setTitle("Test Content");
    List<ContentBlock> blocks = new ArrayList<>();
    ContentBlock block1 = new ContentBlock();
    block1.setBlockType(1);
    block1.setTitle("Test Block");
    blocks.add(block1);
    content.setContentBlocks(blocks);

    XamoomContentFragment fragment = XamoomContentFragment.newInstance("api");
    fragment.setContent(content);

    addFragmentToActivity(fragment);

    assertNotNull(fragment);
    assertNotNull(fragment.getRecyclerView());
    assertNotNull(fragment.getRecyclerView().getLayoutManager());
    assertNotNull(fragment.getRecyclerView().getAdapter());
  }

  @Test
  public void testAddContentTitleAndImage() {
    com.xamoom.android.xamoomsdk.Resource.Content content = new com.xamoom.android.xamoomsdk.Resource.Content();
    content.setTitle("Test Content");
    content.setDescription("some excerpt");
    content.setPublicImageUrl("www.xamoom.com/logo.png");
    List<ContentBlock> blocks = new ArrayList<>();
    ContentBlock block = new ContentBlock();
    block.setBlockType(1);
    block.setTitle("Test Block");
    block.setText("Some text");
    blocks.add(block);
    content.setContentBlocks(blocks);

    XamoomContentFragment fragment = XamoomContentFragment.newInstance("api");
    fragment.setContent(content);

    List<ContentBlock> contentBlocks = fragment.getContentBlocks();
    assertEquals(contentBlocks.size(), 3);
    assertEquals(contentBlocks.get(0).getTitle(), "Test Content");
    assertEquals(contentBlocks.get(0).getText(), "some excerpt");
    assertEquals(contentBlocks.get(1).getFileId(), "www.xamoom.com/logo.png");
    assertEquals(contentBlocks.get(1).getScaleX(), 0.0);
    assertEquals(contentBlocks.get(2).getTitle(), "Test Block");
    assertEquals(contentBlocks.get(2).getText(), "Some text");
  }

  @Test
  public void testDoNotAddHeader() {
    com.xamoom.android.xamoomsdk.Resource.Content content = new com.xamoom.android.xamoomsdk.Resource.Content();
    content.setTitle("Test Content");
    content.setDescription("some excerpt");
    content.setPublicImageUrl("www.xamoom.com/logo.png");
    List<ContentBlock> blocks = new ArrayList<>();
    ContentBlock block = new ContentBlock();
    block.setBlockType(1);
    block.setTitle("Test Block");
    block.setText("Some text");
    blocks.add(block);
    content.setContentBlocks(blocks);

    XamoomContentFragment fragment = XamoomContentFragment.newInstance("api");
    fragment.setContent(content, false);

    List<ContentBlock> contentBlocks = fragment.getContentBlocks();
    assertEquals(contentBlocks.size(), 1);
    assertEquals(contentBlocks.get(0).getTitle(), "Test Block");
    assertEquals(contentBlocks.get(0).getText(), "Some text");
  }

  @Test
  public void testRemoveStoreLinks() {
    com.xamoom.android.xamoomsdk.Resource.Content content = new com.xamoom.android.xamoomsdk.Resource.Content();
    List<ContentBlock> blocks = new ArrayList<>();
    ContentBlock block = new ContentBlock();
    block.setBlockType(4);
    block.setLinkType(15); //other store link
    blocks.add(block);
    ContentBlock block1 = new ContentBlock();
    block1.setBlockType(4);
    block1.setLinkType(16); //android store link
    blocks.add(block1);
    ContentBlock block2 = new ContentBlock();
    block2.setBlockType(4);
    block2.setLinkType(17); //other store link
    blocks.add(block2);
    content.setContentBlocks(blocks);

    XamoomContentFragment fragment = XamoomContentFragment.newInstance("api");
    fragment.setContent(content, false);

    List<ContentBlock> contentBlocks = fragment.getContentBlocks();
    assertEquals(contentBlocks.size(), 1);
  }

  @Test
  public void testOnDestroy() {
    XamoomContentFragment fragment = XamoomContentFragment.newInstance("api");

    ContentFragmentActivity activity = Robolectric.buildActivity( ContentFragmentActivity.class )
        .create()
        .start()
        .resume()
        .get();

    FragmentManager fragmentManager = activity.getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.add(fragment, null);
    fragmentTransaction.commit();

    fragmentManager.beginTransaction().remove(fragment).commit();

    assertNull(fragment.getRecyclerView());
    assertNull(fragment.getContentBlocks());
  }

  @Test
  public void testWithAnimation() {
    com.xamoom.android.xamoomsdk.Resource.Content content = new com.xamoom.android.xamoomsdk.Resource.Content();
    content.setTitle("Test Content");
    List<ContentBlock> blocks = new ArrayList<>();
    ContentBlock block1 = new ContentBlock();
    block1.setBlockType(1);
    block1.setTitle("Test Block");
    blocks.add(block1);
    content.setContentBlocks(blocks);

    XamoomContentFragment fragment = XamoomContentFragment.newInstance("api");
    fragment.setContent(content);

    ContentFragmentActivity activity = Robolectric.buildActivity( ContentFragmentActivity.class )
        .create()
        .start()
        .resume()
        .get();

    FragmentManager fragmentManager = activity.getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.add(fragment, null);
    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
    fragmentTransaction.commit();

    fragment.animationListener.onAnimationEnd(null);

    assertNotNull(fragment.getRecyclerView());
    assertNotNull(fragment.getRecyclerView().getLayoutManager());
    assertNotNull(fragment.getRecyclerView().getAdapter());
  }

}
