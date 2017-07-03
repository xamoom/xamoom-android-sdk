/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomsdk.Storage.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Storage.TableContracts.OfflineEnduserContract;

import java.util.ArrayList;
import java.util.Arrays;

public class ContentBlockDatabaseAdapter extends DatabaseAdapter {
  private static ContentBlockDatabaseAdapter mSharedInstance;

  public static ContentBlockDatabaseAdapter getInstance(Context context) {
    if(mSharedInstance == null) {
      mSharedInstance = new ContentBlockDatabaseAdapter(context);
    }
    return mSharedInstance;
  }

  private ContentBlockDatabaseAdapter(Context context) {
    super(context);
  }

  public ContentBlock getContentBlock(String jsonId) {
    String selection = OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_JSON_ID +
        " = ?";
    String[] selectionArgs = {jsonId};

    return getContentBlock(selection, selectionArgs);
  }

  private ContentBlock getContentBlock(String selection, String[] selectionArgs) {
    open();
    Cursor cursor = queryContentBlocks(selection, selectionArgs);
    ArrayList<ContentBlock> contentBlocks = cursorToContentBlocks(cursor);
    close();

    if (contentBlocks.size() > 0) {
      return contentBlocks.get(0);
    }

    return null;
  }

  public ArrayList<ContentBlock> getRelatedContentBlocks(long row) {
    String selection = OfflineEnduserContract.
        ContentBlockEntry.COLUMN_NAME_CONTENT_RELATION + " = ?";
    String[] selectionArgs = {String.valueOf(row)};

    open();
    Cursor cursor = queryContentBlocks(selection, selectionArgs);
    ArrayList<ContentBlock> contentBlocks = cursorToContentBlocks(cursor);
    close();

    return contentBlocks;
  }

  public ArrayList<ContentBlock> getContentBlocksWithFile(String fileID) {
    String selection = OfflineEnduserContract.
        ContentBlockEntry.COLUMN_NAME_FILE_ID + " = ? OR " +
        OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_VIDEO_URL + " = ?";
    String[] selectionArgs = { fileID, fileID };

    open();
    Cursor cursor = queryContentBlocks(selection, selectionArgs);
    ArrayList<ContentBlock> contentBlocks = cursorToContentBlocks(cursor);
    close();

    return contentBlocks;
  }

  public long insertOrUpdate(ContentBlock contentBlock, long parentRow) {
    ContentValues values = new ContentValues();
    values.put(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_JSON_ID,
        contentBlock.getId());
    values.put(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_BLOCK_TYPE,
        contentBlock.getBlockType());
    values.put(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_PUBLIC_STATUS,
        contentBlock.isPublicStatus());
    values.put(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_TITLE,
        contentBlock.getTitle());
    values.put(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_TEXT,
        contentBlock.getText());
    values.put(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_ARTISTS,
        contentBlock.getArtists());
    values.put(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_FILE_ID,
        contentBlock.getFileId());
    values.put(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_SOUNDCLOUD_URL,
        contentBlock.getSoundcloudUrl());
    values.put(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_LINK_TYPE,
        contentBlock.getLinkType());
    values.put(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_LINK_URL,
        contentBlock.getLinkUrl());
    values.put(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_CONTENT_ID,
        contentBlock.getContentId());
    values.put(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_DOWNLOAD_TYPE,
        contentBlock.getDownloadType());
    if (contentBlock.getSpotMapTags() != null) {
      values.put(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_SPOT_MAP_TAGS,
          TextUtils.join(",", contentBlock.getSpotMapTags()));
    }
    values.put(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_SCALE_X,
        contentBlock.getScaleX());
    values.put(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_VIDEO_URL,
        contentBlock.getVideoUrl());
    values.put(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_SHOW_CONTENT_ON_SPOTMAP,
        contentBlock.isShowContentOnSpotmap());
    values.put(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_ALT_TEXT,
        contentBlock.getAltText());
    values.put(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_COPYRIGHT,
        contentBlock.getCopyright());
    values.put(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_CONTENT_RELATION, parentRow);

    long row = getPrimaryKey(contentBlock.getId());
    if (row != -1) {
      updateContentBlock(row, values);
    } else {
      open();
      row = mDatabase.insert(OfflineEnduserContract.ContentBlockEntry.TABLE_NAME,
          null, values);
      close();
    }

    return row;
  }

