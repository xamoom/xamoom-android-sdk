package com.xamoom.android.xamoomsdk.Storage.Database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.xamoom.android.xamoomsdk.Resource.SystemSetting;
import com.xamoom.android.xamoomsdk.Storage.TableContracts.OfflineEnduserContract;

public class SettingDatabaseAdapter extends DatabaseAdapter {
  private static SettingDatabaseAdapter mSharedInstance;

  public static SettingDatabaseAdapter getInstance(Context context) {
    if (mSharedInstance == null) {
      mSharedInstance = new SettingDatabaseAdapter(context);
    }
    return mSharedInstance;
  }

  private SettingDatabaseAdapter(Context context) {
    super(context);
  }

  public SystemSetting getRelatedSystemSetting(String jsonId) {
    String selection = OfflineEnduserContract.SettingEntry.
        COLUMN_NAME_JSON_ID + " = ?";
    String[] selectionArgs = {jsonId};

    return getRelatedSystemSetting(selection, selectionArgs);
  }

  public SystemSetting getRelatedSystemSetting(long systemRow) {
    String selection = OfflineEnduserContract.SettingEntry.
        _ID + " = ?";
    String[] selectionArgs = {String.valueOf(systemRow)};

    return getRelatedSystemSetting(selection, selectionArgs);
  }

  private SystemSetting getRelatedSystemSetting(String selection, String[] selectionArgs) {
    open();
    Cursor cursor = querySettings(selection, selectionArgs);

    if (cursor.getCount() > 1) {
      return null;
      // TODO: throw too many exception
    }

    SystemSetting setting = cursorToSetting(cursor);

    close();
    return setting;
  }

  public long insertOrUpdateSetting(SystemSetting setting, long systemRow) {
    ContentValues values = new ContentValues();
    values.put(OfflineEnduserContract.SettingEntry.COLUMN_NAME_JSON_ID,
        setting.getId());
    values.put(OfflineEnduserContract.SettingEntry.COLUMN_NAME_APPSTORE_ID,
        setting.getItunesAppId());
    values.put(OfflineEnduserContract.SettingEntry.COLUMN_NAME_PLAYSTORE_ID,
        setting.getGooglePlayAppId());
    if (systemRow != -1) {
      values.put(OfflineEnduserContract.SettingEntry.COLUMN_NAME_SYSTEM_RELATION,
          systemRow);
    }

    long row = getPrimaryKey(setting.getId());
    if (row != -1) {
      updateSetting(row, values);
    } else {
      open();
      row = mDatabase.insert(OfflineEnduserContract.SettingEntry.TABLE_NAME,
          null, values);
      close();
    }

    return row;
  }

  private void updateSetting(long row, ContentValues values) {
    String selection = OfflineEnduserContract.
        SettingEntry._ID + " = ?";
    String[] selectionArgs = {String.valueOf(row)};

    open();
    int rows = mDatabase.update(
        OfflineEnduserContract.SettingEntry.TABLE_NAME,
        values, selection, selectionArgs);
    close();
  }

  private long getPrimaryKey(String jsonId) {
    String selection = OfflineEnduserContract.
        SettingEntry.COLUMN_NAME_JSON_ID + " = ?";
    String[] selectionArgs = {jsonId};

    open();
    Cursor cursor = querySettings(selection, selectionArgs);
    if (cursor != null && cursor.moveToFirst()) {
      long id = cursor.getLong(cursor.getColumnIndex(
          OfflineEnduserContract.SettingEntry._ID));
      close();
      return id;
    }
    return -1;
  }

  private SystemSetting cursorToSetting(Cursor cursor) {
    if (cursor.moveToFirst()) {
      SystemSetting setting = new SystemSetting();
      setting.setId(cursor.getString(cursor.getColumnIndex(
          OfflineEnduserContract.StyleEntry.COLUMN_NAME_JSON_ID)));
      setting.setItunesAppId(cursor.getString(cursor.getColumnIndex(
          OfflineEnduserContract.SettingEntry.COLUMN_NAME_APPSTORE_ID)));
      setting.setGooglePlayAppId(cursor.getString(
          cursor.getColumnIndex(OfflineEnduserContract.SettingEntry.COLUMN_NAME_PLAYSTORE_ID)));
      return setting;
    }
    return null;
  }

  private Cursor querySettings(String selection, String[] selectionArgs) {
    Cursor cursor = mDatabase.query(
        OfflineEnduserContract.SettingEntry.TABLE_NAME,
        OfflineEnduserContract.SettingEntry.PROJECTION,
        selection,
        selectionArgs,
        null,
        null,
        null
    );
    return cursor;
  }
}
