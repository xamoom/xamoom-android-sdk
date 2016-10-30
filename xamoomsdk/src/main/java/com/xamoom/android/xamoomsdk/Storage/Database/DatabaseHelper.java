package com.xamoom.android.xamoomsdk.Storage.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    // TODO: implement upgrading
  }
}
