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
      case 2:
        // add content list columns
        sqLiteDatabase.execSQL("ALTER TABLE " +
            OfflineEnduserContract.ContentBlockEntry.TABLE_NAME +
            " ADD " +
            OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_CONTENT_LIST_TAGS + OfflineEnduserContract.TEXT_TYPE);
        sqLiteDatabase.execSQL("ALTER TABLE " +
            OfflineEnduserContract.ContentBlockEntry.TABLE_NAME +
            " ADD " +
            OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_CONTENT_LIST_PAGE_SIZE + OfflineEnduserContract.INTEGER_TYPE);
        sqLiteDatabase.execSQL("ALTER TABLE " +
            OfflineEnduserContract.ContentBlockEntry.TABLE_NAME +
            " ADD " +
            OfflineEnduserContract.ContentBlockEntry.COLUMN_NAME_CONTENT_LIST_SORT_ASC + OfflineEnduserContract.INTEGER_TYPE);
    }
  }
}
