package com.xamoom.android.xamoomsdk.xamoomsdk.Storage.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Storage.Database.ContentBlockDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.Database.DatabaseHelper;
import com.xamoom.android.xamoomsdk.Storage.TableContracts.OfflineEnduserContract;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertNotNull;
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
    Mockito.stub(mMockedCursor
        .getColumnIndex(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_JSON_ID))
        .toReturn(0);
    Mockito.stub(mMockedCursor.getString(0)).toReturn("id");
    Mockito.stub(mMockedCursor
        .getColumnIndex(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_COPYRIGHT))
        .toReturn(1);
    Mockito.stub(mMockedCursor.getString(1)).toReturn("copyright");
    Mockito.stub(mMockedCursor
        .getColumnIndex(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_PUBLIC_STATUS))
        .toReturn(2);
    Mockito.stub(mMockedCursor.getInt(2)).toReturn(1);
    Mockito.stub(mMockedCursor
        .getColumnIndex(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_SHOW_CONTENT_ON_SPOTMAP))
        .toReturn(3);
    Mockito.stub(mMockedCursor.getInt(3)).toReturn(1);
    Mockito.stub(mMockedCursor
        .getColumnIndex(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_BLOCK_TYPE))
        .toReturn(4);
    Mockito.stub(mMockedCursor.getInt(4)).toReturn(1);
    Mockito.stub(mMockedCursor
        .getColumnIndex(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_TITLE))
        .toReturn(5);
    Mockito.stub(mMockedCursor.getString(5)).toReturn("title");
    Mockito.stub(mMockedCursor
        .getColumnIndex(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_TEXT))
        .toReturn(6);
    Mockito.stub(mMockedCursor.getString(6)).toReturn("text");
    Mockito.stub(mMockedCursor
        .getColumnIndex(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_ARTISTS))
        .toReturn(7);
    Mockito.stub(mMockedCursor.getString(7)).toReturn("artists");
    Mockito.stub(mMockedCursor
        .getColumnIndex(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_FILE_ID))
        .toReturn(8);
    Mockito.stub(mMockedCursor.getString(8)).toReturn("fileId");
    Mockito.stub(mMockedCursor
        .getColumnIndex(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_SOUNDCLOUD_URL))
        .toReturn(9);
    Mockito.stub(mMockedCursor.getString(9)).toReturn("soundcloud");
    Mockito.stub(mMockedCursor
        .getColumnIndex(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_LINK_TYPE))
        .toReturn(10);
    Mockito.stub(mMockedCursor.getInt(10)).toReturn(3);
    Mockito.stub(mMockedCursor
        .getColumnIndex(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_LINK_URL))
        .toReturn(11);
    Mockito.stub(mMockedCursor.getString(11)).toReturn("linkUrl");
    Mockito.stub(mMockedCursor
        .getColumnIndex(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_CONTENT_ID))
        .toReturn(12);
    Mockito.stub(mMockedCursor.getString(12)).toReturn("contentId");
    Mockito.stub(mMockedCursor
        .getColumnIndex(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_DOWNLOAD_TYPE))
        .toReturn(13);
    Mockito.stub(mMockedCursor.getInt(13)).toReturn(2);
    Mockito.stub(mMockedCursor
        .getColumnIndex(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_SPOT_MAP_TAGS))
        .toReturn(14);
    Mockito.stub(mMockedCursor.getString(14)).toReturn("tag1,tag2");
    Mockito.stub(mMockedCursor
        .getColumnIndex(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_SCALE_X))
        .toReturn(15);
    Mockito.stub(mMockedCursor.getDouble(15)).toReturn(5.2);
    Mockito.stub(mMockedCursor
        .getColumnIndex(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_VIDEO_URL))
        .toReturn(16);
    Mockito.stub(mMockedCursor.getString(16)).toReturn("videoUrl");
    Mockito.stub(mMockedCursor
        .getColumnIndex(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_ALT_TEXT))
        .toReturn(18);
    Mockito.stub(mMockedCursor.getString(18)).toReturn("alt");

    ArrayList<String> checkTags = new ArrayList<>();
    checkTags.add("tag1");
    checkTags.add("tag2");

    ContentBlock savedBlock = mContentBlockDatabaseAdapter.getContentBlock("1");

    assertNotNull(savedBlock);
    assertEquals("id", savedBlock.getId());
    assertEquals(1, savedBlock.getBlockType());
    assertTrue(savedBlock.isPublicStatus());
    assertEquals("title", savedBlock.getTitle());
    assertEquals("text", savedBlock.getText());
    assertEquals("artists", savedBlock.getArtists());
    assertEquals("fileId", savedBlock.getFileId());
    assertEquals("soundcloud", savedBlock.getSoundcloudUrl());
    assertEquals("linkUrl", savedBlock.getLinkUrl());
    assertEquals(3, savedBlock.getLinkType());
    assertEquals("contentId", savedBlock.getContentId());
    assertEquals(2, savedBlock.getDownloadType());
    assertEquals(checkTags, savedBlock.getSpotMapTags());
    assertEquals(5.2, savedBlock.getScaleX());
    assertEquals("videoUrl", savedBlock.getVideoUrl());
    assertTrue(savedBlock.isShowContentOnSpotmap());
    assertEquals("alt", savedBlock.getAltText());
    assertEquals("copyright", savedBlock.getCopyright());
  }

  @Test
  public void testInsertOrUpdateNewBlock() {
    ArgumentCaptor<ContentValues> valueCaptor = ArgumentCaptor.forClass(ContentValues.class);

    ContentBlock block;
    block = new ContentBlock();
    block.setId("1");
    block.setCopyright("copyright");

    Mockito.stub(mMockedDatabase.query(anyString(), any(String[].class), anyString(), any(String[].class), anyString(),
        anyString(), anyString())).toReturn(mMockedCursor);
    Mockito.stub(mMockedCursor.moveToFirst()).toReturn(false);

    mContentBlockDatabaseAdapter.insertOrUpdate(block, 0);

    Mockito.verify(mMockedDatabase).insert(anyString(), anyString(), valueCaptor.capture());
    assertEquals(valueCaptor.getValue()
        .get(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_JSON_ID), block.getId());
    assertEquals(valueCaptor.getValue()
        .get(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_COPYRIGHT), block.getCopyright());
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

  @Test
  public void testGetContentBlocksWithFileId() {
    String selection = OfflineEnduserContract.
        ContentBlockEntry.COLUMN_NAME_FILE_ID + " = ? OR " +
        OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_VIDEO_URL + " = ?";
    String[] selectionArg = {"url", "url"};

    Mockito.stub(mMockedDatabase.query(anyString(), any(String[].class), anyString(), any(String[].class), anyString(),
        anyString(), anyString())).toReturn(mMockedCursor);
    Mockito.stub(mMockedCursor.moveToFirst()).toReturn(true);
    Mockito.stub(mMockedCursor.getCount()).toReturn(1);

    mContentBlockDatabaseAdapter.getContentBlocksWithFile("url");

    Mockito.verify(mMockedDatabase).query(anyString(), any(String[].class), eq(selection),
        eq(selectionArg), anyString(), anyString(), anyString());
  }

  @Test
  public void testDelete() {
    Mockito.stub(mMockedDatabase.delete(anyString(), anyString(), any(String[].class))).toReturn(1);

    boolean deleted = mContentBlockDatabaseAdapter.deleteContentBlock("1");

    assertTrue(deleted);
  }

  @Test
  public void testDeleteFail() {
    Mockito.stub(mMockedDatabase.delete(anyString(), anyString(), any(String[].class))).toReturn(0);

    boolean deleted = mContentBlockDatabaseAdapter.deleteContentBlock("1");

    junit.framework.Assert.assertFalse(deleted);
  }
}
