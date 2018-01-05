package com.xamoom.android.xamoomsdk.xamoomcontentblocks;


import com.xamoom.android.xamoomcontentblocks.ContentFragmentCache;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.class)
public class ContentFragmentCacheTest {

  @Test
  public void testSaveAndRestoring() {
    ContentBlock contentBlock = new ContentBlock();
    ArrayList<ContentBlock> contentBlocks = new ArrayList<>();
    contentBlocks.add(contentBlock);

    ContentFragmentCache.getSharedInstance().save("test", contentBlocks);
    ContentFragmentCache.getSharedInstance().save("test", contentBlocks);

    // test get first
    ArrayList<ContentBlock> savedContentBlocks =
        (ArrayList<ContentBlock>) ContentFragmentCache.getSharedInstance().get("test");
    Assert.assertEquals(contentBlocks, savedContentBlocks);

    // test get second -> should work because it saved it two times
    savedContentBlocks =
        (ArrayList<ContentBlock>) ContentFragmentCache.getSharedInstance().get("test");
    Assert.assertEquals(contentBlocks, savedContentBlocks);

    // test if second one is null
    ArrayList<ContentBlock> nullContentBlocks =
        (ArrayList<ContentBlock>) ContentFragmentCache.getSharedInstance().get("test");
    Assert.assertEquals(null, nullContentBlocks);
  }
}
