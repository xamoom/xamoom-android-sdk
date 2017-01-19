/*
* Copyright 2017 by xamoom GmbH <apps@xamoom.com>
*
* This file is part of some open source application.
*
* Some open source application is free software: you can redistribute
* it and/or modify it under the terms of the GNU General Public
* License as published by the Free Software Foundation, either
* version 2 of the License, or (at your option) any later version.
*
* Some open source application is distributed in the hope that it will
* be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
* of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with xamoom-android-sdk. If not, see <http://www.gnu.org/licenses/>.
*
* author: Raphael Seher <raphael@xamoom.com>
*/

package com.xamoom.android.xamoomsdk.Storage.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
