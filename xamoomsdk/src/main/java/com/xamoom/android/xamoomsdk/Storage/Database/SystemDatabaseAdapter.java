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
      long menuRow = getMenuDatabaseAdapter().insertOrUpdate(system.getMenu());
      if (menuRow != -1) {
        values.put(SystemEntry.COLUMN_NAME_MENU, menuRow);
        updateSystem(row, values);
      }
    }

    if (system.getStyle() != null) {
      long styleRow = getStyleDatabaseAdapter().insertOrUpdateStyle(system.getStyle());
      if (styleRow != -1) {
        values.put(SystemEntry.COLUMN_NAME_STYLE, system.getStyle().getId());
        updateSystem(row, values);
      }
    }

    if (system.getSystemSetting() != null) {
      long settingRow = getSettingDatabaseAdapter().insertOrUpdateSetting(system.getSystemSetting());
      if (settingRow != -1) {
        values.put(SystemEntry.COLUMN_NAME_SYSTEMSETTING, system.getSystemSetting().getId());
        updateSystem(row, values);
      }
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
      system.setStyle(mStyleDatabaseAdapter.getStyle(
          cursor.getString(cursor.getColumnIndex(SystemEntry.COLUMN_NAME_STYLE))));
      // TODO: change to FK
      system.setSystemSetting(mSettingDatabaseAdapter.getSystemSetting(
          cursor.getString(cursor.getColumnIndex(SystemEntry.COLUMN_NAME_SYSTEMSETTING))));
      // TODO: change to FK
      system.setMenu(mMenuDatabaseAdapter.getMenu(cursor.getLong(
          cursor.getColumnIndex(SystemEntry.COLUMN_NAME_MENU))));
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
