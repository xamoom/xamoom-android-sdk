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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.xamoom.android.xamoomsdk.Storage.TableContracts.OfflineEnduserContract;

public class DatabaseHelper extends SQLiteOpenHelper {

  public DatabaseHelper(Context context) {
    super(context, OfflineEnduserContract.DATABASE_NAME, null,
        OfflineEnduserContract.DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase) {
    sqLiteDatabase.execSQL(OfflineEnduserContract.SystemEntry.CREATE_TABLE);
    sqLiteDatabase.execSQL(OfflineEnduserContract.StyleEntry.CREATE_TABLE);
    sqLiteDatabase.execSQL(OfflineEnduserContract.SettingEntry.CREATE_TABLE);
    sqLiteDatabase.execSQL(OfflineEnduserContract.ContentEntry.CREATE_TABLE);
    sqLiteDatabase.execSQL(OfflineEnduserContract.ContentBlockEntry.CREATE_TABLE);
    sqLiteDatabase.execSQL(OfflineEnduserContract.MenuEntry.CREATE_TABLE);
    sqLiteDatabase.execSQL(OfflineEnduserContract.MarkerEntry.CREATE_TABLE);
    sqLiteDatabase.execSQL(OfflineEnduserContract.SpotEntry.CREATE_TABLE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    switch (oldVersion) {
      case 1:
        // add copyright column to ContentBlock
        sqLiteDatabase.execSQL("ALTER TABLE " +
            OfflineEnduserContract.ContentBlockEntry.TABLE_NAME +
            " ADD " +
            OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_COPYRIGHT
            + OfflineEnduserContract.TEXT_TYPE);
    }
  }
}
