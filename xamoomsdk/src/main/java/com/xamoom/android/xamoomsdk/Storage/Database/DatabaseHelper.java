package com.xamoom.android.xamoomsdk.Storage.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xamoom.android.xamoomsdk.Storage.TableContracts.OfflineEnduserContract;
import com.xamoom.android.xamoomsdk.Storage.TableContracts.OfflineEnduserContract.SystemEntry;

public class DatabaseHelper extends SQLiteOpenHelper {

  public DatabaseHelper(Context context) {
    super(context, OfflineEnduserContract.DATABASE_NAME, null,
        OfflineEnduserContract.DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase) {
    sqLiteDatabase.execSQL(SystemEntry.SYSTEM_CREATE_TABLE);
    // TODO: create other tables
  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    // TODO: implement upgrading
  }
}
