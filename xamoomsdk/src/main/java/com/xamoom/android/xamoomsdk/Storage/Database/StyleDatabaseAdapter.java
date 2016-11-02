package com.xamoom.android.xamoomsdk.Storage.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.xamoom.android.xamoomsdk.Resource.Style;
import com.xamoom.android.xamoomsdk.Storage.TableContracts.OfflineEnduserContract;
import com.xamoom.android.xamoomsdk.Storage.TableContracts.OfflineEnduserContract.StyleEntry;

public class StyleDatabaseAdapter extends DatabaseAdapter {

  private static StyleDatabaseAdapter mSharedInstance;

  public static StyleDatabaseAdapter getInstance(Context context) {
    if (mSharedInstance == null) {
      mSharedInstance = new StyleDatabaseAdapter(context);
    }
    return mSharedInstance;
  }

  private StyleDatabaseAdapter(Context context) {
    super(context);
  }

  public Style getStyle(String jsonId) {
    String selection = OfflineEnduserContract.StyleEntry.COLUMN_NAME_JSON_ID + " = ?";
    String[] selectionArgs = {jsonId};

    open();

    Cursor cursor = queryStyles(selection, selectionArgs);

    if (cursor.getCount() > 1) {
      return null;
      // TODO: throw to many exception
    }

    Style style = null;
    if (cursor.moveToFirst()) {
      style = cursorToStyle(cursor);
    }

    close();
    return style;
  }

  public long insertOrUpdateStyle(Style style) {
    ContentValues values = new ContentValues();
    values.put(StyleEntry.COLUMN_NAME_JSON_ID, style.getId());
    values.put(StyleEntry.COLUMN_NAME_BACKGROUND_COLOR, style.getBackgroundColor());
    values.put(StyleEntry.COLUMN_NAME_HIGHLIGHT_COLOR, style.getHighlightFontColor());
    values.put(StyleEntry.COLUMN_NAME_FOREGROUND_COLOR, style.getForegroundFontColor());
    values.put(StyleEntry.COLUMN_NAME_CHROME_HEADER_COLOR, style.getChromeHeaderColor());
    values.put(StyleEntry.COLUMN_NAME_MAP_PIN, style.getCustomMarker());
    values.put(StyleEntry.COLUMN_NAME_ICON, style.getIcon());

    long row = getPrimaryKey(style.getId());
    if (row != -1) {
      updateStyle(row, values);
    } else {
      open();
      row = mDatabase.insert(StyleEntry.TABLE_NAME, null, values);
      close();
    }

    return row;
  }

  public int updateStyle(long id, ContentValues values) {
    String selection = StyleEntry._ID + " = ?";
    String[] selectionArgs = { String.valueOf(id) };

    open();
    int rows = mDatabase.update(StyleEntry.TABLE_NAME, values, selection, selectionArgs);
    close();

    return rows;
  }

  private Cursor queryStyles(String selection, String[] args) {
    Cursor cursor = mDatabase.query(
        OfflineEnduserContract.StyleEntry.TABLE_NAME,
        OfflineEnduserContract.StyleEntry.PROJECTION,
        selection,
        args,
        null,
        null,
        null
    );

    return cursor;
  }

  private long getPrimaryKey(String jsonId) {
    String selection = OfflineEnduserContract.StyleEntry.COLUMN_NAME_JSON_ID + " = ?";
    String[] selectionArgs = { jsonId };

    open();
    Cursor cursor = queryStyles(selection, selectionArgs);
    if (cursor != null) {
      if (cursor.moveToFirst()) {
        long id = cursor.getLong(cursor.getColumnIndex(StyleEntry._ID));
        close();
        return id;
      }
    }
    close();
    return -1;
  }

  private Style cursorToStyle(Cursor cursor) {
    if (cursor.moveToFirst()) {
      Style style = new Style();
      style.setId(cursor.getString(
          cursor.getColumnIndex(StyleEntry.COLUMN_NAME_JSON_ID)));
      style.setBackgroundColor(cursor.getString(
          cursor.getColumnIndex(StyleEntry.COLUMN_NAME_BACKGROUND_COLOR)));
      style.setHighlightFontColor(cursor.getString(
          cursor.getColumnIndex(StyleEntry.COLUMN_NAME_HIGHLIGHT_COLOR)));
      style.setForegroundFontColor(cursor.getString(
          cursor.getColumnIndex(StyleEntry.COLUMN_NAME_FOREGROUND_COLOR)));
      style.setChromeHeaderColor(cursor.getString(
          cursor.getColumnIndex(StyleEntry.COLUMN_NAME_CHROME_HEADER_COLOR)));
      style.setCustomMarker(cursor.getString(
          cursor.getColumnIndex(StyleEntry.COLUMN_NAME_MAP_PIN)));
      style.setIcon(cursor.getString(
          cursor.getColumnIndex(StyleEntry.COLUMN_NAME_ICON)));

      return style;
    }

    return null;
  }
}
