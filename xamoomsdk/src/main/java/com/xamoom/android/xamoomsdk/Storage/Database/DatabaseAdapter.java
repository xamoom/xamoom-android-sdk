/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomsdk.Storage.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseAdapter {
  private DatabaseHelper mDatabaseHelper;
  protected Context mContext;
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
