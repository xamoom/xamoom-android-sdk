package com.xamoom.android.xamoomsdk.Storage.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.xamoom.android.xamoomsdk.Resource.System;
import com.xamoom.android.xamoomsdk.Storage.TableContracts.OfflineEnduserContract.SystemEntry;


public class SystemDatabaseAdapter extends DatabaseAdapter {

  public SystemDatabaseAdapter(Context context) {
    super(context);
  }

  public System getSystem(String jsonId) {
    Cursor cursor = getCursor(jsonId);

    if (cursor.getCount() > 1) {
      // TODO: too many exception
    }

    if (cursor.moveToFirst()) {
      System system = new System();
      system.setId(cursor.getString(
          cursor.getColumnIndex(SystemEntry.COLUMN_NAME_JSON_ID)));
      system.setName(cursor.getString(
          cursor.getColumnIndex(SystemEntry.COLUMN_NAME_NAME)));
      return system;
    }

    return null;
  }

  public long insertOrUpdateSystem(System system) {
    ContentValues values = new ContentValues();
    values.put(SystemEntry.COLUMN_NAME_JSON_ID, system.getId());
    values.put(SystemEntry.COLUMN_NAME_NAME, system.getName());
    if (system.getMenu() != null) {
      // TODO: insert menu
    }

    if (system.getStyle() != null) {
      // TODO: insert style
    }

    if (system.getSystemSetting() != null) {
      // TODO: insert settings
    }

    Cursor cursor = getCursor(system.getId());
    if (cursor != null) {
      if (cursor.moveToFirst()) {
        long rows = updateSystem(cursor.getInt(
            cursor.getColumnIndex(SystemEntry._ID)),
            values);
        return rows;
      }
    }

    open();
    long rows = mDatabase.insert(SystemEntry.TABLE_NAME,
        null, values);
    close();

    return rows;
  }

  public Cursor getCursor(String jsonId) {
    open();

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

    close();

    return cursor;
  }

  private long updateSystem(int id, ContentValues values) {
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
}
