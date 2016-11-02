package com.xamoom.android.xamoomsdk.xamoomsdk.Storage.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Storage.Database.ContentBlockDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.Database.DatabaseHelper;
import com.xamoom.android.xamoomsdk.Storage.Database.SettingDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.TableContracts.OfflineEnduserContract;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ContentBlockDatabaseAdapterTest {
  private ContentBlockDatabaseAdapter mContentBlockDatabaseAdapter;
  private DatabaseHelper mMockedDatabaseHelper;
  private SQLiteDatabase mMockedDatabase;
  private Cursor mMockedCursor;

  @Before
  public void setup() {
    mContentBlockDatabaseAdapter = ContentBlockDatabaseAdapter.getInstance(RuntimeEnvironment.application);
    mMockedDatabaseHelper = mock(DatabaseHelper.class);
    mMockedDatabase = mock(SQLiteDatabase.class);
    mMockedCursor = mock(Cursor.class);

    mContentBlockDatabaseAdapter.setDatabaseHelper(mMockedDatabaseHelper);

    Mockito.stub(mMockedDatabaseHelper.getWritableDatabase())
        .toReturn(mMockedDatabase);
    Mockito.stub(mMockedDatabaseHelper.getReadableDatabase())
        .toReturn(mMockedDatabase);
  }

  @Test
  public void testGetContentBlock() {
    Mockito.stub(mMockedDatabase.query(anyString(), any(String[].class), anyString(),
        any(String[].class), anyString(), anyString(), anyString())).toReturn(mMockedCursor);
    Mockito.stub(mMockedCursor.getCount()).toReturn(1);
    Mockito.stub(mMockedCursor.moveToNext()).toReturn(true).toReturn(false);

    Mockito.stub(mMockedCursor.getInt(anyInt())).toReturn(1);

    ContentBlock savedBlock = mContentBlockDatabaseAdapter.getContentBlock("1");

    Assert.assertNotNull(savedBlock);
    Assert.assertTrue(savedBlock.isPublicStatus());
    Assert.assertTrue(savedBlock.isShowContentOnSpotmap());
  }

  @Test
  public void testInsertOrUpdateNewBlock() {
    ContentBlock block = new ContentBlock();
    block.setId("1");

    Mockito.stub(mMockedDatabase.query(anyString(), any(String[].class), anyString(), any(String[].class), anyString(),
        anyString(), anyString())).toReturn(mMockedCursor);
    Mockito.stub(mMockedCursor.moveToFirst()).toReturn(false);

    mContentBlockDatabaseAdapter.insertOrUpdate(block, 0);

    Mockito.verify(mMockedDatabase).insert(anyString(), anyString(), any(ContentValues.class));
  }

  @Test
  public void testInsertOrUpdateExistingBlock() {
    ContentBlock block = new ContentBlock();
    block.setId("1");

    Mockito.stub(mMockedDatabase.query(anyString(), any(String[].class), anyString(), any(String[].class), anyString(),
        anyString(), anyString())).toReturn(mMockedCursor);
    Mockito.stub(mMockedCursor.moveToFirst()).toReturn(true);
    Mockito.stub(mMockedCursor.getCount()).toReturn(1);

    mContentBlockDatabaseAdapter.insertOrUpdate(block, 0);

    Mockito.verify(mMockedDatabase).update(anyString(), any(ContentValues.class), anyString(),
        any(String[].class));
  }

  @Test
  public void testGetRelatedContentBlocks() {
    String selection = OfflineEnduserContract.
        ContentBlockEntry.COLUMN_NAME_CONTENT_RELATION + " = ?";
    String[] selectionArg = {"1"};

    Mockito.stub(mMockedDatabase.query(anyString(), any(String[].class), anyString(), any(String[].class), anyString(),
        anyString(), anyString())).toReturn(mMockedCursor);
    Mockito.stub(mMockedCursor.moveToFirst()).toReturn(true);
    Mockito.stub(mMockedCursor.getCount()).toReturn(1);

    mContentBlockDatabaseAdapter.getRelatedContentBlocks(1);

    Mockito.verify(mMockedDatabase).query(anyString(), any(String[].class), eq(selection),
        eq(selectionArg), anyString(), anyString(), anyString());
  }
}
