package com.xamoom.android.xamoomsdk.Storage.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;

import com.xamoom.android.xamoomsdk.Resource.Style;
import com.xamoom.android.xamoomsdk.Resource.System;
import com.xamoom.android.xamoomsdk.Storage.TableContracts.OfflineEnduserContract;
import com.xamoom.android.xamoomsdk.Storage.TableContracts.OfflineEnduserContract.SystemEntry;


public class SystemDatabaseAdapter extends DatabaseAdapter {

  public StyleDatabaseAdapter mStyleDatabaseAdapter;


  public SystemDatabaseAdapter(Context context) {
    super(context);
    mStyleDatabaseAdapter = new StyleDatabaseAdapter(mContext);
  }

  public System getSystem(String jsonId) {
    open();
    Cursor cursor = querySystems(jsonId);

    if (cursor.getCount() > 1) {
      // TODO: too many exception
    }

    System system = cursorToSystem(cursor);

    close();
    return system;
  }

  public long insertOrUpdateSystem(System system) {
    ContentValues values = new ContentValues();
    values.put(SystemEntry.COLUMN_NAME_JSON_ID, system.getId());
    values.put(SystemEntry.COLUMN_NAME_NAME, system.getName());

    long row = getPrimaryKey(system.getId());
    if (row != -1) {
      updateSystem(row, values);
    } else {
      open();
      row = mDatabase.insert(SystemEntry.TABLE_NAME,
          null, values);
      close();
    }

    if (system.getMenu() != null) {
      // TODO: insert menu
    }

    if (system.getStyle() != null) {
      long styleRow = mStyleDatabaseAdapter.insertOrUpdateStyle(system.getStyle());
      if (styleRow != -1) {
        values.put(SystemEntry.COLUMN_NAME_STYLE, system.getStyle().getId());
        updateSystem(row, values);
      }
    }

    if (system.getSystemSetting() != null) {
      // TODO: insert settings
    }

    return row;
  }

  private long updateSystem(long id, ContentValues values) {
    String selection = SystemEntry._ID + " = ?";
    String[] selectionArgs = { String.valueOf(id) };

    open();
    int rowsUpdated = mDatabase.update(SystemEntry.TABLE_NAME,
        values,
        selection,
        selectionArgs);
    close();

    return rowsUpdated;
  }

  private Cursor querySystems(String jsonId) {
    String selection = SystemEntry.COLUMN_NAME_JSON_ID + " = ?";
    String[] selectionArgs = { jsonId };

    Cursor cursor = mDatabase.query(
        SystemEntry.TABLE_NAME,
        SystemEntry.PROJECTION,
        selection,
        selectionArgs,
        null,
        null,
        null
    );

    return cursor;
  }

  private long getPrimaryKey(String jsonId) {
    open();
    Cursor cursor = querySystems(jsonId);
    if (cursor != null) {
      if (cursor.moveToFirst()) {
        long id = cursor.getLong(cursor.getColumnIndex(SystemEntry._ID));
        close();
        return id;
      }
    }
    close();
    return -1;
  }

  private System cursorToSystem(Cursor cursor) {
    if (cursor.moveToFirst()) {
      System system = new System();
      system.setId(cursor.getString(
          cursor.getColumnIndex(SystemEntry.COLUMN_NAME_JSON_ID)));
      system.setName(cursor.getString(
          cursor.getColumnIndex(SystemEntry.COLUMN_NAME_NAME)));
      system.setStyle(mStyleDatabaseAdapter.getStyle(
          cursor.getString(cursor.getColumnIndex(SystemEntry.COLUMN_NAME_STYLE))));
      return system;
    }

    return null;
  }

  // getter & setter

  public void setStyleDatabaseAdapter(StyleDatabaseAdapter styleDatabaseAdapter) {
    mStyleDatabaseAdapter = styleDatabaseAdapter;
  }
}
