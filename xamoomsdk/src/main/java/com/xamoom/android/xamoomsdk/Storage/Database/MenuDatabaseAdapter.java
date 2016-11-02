package com.xamoom.android.xamoomsdk.Storage.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;

import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.Menu;
import com.xamoom.android.xamoomsdk.Storage.TableContracts.OfflineEnduserContract;

public class MenuDatabaseAdapter extends DatabaseAdapter {

  private ContentDatabaseAdapter mContentDatabaseAdapter;

  public MenuDatabaseAdapter(Context context) {
    super(context);
    mContentDatabaseAdapter = ContentDatabaseAdapter.getInstance(context);
  }

  public Menu getMenu(String jsonId) {
    String selection = OfflineEnduserContract.MenuEntry.COLUMN_NAME_JSON_ID  + " = ?";
    String[] selectionArgs = {jsonId};

    open();
    Cursor cursor = queryMenu(selection, selectionArgs);

    if (cursor.getCount() > 1) {
      // TODO: too many exception
    }

    Menu menu = cursorToMenu(cursor);

    return menu;
  }

  public long insertOrUpdate(Menu menu) {
    ContentValues values = new ContentValues();
    values.put(OfflineEnduserContract.MenuEntry.COLUMN_NAME_JSON_ID, menu.getId());

    long row = getPrimaryKey(menu.getId());
    if (row != -1) {
      updateMenu(row, values);
    } else {
      open();
      row = mDatabase.insert(OfflineEnduserContract.MenuEntry.TABLE_NAME,
          null, values);
      close();
    }

    if (menu.getItems() != null) {
      for (Content content : menu.getItems()) {
        mContentDatabaseAdapter.insertOrUpdateContent(content, true, row);
      }
    }

    return row;
  }

  private int updateMenu(long row, ContentValues values) {
    String selection = OfflineEnduserContract.MenuEntry._ID + " = ?";
    String[] selectionArgs = {String.valueOf(row)};

    open();
    int rowsUpdated = mDatabase.update(OfflineEnduserContract.MenuEntry.TABLE_NAME, values,
        selection, selectionArgs);
    close();

    return rowsUpdated;
  }

  private long getPrimaryKey(String jsonId) {
    String selection = OfflineEnduserContract.MenuEntry.COLUMN_NAME_JSON_ID + " = ?";
    String[] selectionArgs = {jsonId};

    open();
    Cursor cursor = queryMenu(selection, selectionArgs);

    if (cursor != null) {
      if (cursor.moveToFirst()) {
        long id = cursor.getInt(cursor.getColumnIndex(OfflineEnduserContract.MenuEntry._ID));
        close();
        return id;
      }
    }
    close();
    return -1;
  }

  private Menu cursorToMenu(Cursor cursor) {
    if (cursor.moveToFirst()) {
      Menu menu = new Menu();
      menu.setId(cursor.getString(cursor
          .getColumnIndex(OfflineEnduserContract.MenuEntry.COLUMN_NAME_JSON_ID)));
      menu.setItems(mContentDatabaseAdapter.getRelatedContents(cursor.getLong(
          cursor.getColumnIndex(OfflineEnduserContract.MenuEntry._ID)
      )));
      return menu;
    }
    return null;
  }

  private Cursor queryMenu(String selection, String[] selectionArgs) {
    Cursor cursor = mDatabase.query(OfflineEnduserContract.MenuEntry.TABLE_NAME,
        OfflineEnduserContract.MenuEntry.PROJECTION,
        selection,
        selectionArgs,
        null, null, null);

    return cursor;
  }

  // setter

  public void setContentDatabaseAdapter(ContentDatabaseAdapter contentDatabaseAdapter) {
    mContentDatabaseAdapter = contentDatabaseAdapter;
  }
}
