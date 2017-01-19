/*
* Copyright 2017 by xamoom GmbH <apps@xamoom.com>
*
* This file is part of some open source application.
*
* Some open source application is free software: you can redistribute
* it and/or modify it under the terms of the GNU General Public
* License as published by the Free Software Foundation, either
* version 2 of the License, or (at your option) any later version.
*
* Some open source application is distributed in the hope that it will
* be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
* of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with xamoom-android-sdk. If not, see <http://www.gnu.org/licenses/>.
*
* author: Raphael Seher <raphael@xamoom.com>
*/

package com.xamoom.android.xamoomsdk.xamoomsdk.Resource;

import android.os.Parcel;

import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.KeyValueObject;
import com.xamoom.android.xamoomsdk.Resource.System;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(RobolectricTestRunner.class)
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

  @Test
  public void testGetCustomMeta() {
    Content content = new Content();
    ArrayList<KeyValueObject> customMetaList = new ArrayList<>();
    customMetaList.add(new KeyValueObject("key", "value"));
    customMetaList.add(new KeyValueObject("key2", "value2"));
    content.setCustomMeta(customMetaList);

    HashMap<String, String> checkCustomMeta = new HashMap<>();
    checkCustomMeta.put("key", "value");
    checkCustomMeta.put("key2", "value2");

    HashMap<String, String> customMeta = content.getCustomMeta();

    assertEquals(customMeta, checkCustomMeta);
  }

  @Test
  public void testSetCustomMeta() {
    Content content = new Content();

    HashMap<String, String> customMeta = new HashMap<>();
    customMeta.put("key", "value");
    customMeta.put("key2", "value2");

    content.setCustomMeta(customMeta);

    assertEquals(content.getCustomMeta(), customMeta);
  }
}
