package com.xamoom.android.xamoomsdk.Storage.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseAdapter {
  private Context mContext;
  private DatabaseHelper mDatabaseHelper;
  protected SQLiteDatabase mDatabase;

  public DatabaseAdapter(Context context) {
    this.mContext = context;
    this.mDatabaseHelper = new DatabaseHelper(context);
  }

  public DatabaseAdapter open() {
    this.mDatabase = mDatabaseHelper.getWritableDatabase();
    return this;
  }

  public void close() {
    mDatabaseHelper.close();
  }

  // getter & setter

  public void setDatabaseHelper(DatabaseHelper databaseHelper) {
    mDatabaseHelper = databaseHelper;
  }
}
