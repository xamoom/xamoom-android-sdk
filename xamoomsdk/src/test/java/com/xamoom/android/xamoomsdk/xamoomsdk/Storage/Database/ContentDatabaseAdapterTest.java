/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomsdk.xamoomsdk.Storage.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xamoom.android.xamoomcontentblocks.Adapters.ContentBlockAdapter;
import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Style;
import com.xamoom.android.xamoomsdk.Resource.System;
import com.xamoom.android.xamoomsdk.Storage.Database.ContentBlockDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.Database.ContentDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.Database.DatabaseHelper;
import com.xamoom.android.xamoomsdk.Storage.Database.SettingDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.Database.StyleDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.Database.SystemDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.TableContracts.OfflineEnduserContract;

import junit.framework.Assert;

import org.apache.maven.plugin.registry.RuntimeInfo;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.HashMap;

import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyChar;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ContentDatabaseAdapterTest {
  private ContentDatabaseAdapter mContentDatabaseAdapter;
  private ContentBlockDatabaseAdapter mMockedContentBlockAdapter;
  private DatabaseHelper mMockedDatabaseHelper;
  private SQLiteDatabase mMockedDatabase;

  @Before
  public void setup() {
    mContentDatabaseAdapter =
        ContentDatabaseAdapter.getInstance(RuntimeEnvironment.application);
    mMockedDatabaseHelper = mock(DatabaseHelper.class);
    mMockedDatabase = mock(SQLiteDatabase.class);
    mContentDatabaseAdapter.setDatabaseHelper(mMockedDatabaseHelper);

    mMockedContentBlockAdapter = mock(ContentBlockDatabaseAdapter.class);
    mContentDatabaseAdapter
        .setContentBlockDatabaseAdapter(mMockedContentBlockAdapter);

    Mockito.stub(mMockedDatabaseHelper.getWritableDatabase())
        .toReturn(mMockedDatabase);
    Mockito.stub(mMockedDatabaseHelper.getReadableDatabase())
        .toReturn(mMockedDatabase);
  }

  @Test
  public void testGetContent() {
    Cursor mockedCursor = mock(Cursor.class);

    Mockito.stub(mMockedDatabase.query(
        Mockito.anyString(), Mockito.any(String[].class), Mockito.anyString(),
        Mockito.any(String[].class), Mockito.anyString(), Mockito.anyString(),
        Mockito.anyString())).toReturn(mockedCursor);

    Mockito.stub(mockedCursor.getCount()).toReturn(1);
    Mockito.stub(mockedCursor.moveToNext()).toReturn(true).toReturn(false);
    Mockito.stub(mockedCursor.getColumnIndex(OfflineEnduserContract.ContentEntry.COLUMN_NAME_CUSTOM_META)).toReturn(1);
    Mockito.stub(mockedCursor.getString(eq(1))).toReturn("{\"key\":\"value\"}");

    HashMap<String, String> customMeta = new HashMap<>();
    customMeta.put("key", "value");

    Content savedContent = mContentDatabaseAdapter.getContent("1");

    Mockito.verify(mMockedContentBlockAdapter).getRelatedContentBlocks(anyLong());

    Assert.assertNotNull(savedContent);
    Assert.assertNotNull(savedContent.getContentBlocks());
    assertTrue(savedContent.getCustomMeta().values().containsAll(customMeta.values()));
  }

  @Test
  public void testInsertOrUpdateContentInsertingNew() {
    ArrayList<ContentBlock> contentBlocks = new ArrayList<>();
    ContentBlock block = new ContentBlock();
    block.setId("test | 2");

    contentBlocks.add(block);

    Content content = new Content();
    content.setId("1");
    content.setContentBlocks(contentBlocks);

    HashMap<String, String> customMeta = new HashMap<>();
    customMeta.put("key", "value");
    content.setCustomMeta(customMeta);

    ContentValues checkValues = new ContentValues();
    checkValues.put(OfflineEnduserContract.ContentEntry.COLUMN_NAME_JSON_ID, content.getId());
    checkValues.put(OfflineEnduserContract.ContentEntry.COLUMN_NAME_TITLE, content.getTitle());
    checkValues.put(OfflineEnduserContract.ContentEntry.COLUMN_NAME_DESCRIPTION, content.getDescription());
    checkValues.put(OfflineEnduserContract.ContentEntry.COLUMN_NAME_LANGUAGE, content.getLanguage());
    checkValues.put(OfflineEnduserContract.ContentEntry.COLUMN_NAME_CATEGORY, content.getCategory());
    checkValues.put(OfflineEnduserContract.ContentEntry.COLUMN_NAME_PUBLIC_IMAGE_URL, content.getPublicImageUrl());
    checkValues.put(OfflineEnduserContract.ContentEntry.COLUMN_NAME_CUSTOM_META,
        new JSONObject(content.getCustomMeta()).toString());

    mContentDatabaseAdapter.insertOrUpdateContent(content, false, 0);

    Mockito.verify(mMockedDatabase)
        .insert(Mockito.eq(OfflineEnduserContract.ContentEntry.TABLE_NAME),
            Mockito.isNull(String.class),
            argThat(contentValuesMatchesValues(checkValues)));

    Mockito.verify(mMockedContentBlockAdapter)
        .insertOrUpdate(Mockito.eq(block), anyInt());
  }

  Matcher<ContentValues> contentValuesMatchesValues(final ContentValues checkValues) {
    return new TypeSafeMatcher<ContentValues>() {
      public boolean matchesSafely(ContentValues values) {
        boolean matching = true;
        for (String key : checkValues.keySet()) {
          if (values.containsKey(key)) {
            String valueString = values.getAsString(key);
            String checkValueString = checkValues.getAsString(key);
            if (valueString == null && checkValueString == null) {
              continue;
            }
            if (valueString == null || checkValueString == null ||
                !valueString.equalsIgnoreCase(checkValueString)) {
              matching = false;
              break;
            }
          } else {
            matching = false;
            break;
          }
        }
        return matching;
      }
      public void describeTo(Description description) {
        description.appendText("contentValues matching");
      }
    };
  }

  @Test
  public void testInsertOrUpdateContentUpdateExisting() {
    Content content = new Content();
    content.setId("1");

    Cursor mockCursor = mock(Cursor.class);
    Mockito.stub(mMockedDatabase.query(
        Mockito.anyString(),
        Mockito.any(String[].class),
        Mockito.anyString(),
        Mockito.any(String[].class),
        Mockito.anyString(),
        Mockito.anyString(),
        Mockito.anyString()
    )).toReturn(mockCursor);
    Mockito.stub(mockCursor.moveToFirst()).toReturn(true);

    mContentDatabaseAdapter.insertOrUpdateContent(content, false, 0);

    Mockito.verify(mMockedDatabase)
        .update(Mockito.eq(
            OfflineEnduserContract.ContentEntry.TABLE_NAME),
            Mockito.any(ContentValues.class),
            Mockito.anyString(),
            Mockito.any(String[].class));
  }

  @Test
  public void testRelatedBlocksSorting() {
    ArrayList<ContentBlock> contentBlocks = new ArrayList<>();
    ContentBlock block = new ContentBlock();
    block.setId("test | 2");

    ContentBlock block2 = new ContentBlock();
    block2.setId("test | 1");

    contentBlocks.add(block);
    contentBlocks.add(block2);

    Cursor mockedCursor = mock(Cursor.class);
    Mockito.stub(mMockedDatabase.query(
        Mockito.anyString(), Mockito.any(String[].class), Mockito.anyString(),
        Mockito.any(String[].class), Mockito.anyString(), Mockito.anyString(),
        Mockito.anyString())).toReturn(mockedCursor);
    Mockito.stub(mockedCursor.getCount()).toReturn(1);
    Mockito.stub(mockedCursor.moveToNext()).toReturn(true).toReturn(false);
    Mockito.stub(mMockedContentBlockAdapter.getRelatedContentBlocks(anyLong()))
        .toReturn(contentBlocks);

    Content savedContent = mContentDatabaseAdapter.getContent("1");

    Assert.assertEquals(savedContent.getContentBlocks().get(0), block2);
    Assert.assertEquals(savedContent.getContentBlocks().get(1), block);
  }

  @Test
  public void testRelatedContents() {
    Cursor mockedCursor = mock(Cursor.class);
    Mockito.stub(mMockedDatabase.query(
        Mockito.anyString(), Mockito.any(String[].class), Mockito.anyString(),
        Mockito.any(String[].class), Mockito.anyString(), Mockito.anyString(),
        Mockito.anyString())).toReturn(mockedCursor);
    Mockito.stub(mockedCursor.getCount()).toReturn(1);
    Mockito.stub(mockedCursor.moveToNext()).toReturn(true).toReturn(false);

    ArrayList<Content> contents = mContentDatabaseAdapter.getRelatedContents(1);

    Mockito.verify(mMockedDatabase).query(anyString(), any(String[].class), anyString(),
        any(String[].class), anyString(), anyString(), anyString());
  }

  @Test
  public void testGetAllContents() {
    Cursor mockedCursor = mock(Cursor.class);
    Mockito.stub(mMockedDatabase.query(
        Mockito.anyString(), Mockito.any(String[].class), Mockito.anyString(),
        Mockito.any(String[].class), Mockito.anyString(), Mockito.anyString(),
        Mockito.anyString())).toReturn(mockedCursor);
    Mockito.stub(mockedCursor.getCount()).toReturn(1);
    Mockito.stub(mockedCursor.moveToNext()).toReturn(true).toReturn(true).toReturn(false);

    ArrayList<Content> contents = mContentDatabaseAdapter.getAllContents();

    Assert.assertEquals(2, contents.size());

    Mockito.verify(mMockedDatabase).query(anyString(), any(String[].class), anyString(),
        any(String[].class), anyString(), anyString(), anyString());
  }

  @Test
  public void testGetContentsWithName() {
    Cursor mockedCursor = mock(Cursor.class);
    Mockito.stub(mMockedDatabase.query(
        Mockito.anyString(), Mockito.any(String[].class), Mockito.anyString(),
        Mockito.any(String[].class), Mockito.anyString(), Mockito.anyString(),
        Mockito.anyString())).toReturn(mockedCursor);
    Mockito.stub(mockedCursor.getCount()).toReturn(1);
    Mockito.stub(mockedCursor.moveToNext()).toReturn(true).toReturn(true).toReturn(false);

    ArrayList<Content> contents = mContentDatabaseAdapter.getContents("testing");

    Assert.assertEquals(2, contents.size());

    Mockito.verify(mMockedDatabase).query(anyString(), any(String[].class),
        eq("LOWER(title) LIKE LOWER(?)"),
        any(String[].class), anyString(), anyString(), anyString());
  }

  @Test
  public void testGetContentsWithFileId() {
    Cursor mockedCursor = mock(Cursor.class);
    Mockito.stub(mMockedDatabase.query(
        Mockito.anyString(), Mockito.any(String[].class), Mockito.anyString(),
        Mockito.any(String[].class), Mockito.anyString(), Mockito.anyString(),
        Mockito.anyString())).toReturn(mockedCursor);
    Mockito.stub(mockedCursor.getCount()).toReturn(1);
    Mockito.stub(mockedCursor.moveToNext()).toReturn(true).toReturn(true).toReturn(false);

    ArrayList<Content> contents = mContentDatabaseAdapter.getContentsWithFileId("url");

    Assert.assertEquals(2, contents.size());

    Mockito.verify(mMockedDatabase).query(anyString(), any(String[].class),
        eq(OfflineEnduserContract.ContentEntry.COLUMN_NAME_PUBLIC_IMAGE_URL + " = ?"),
        any(String[].class), anyString(), anyString(), anyString());
  }

  @Test
  public void testDelete() {
    Mockito.stub(mMockedDatabase.delete(anyString(), anyString(), any(String[].class))).toReturn(1);

    boolean deleted = mContentDatabaseAdapter.deleteContent("1");

    assertTrue(deleted);
  }

  @Test
  public void testDeleteFail() {
    Mockito.stub(mMockedDatabase.delete(anyString(), anyString(), any(String[].class))).toReturn(0);

    boolean deleted = mContentDatabaseAdapter.deleteContent("1");

    Assert.assertFalse(deleted);
  }
}
