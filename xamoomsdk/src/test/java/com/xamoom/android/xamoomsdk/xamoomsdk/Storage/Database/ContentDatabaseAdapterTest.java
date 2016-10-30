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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyChar;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
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
        new ContentDatabaseAdapter(RuntimeEnvironment.application);
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
  public void testGetSystem() {
    Cursor mockedCursor = mock(Cursor.class);

    Mockito.stub(mMockedDatabase.query(
        Mockito.anyString(), Mockito.any(String[].class), Mockito.anyString(),
        Mockito.any(String[].class), Mockito.anyString(), Mockito.anyString(),
        Mockito.anyString())).toReturn(mockedCursor);

    Mockito.stub(mockedCursor.getCount()).toReturn(1);
    Mockito.stub(mockedCursor.moveToFirst()).toReturn(true);

    Content savedContent = mContentDatabaseAdapter.getContent("1");

    Mockito.verify(mMockedContentBlockAdapter).getRelatedContentBlocks(anyLong());

    Assert.assertNotNull(savedContent);
    Assert.assertNotNull(savedContent.getContentBlocks());
  }

  @Test
  public void insertOrUpdateContentInsertingNew() {
    ArrayList<ContentBlock> contentBlocks = new ArrayList<>();
    ContentBlock block = new ContentBlock();
    block.setId("test | 2");

    contentBlocks.add(block);

    Content content = new Content();
    content.setId("1");
    content.setContentBlocks(contentBlocks);

    mContentDatabaseAdapter.insertOrUpdateContent(content);

    Mockito.verify(mMockedDatabase)
        .insert(Mockito.eq(OfflineEnduserContract.SystemEntry.TABLE_NAME),
            Mockito.isNull(String.class),
            Mockito.any(ContentValues.class));

    Mockito.verify(mMockedContentBlockAdapter)
        .insertOrUpdate(Mockito.eq(block), anyInt());
  }

  @Test
  public void insertOrUpdateContentUpdateExisting() {
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

    mContentDatabaseAdapter.insertOrUpdateContent(content);

    Mockito.verify(mMockedDatabase)
        .update(Mockito.eq(
            OfflineEnduserContract.ContentEntry.TABLE_NAME),
            Mockito.any(ContentValues.class),
            Mockito.anyString(),
            Mockito.any(String[].class));
  }

  @Test
  public void relatedBlocksSorting() {
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
    Mockito.stub(mockedCursor.moveToFirst()).toReturn(true);
    Mockito.stub(mMockedContentBlockAdapter.getRelatedContentBlocks(anyLong()))
        .toReturn(contentBlocks);

    Content savedContent = mContentDatabaseAdapter.getContent("1");

    Assert.assertEquals(savedContent.getContentBlocks().get(0), block2);
    Assert.assertEquals(savedContent.getContentBlocks().get(1), block);
  }
}
