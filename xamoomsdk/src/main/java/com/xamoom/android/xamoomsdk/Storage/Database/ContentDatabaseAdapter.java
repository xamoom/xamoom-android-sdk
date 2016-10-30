package com.xamoom.android.xamoomsdk.Storage.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.System;
import com.xamoom.android.xamoomsdk.Storage.TableContracts.OfflineEnduserContract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class ContentDatabaseAdapter extends DatabaseAdapter {

  private ContentBlockDatabaseAdapter mContentBlockDatabaseAdapter;
  private SystemDatabaseAdapter mSystemDatabaseAdapter;

  public ContentDatabaseAdapter(Context context) {
    super(context);
    mContentBlockDatabaseAdapter = new ContentBlockDatabaseAdapter(context);
    mSystemDatabaseAdapter = new SystemDatabaseAdapter(context);
  }

  public Content getContent(String jsonId) {
    open();
    Cursor cursor = queryContent(jsonId);

    if (cursor.getCount() > 1) {
      // TODO: too many exception
    }

    Content content = cursorToContent(cursor);

    close();
    return content;
  }

  public long insertOrUpdateContent(Content content) {
    ContentValues values = new ContentValues();
    values.put(OfflineEnduserContract.ContentEntry.COLUMN_NAME_JSON_ID, content.getId());
    values.put(OfflineEnduserContract.ContentEntry.COLUMN_NAME_TITLE, content.getTitle());
    values.put(OfflineEnduserContract.ContentEntry.COLUMN_NAME_DESCRIPTION, content.getDescription());
    values.put(OfflineEnduserContract.ContentEntry.COLUMN_NAME_LANGUAGE, content.getLanguage());
    values.put(OfflineEnduserContract.ContentEntry.COLUMN_NAME_CATEGORY, content.getCategory());
    if (content.getTags() != null) {
      values.put(OfflineEnduserContract.ContentEntry.COLUMN_NAME_TAGS,
          TextUtils.join(",", content.getTags()));
    }

    long row = getPrimaryKey(content.getId());
    if (row != -1) {
      updateContent(row, values);
    } else {
      open();
      row = mDatabase.insert(OfflineEnduserContract.ContentEntry.TABLE_NAME,
          null, values);
      close();
    }

    // inserts contentBlocks to database with foreign key to content
    if (content.getContentBlocks() != null) {
      for (ContentBlock cb : content.getContentBlocks()) {
        mContentBlockDatabaseAdapter.insertOrUpdate(cb, row);
      }
    }

    if (content.getSystem() != null) {
      long styleRow = mSystemDatabaseAdapter.insertOrUpdateSystem(content.getSystem());
      if (styleRow != -1) {
        values.put(OfflineEnduserContract.ContentEntry.COLUMN_NAME_SYSTEM_RELATION, content.getSystem().getId());
        updateContent(row, values);
      }
    }

    return row;
  }

  private int updateContent(long row, ContentValues values) {
    String selection = OfflineEnduserContract.ContentEntry._ID + " = ?";
    String[] selectionArgs = { String.valueOf(row) };

    open();
    int rowsUpdated = mDatabase.update(
        OfflineEnduserContract.ContentEntry.TABLE_NAME,
        values,
        selection,
        selectionArgs);
    close();

    return rowsUpdated;
  }

  private long getPrimaryKey(String jsonId) {
    open();
    Cursor cursor = queryContent(jsonId);
    if (cursor != null) {
      if (cursor.moveToFirst()) {
        long id = cursor.getInt(cursor.getColumnIndex(OfflineEnduserContract.ContentEntry._ID));
        close();
        return id;
      }
    }
    close();
    return -1;
  }

  private Cursor queryContent(String jsonId) {
    String selection = OfflineEnduserContract.ContentEntry.COLUMN_NAME_JSON_ID + " = ?";
    String[] selectionArgs = { jsonId };

    Cursor cursor = mDatabase.query(
        OfflineEnduserContract.ContentEntry.TABLE_NAME,
        OfflineEnduserContract.ContentEntry.PROJECTION,
        selection,
        selectionArgs,
        null,
        null,
        null
    );

    return cursor;
  }

  private Content cursorToContent(Cursor cursor) {
    if (cursor.moveToFirst()) {
      Content content = new Content();
      content.setId(cursor.getString(cursor.getColumnIndex(
          OfflineEnduserContract.ContentEntry.COLUMN_NAME_JSON_ID)));
      content.setTitle(cursor.getString(cursor.getColumnIndex(
          OfflineEnduserContract.ContentEntry.COLUMN_NAME_TITLE)));
      content.setDescription(cursor.getString(cursor.getColumnIndex(
          OfflineEnduserContract.ContentEntry.COLUMN_NAME_DESCRIPTION)));
      content.setLanguage(cursor.getString(cursor.getColumnIndex(
          OfflineEnduserContract.ContentEntry.COLUMN_NAME_LANGUAGE)));
      content.setCategory(cursor.getInt(cursor.getColumnIndex(
          OfflineEnduserContract.ContentEntry.COLUMN_NAME_CATEGORY)));
      String tags = cursor.getString(cursor
          .getColumnIndex(OfflineEnduserContract.ContentEntry.COLUMN_NAME_TAGS));
      if (tags != null) {
        content.setTags(Arrays.asList(tags.split(",")));
      }
      content.setPublicImageUrl(cursor.getString(cursor.getColumnIndex(
          OfflineEnduserContract.ContentEntry.COLUMN_NAME_PUBLIC_IMAGE_URL)));
      content.setContentBlocks(relatedBlocks(cursor
          .getLong(cursor.getColumnIndex(OfflineEnduserContract.ContentEntry._ID))));
      return content;
    }
    return null;
  }

  public ArrayList<ContentBlock> relatedBlocks(long id) {
    ArrayList<ContentBlock> blocks = mContentBlockDatabaseAdapter.getRelatedContentBlocks(id);
    Collections.sort(blocks, new Comparator<ContentBlock>() {
      @Override
      public int compare(ContentBlock cb1, ContentBlock cb2) {
        return cb1.getId().compareTo(cb2.getId());
      }
    });
    return blocks;
  }

  // setter

  public void setContentBlockDatabaseAdapter(ContentBlockDatabaseAdapter contentBlockDatabaseAdapter) {
    mContentBlockDatabaseAdapter = contentBlockDatabaseAdapter;
  }
}