  public boolean deleteContentBlock(String jsonId) {
    String selection = OfflineEnduserContract.ContentBlockEntry
        .COLUMN_NAME_JSON_ID + " = ?";
    String[] selectionArgs = { jsonId };

    open();
    int rowsAffected = mDatabase.delete(OfflineEnduserContract.ContentBlockEntry.TABLE_NAME,
        selection, selectionArgs);
    close();

    return rowsAffected >= 1;
  }

  private void updateContentBlock(long row, ContentValues values) {
    String selection = OfflineEnduserContract.
        ContentBlockEntry._ID + " = ?";
    String[] selectionArgs = {String.valueOf(row)};

    open();
    int rows = mDatabase.update(
        OfflineEnduserContract.ContentBlockEntry.TABLE_NAME,
        values, selection, selectionArgs);
    close();
  }

  private long getPrimaryKey(String jsonId) {
    String selection = OfflineEnduserContract.
        ContentBlockEntry.COLUMN_NAME_JSON_ID + " = ?";
    String[] selectionArgs = {jsonId};

    open();
    Cursor cursor = queryContentBlocks(selection, selectionArgs);
    if (cursor != null && cursor.moveToFirst()) {
      long id = cursor.getLong(cursor.getColumnIndex(
          OfflineEnduserContract.ContentBlockEntry._ID));
      close();
      return id;
    }
    return -1;
  }

  private ArrayList<ContentBlock> cursorToContentBlocks(Cursor cursor) {
    ArrayList<ContentBlock> contentBlocks = new ArrayList<>();

    while(cursor.moveToNext()) {
      ContentBlock contentBlock = new ContentBlock();
      contentBlock.setId(cursor.getString(cursor
          .getColumnIndex(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_JSON_ID)));
      contentBlock.setBlockType(cursor.getInt(cursor
          .getColumnIndex(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_BLOCK_TYPE)));
      contentBlock.setPublicStatus((cursor.getInt(cursor
          .getColumnIndex(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_PUBLIC_STATUS)) == 1));
      contentBlock.setTitle(cursor.getString(cursor
          .getColumnIndex(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_TITLE)));
      contentBlock.setText(cursor.getString(cursor
          .getColumnIndex(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_TEXT)));
      contentBlock.setArtists(cursor.getString(cursor
          .getColumnIndex(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_ARTISTS)));
      contentBlock.setFileId(cursor.getString(cursor
          .getColumnIndex(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_FILE_ID)));
      contentBlock.setSoundcloudUrl(cursor.getString(cursor
          .getColumnIndex(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_SOUNDCLOUD_URL)));
      contentBlock.setLinkType(cursor.getInt(cursor
          .getColumnIndex(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_LINK_TYPE)));
      contentBlock.setLinkUrl(cursor.getString(cursor
          .getColumnIndex(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_LINK_URL)));
      contentBlock.setContentId(cursor.getString(cursor
          .getColumnIndex(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_CONTENT_ID)));
      contentBlock.setDownloadType(cursor.getInt(cursor
          .getColumnIndex(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_DOWNLOAD_TYPE)));
      int index = cursor
          .getColumnIndex(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_SPOT_MAP_TAGS);
      String tags = cursor.getString(index);
      if (tags != null) {
        contentBlock.setSpotMapTags(Arrays.asList(tags.split(",")));
      }
      contentBlock.setScaleX(cursor.getDouble(cursor
          .getColumnIndex(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_SCALE_X)));
      contentBlock.setVideoUrl(cursor.getString(cursor
          .getColumnIndex(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_VIDEO_URL)));
      contentBlock.setShowContentOnSpotmap((cursor.getInt(cursor
          .getColumnIndex(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_SHOW_CONTENT_ON_SPOTMAP)) == 1));
      contentBlock.setAltText(cursor.getString(cursor
          .getColumnIndex(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_ALT_TEXT)));
      contentBlock.setCopyright(cursor.getString(cursor
          .getColumnIndex(OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_COPYRIGHT)));
      contentBlocks.add(contentBlock);
    }

    return contentBlocks;
  }

  private Cursor queryContentBlocks(String selection, String[] selectionArgs) {
    Cursor cursor = mDatabase.query(
        OfflineEnduserContract.ContentBlockEntry.TABLE_NAME,
        OfflineEnduserContract.ContentBlockEntry.PROJECTION,
        selection,
        selectionArgs,
        null,
        null,
        null
    );
    return cursor;
  }
}
