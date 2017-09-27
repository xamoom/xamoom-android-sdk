/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomsdk.xamoomsdk.Resource;

import android.os.Parcel;

import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.LinkedList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "AndroidManifest.xml")
public class ContentBlockTest {

  private ContentBlock mContentBlock;

  @Before
  public void setup() {
    mContentBlock = new ContentBlock();
    mContentBlock.setId("id");
    mContentBlock.setBlockType(0);
    mContentBlock.setPublicStatus(true);
    mContentBlock.setTitle("Title");
    mContentBlock.setText("Text");
    mContentBlock.setArtists("artists");
    mContentBlock.setFileId("www.file.id");
    mContentBlock.setSoundcloudUrl("www.souncloud.url");
    mContentBlock.setLinkType(0);
    mContentBlock.setLinkUrl("www.link.url");
    mContentBlock.setContentId("contentid");
    mContentBlock.setDownloadType(0);
    LinkedList<String> tags = new LinkedList<>();
    tags.add("tag1");
    tags.add("tag2");
    mContentBlock.setSpotMapTags(tags);
    mContentBlock.setScaleX(1.00);
    mContentBlock.setVideoUrl("www.video.url");
    mContentBlock.setShowContentOnSpotmap(true);
    mContentBlock.setAltText("alttext");
    mContentBlock.setCopyright("copyright");
    mContentBlock.setContentListTags(tags);
    mContentBlock.setContentListSortAsc(true);
    mContentBlock.setContentListPageSize(10);
  }

  @Test
  public void testConstructor() {
    ContentBlock contentBlock = new ContentBlock();

    assertNotNull(contentBlock);
  }

  @Test
  public void testParcelable() {
    Parcel parcel = Parcel.obtain();
    mContentBlock.writeToParcel(parcel, 0);
    parcel.setDataPosition(0);
    ContentBlock createdFromParcel = ContentBlock.CREATOR.createFromParcel(parcel);

    assertEquals(mContentBlock.getId(), createdFromParcel.getId());
    assertEquals(mContentBlock.getBlockType(), createdFromParcel.getBlockType());
    assertEquals(mContentBlock.isPublicStatus(), createdFromParcel.isPublicStatus());
    assertEquals(mContentBlock.getTitle(), createdFromParcel.getTitle());
    assertEquals(mContentBlock.getText(), createdFromParcel.getText());
    assertEquals(mContentBlock.getArtists(), createdFromParcel.getArtists());
    assertEquals(mContentBlock.getFileId(), createdFromParcel.getFileId());
    assertEquals(mContentBlock.getSoundcloudUrl(), createdFromParcel.getSoundcloudUrl());
    assertEquals(mContentBlock.getLinkType(), createdFromParcel.getLinkType());
    assertEquals(mContentBlock.getLinkUrl(), createdFromParcel.getLinkUrl());
    assertEquals(mContentBlock.getContentId(), createdFromParcel.getContentId());
    assertEquals(mContentBlock.getDownloadType(), createdFromParcel.getDownloadType());
    assertEquals(mContentBlock.getSpotMapTags(), createdFromParcel.getSpotMapTags());
    assertEquals(mContentBlock.getScaleX(), createdFromParcel.getScaleX());
    assertEquals(mContentBlock.getVideoUrl(), createdFromParcel.getVideoUrl());
    assertEquals(mContentBlock.isShowContentOnSpotmap(), createdFromParcel.isShowContentOnSpotmap());
    assertEquals(mContentBlock.getAltText(), createdFromParcel.getAltText());
    assertEquals(mContentBlock.getCopyright(), createdFromParcel.getCopyright());
    assertEquals(mContentBlock.getContentListPageSize(), createdFromParcel.getContentListPageSize());
    assertEquals(mContentBlock.getContentListSortAsc(), createdFromParcel.getContentListSortAsc());
    assertEquals(mContentBlock.getContentListTags(), createdFromParcel.getContentListTags());
  }
}
