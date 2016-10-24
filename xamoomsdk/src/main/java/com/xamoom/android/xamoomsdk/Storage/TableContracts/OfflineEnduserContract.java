package com.xamoom.android.xamoomsdk.Storage.TableContracts;

import android.provider.BaseColumns;

/**
 * Created by raphaelseher on 24/10/2016.
 */

public class OfflineEnduserContract {
  private OfflineEnduserContract() {}
  public static final int DATABASE_VERSION = 1;
  public static final String DATABASE_NAME = "OfflineEnduser.db";
  public static final String TEXT_TYPE = " TEXT";
  public static final String COMMA_SEP = ",";

  /**
   * System
   */
  public static class SystemEntry implements BaseColumns {
    public static final String TABLE_NAME = "System";
    public static final String COLUMN_NAME_JSON_ID = "json_id";
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_URL = "url";
    public static final String COLUMN_NAME_STYLE = "style";
    public static final String COLUMN_NAME_SYSTEMSETTING = "setting";
    public static final String COLUMN_NAME_MENU = "menu";

    public static final String SYSTEM_CREATE_TABLE =
        "CREATE TABLE " + SystemEntry.TABLE_NAME + " (" +
            SystemEntry._ID + " INTEGER PRIMARY KEY," +
            SystemEntry.COLUMN_NAME_JSON_ID + TEXT_TYPE + COMMA_SEP +
            SystemEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
            SystemEntry.COLUMN_NAME_URL + TEXT_TYPE + COMMA_SEP +
            SystemEntry.COLUMN_NAME_STYLE + TEXT_TYPE + COMMA_SEP +
            SystemEntry.COLUMN_NAME_SYSTEMSETTING + TEXT_TYPE + COMMA_SEP +
            SystemEntry.COLUMN_NAME_MENU + TEXT_TYPE + " )";

    public static  final String[] PROJECTION = {
          SystemEntry._ID,
          SystemEntry.COLUMN_NAME_JSON_ID,
          SystemEntry.COLUMN_NAME_NAME,
          SystemEntry.COLUMN_NAME_URL,
          SystemEntry.COLUMN_NAME_STYLE,
          SystemEntry.COLUMN_NAME_SYSTEMSETTING,
          SystemEntry.COLUMN_NAME_MENU
    };
  }
}
