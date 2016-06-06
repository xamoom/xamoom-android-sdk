package com.xamoom.android.xamoomsdk.xamoomsdk.Resource;

import android.os.Parcel;

import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.System;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "AndroidManifest.xml")
public class ContentTest {

  private Content mContent;

  @Before
  public void setup() {
    mContent = new Content();
    mContent.setId("id");
    mContent.setTitle("Title");
    mContent.setDescription("Description");
    mContent.setLanguage("en");
    mContent.setCategory(22);
    ArrayList<String> tags = new ArrayList<String>();
    tags.add("tag1");
    tags.add("tag2");
    mContent.setTags(tags);
    mContent.setPublicImageUrl("www.image.com");
    ArrayList<ContentBlock> contentBlocks = new ArrayList<>();
    ContentBlock contentBlock = new ContentBlock();
    contentBlock.setTitle("Title");
    contentBlock.setBlockType(0);
    contentBlocks.add(contentBlock);
    mContent.setContentBlocks(contentBlocks);
    System system = new System();
    system.setName("SystemName");
    mContent.setSystem(system);
  }

  @Test
  public void testConstructor() {
    Content content = new Content();

    assertNotNull(content);
  }

  @Test
  public void testParcelable() {
    Parcel parcel = Parcel.obtain();
    mContent.writeToParcel(parcel, 0);
    parcel.setDataPosition(0);
    Content createdFromParcel = Content.CREATOR.createFromParcel(parcel);

    assertEquals(mContent.getId(), createdFromParcel.getId());
    assertEquals(mContent.getTitle(), createdFromParcel.getTitle());
    assertEquals(mContent.getDescription(), createdFromParcel.getDescription());
    assertEquals(mContent.getLanguage(), createdFromParcel.getLanguage());
    assertEquals(mContent.getCategory(), createdFromParcel.getCategory());
    assertEquals(mContent.getTags(), createdFromParcel.getTags());
    assertEquals(mContent.getPublicImageUrl(), createdFromParcel.getPublicImageUrl());
    assertEquals(mContent.getContentBlocks().get(0).getTitle(), createdFromParcel.getContentBlocks().get(0).getTitle());
    assertEquals(mContent.getSystem().getName(), createdFromParcel.getSystem().getName());
  }
}
