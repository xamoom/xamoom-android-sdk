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

import com.xamoom.android.xamoomsdk.Resource.System;
import com.xamoom.android.xamoomsdk.Storage.TableContracts.OfflineEnduserContract.SystemEntry;

public class SystemDatabaseAdapter extends DatabaseAdapter {
  private static SystemDatabaseAdapter mSharedInstance;

  private StyleDatabaseAdapter mStyleDatabaseAdapter;
  private SettingDatabaseAdapter mSettingDatabaseAdapter;
  private MenuDatabaseAdapter mMenuDatabaseAdapter;

  public static SystemDatabaseAdapter getInstance(Context context) {
    if (mSharedInstance == null) {
      mSharedInstance = new SystemDatabaseAdapter(context);
    }
    return mSharedInstance;
  }

  private SystemDatabaseAdapter(Context context) {
    super(context);
  }

  public System getSystem() {
    return getSystem(null, null);
  }

  public System getSystem(String jsonId) {
    String selection = SystemEntry.COLUMN_NAME_JSON_ID + " = ?";
    String[] selectionArgs = {jsonId};

    return getSystem(selection, selectionArgs);
  }

  public System getSystem(long row) {
    String selection = SystemEntry._ID + " = ?";
    String[] selectionArgs = {String.valueOf(row)};

    return getSystem(selection, selectionArgs);
  }

  private System getSystem(String selection, String[] selectionArgs) {
    open();
    Cursor cursor = querySystems(selection, selectionArgs);
    System system = cursorToSystem(cursor);

    close();
    return system;
  }

  public long insertOrUpdateSystem(System system) {
    ContentValues values = new ContentValues();
    values.put(SystemEntry.COLUMN_NAME_JSON_ID, system.getId());
    values.put(SystemEntry.COLUMN_NAME_NAME, system.getName());
    values.put(SystemEntry.COLUMN_NAME_WEBCLIENT_URL, system.getWebClientUrl());

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
      long menuRow = getMenuDatabaseAdapter().
          insertOrUpdate(system.getMenu(), row);
    }

    if (system.getStyle() != null) {
      long styleRow = getStyleDatabaseAdapter().
          insertOrUpdateStyle(system.getStyle(), row);
    }

    if (system.getSystemSetting() != null) {
      long settingRow = getSettingDatabaseAdapter().
          insertOrUpdateSetting(system.getSystemSetting(), row);
    }
    return row;
  }

  public boolean deleteSystem(String jsonId) {
    String selection = SystemEntry.COLUMN_NAME_JSON_ID + " = ?";
    String[] selectionArgs = { jsonId };

    open();
    int rowsAffected = mDatabase.delete(SystemEntry.COLUMN_NAME_JSON_ID, selection,
        selectionArgs);
    close();

    return rowsAffected >= 1;
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

  private Cursor querySystems(String selection, String[] selectionArgs) {
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
    String selection = SystemEntry.COLUMN_NAME_JSON_ID + " = ?";
    String[] selectionArgs = {jsonId};

    open();
    Cursor cursor = querySystems(selection, selectionArgs);
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
      system.setWebClientUrl(cursor.getString(
          cursor.getColumnIndex(SystemEntry.COLUMN_NAME_WEBCLIENT_URL)));
      system.setStyle(getStyleDatabaseAdapter().getRelatedStyle(
          cursor.getLong(cursor.getColumnIndex(SystemEntry._ID))));
      system.setSystemSetting(getSettingDatabaseAdapter().getSystemSetting(
          cursor.getLong(cursor.getColumnIndex(SystemEntry._ID))));
      system.setMenu(getMenuDatabaseAdapter().getRelatedMenu(
          cursor.getLong(cursor.getColumnIndex(SystemEntry._ID))));
      return system;
    }

    return null;
  }

  // getter & setter

  public StyleDatabaseAdapter getStyleDatabaseAdapter() {
    if (mStyleDatabaseAdapter == null) {
      mStyleDatabaseAdapter = StyleDatabaseAdapter.getInstance(mContext);
    }
    return mStyleDatabaseAdapter;
  }

  public SettingDatabaseAdapter getSettingDatabaseAdapter() {
    if (mSettingDatabaseAdapter == null) {
      mSettingDatabaseAdapter = SettingDatabaseAdapter.getInstance(mContext);
    }
    return mSettingDatabaseAdapter;
  }

  public MenuDatabaseAdapter getMenuDatabaseAdapter() {
    if (mMenuDatabaseAdapter == null) {
      mMenuDatabaseAdapter = MenuDatabaseAdapter.getInstance(mContext);
    }
    return mMenuDatabaseAdapter;
  }

  public void setStyleDatabaseAdapter(StyleDatabaseAdapter styleDatabaseAdapter) {
    mStyleDatabaseAdapter = styleDatabaseAdapter;
  }

  public void setSettingDatabaseAdapter(SettingDatabaseAdapter settingDatabaseAdapter) {
    mSettingDatabaseAdapter = settingDatabaseAdapter;
  }

  public void setMenuDatabaseAdapter(MenuDatabaseAdapter menuDatabaseAdapter) {
    mMenuDatabaseAdapter = menuDatabaseAdapter;
  }
}
