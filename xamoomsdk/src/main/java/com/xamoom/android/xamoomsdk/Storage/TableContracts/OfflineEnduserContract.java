/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomsdk.Storage.TableContracts;

import android.provider.BaseColumns;
import android.provider.Settings;

import com.xamoom.android.xamoomsdk.Resource.ContentBlock;

public class OfflineEnduserContract {
  public static final int DATABASE_VERSION = 4;
  public static final String DATABASE_NAME = "OfflineEnduser.db";

  public static final String TEXT_TYPE = " TEXT";
  public static final String INTEGER_TYPE = " INTEGER";
  public static final String REAL_TYPE = " REAL";
  public static final String UNIQUE = " UNIQUE";
  public static final String COMMA_SEP = ",";

  private OfflineEnduserContract() {}

  /**
   * System
   */
  public static class SystemEntry implements BaseColumns {
    public static final String TABLE_NAME = "System";
    public static final String COLUMN_NAME_JSON_ID = "json_id";
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_URL = "url";
    public static final String COLUMN_NAME_WEBCLIENT_URL = "webclientUrl";

    public static final String CREATE_TABLE =
        "CREATE TABLE " + SystemEntry.TABLE_NAME + " (" +
            SystemEntry._ID + " INTEGER PRIMARY KEY," +
            SystemEntry.COLUMN_NAME_JSON_ID + TEXT_TYPE + UNIQUE + COMMA_SEP +
            SystemEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
            SystemEntry.COLUMN_NAME_URL + TEXT_TYPE + COMMA_SEP +
            SystemEntry.COLUMN_NAME_WEBCLIENT_URL + TEXT_TYPE + " )";

    public static  final String[] PROJECTION = {
          SystemEntry._ID,
          SystemEntry.COLUMN_NAME_JSON_ID,
          SystemEntry.COLUMN_NAME_NAME,
          SystemEntry.COLUMN_NAME_URL,
          SystemEntry.COLUMN_NAME_WEBCLIENT_URL
    };
  }

  /**
   * Style
   */
  public static class StyleEntry implements BaseColumns {
    public static final String TABLE_NAME = "Style";
    public static final String COLUMN_NAME_JSON_ID = "json_id";
    public static final String COLUMN_NAME_SYSTEM_RELATION = "system";
    public static final String COLUMN_NAME_BACKGROUND_COLOR = "background_color";
    public static final String COLUMN_NAME_HIGHLIGHT_COLOR = "highlight_color";
    public static final String COLUMN_NAME_FOREGROUND_COLOR = "foreground_color";
    public static final String COLUMN_NAME_CHROME_HEADER_COLOR = "chrome_header_color";
    public static final String COLUMN_NAME_MAP_PIN = "map_pin";
    public static final String COLUMN_NAME_ICON = "icon";

    public static final String CREATE_TABLE =
        "CREATE TABLE " + StyleEntry.TABLE_NAME + " (" +
            StyleEntry._ID + " INTEGER PRIMARY KEY," +
            StyleEntry.COLUMN_NAME_JSON_ID + TEXT_TYPE + UNIQUE + COMMA_SEP +
            StyleEntry.COLUMN_NAME_SYSTEM_RELATION + INTEGER_TYPE + COMMA_SEP +
            StyleEntry.COLUMN_NAME_BACKGROUND_COLOR + TEXT_TYPE + COMMA_SEP +
            StyleEntry.COLUMN_NAME_HIGHLIGHT_COLOR + TEXT_TYPE + COMMA_SEP +
            StyleEntry.COLUMN_NAME_FOREGROUND_COLOR + TEXT_TYPE + COMMA_SEP +
            StyleEntry.COLUMN_NAME_CHROME_HEADER_COLOR + TEXT_TYPE + COMMA_SEP +
            StyleEntry.COLUMN_NAME_MAP_PIN + TEXT_TYPE + COMMA_SEP +
            StyleEntry.COLUMN_NAME_ICON + TEXT_TYPE + " )";

    public static  final String[] PROJECTION = {
        StyleEntry._ID,
        StyleEntry.COLUMN_NAME_JSON_ID,
        StyleEntry.COLUMN_NAME_SYSTEM_RELATION,
        StyleEntry.COLUMN_NAME_BACKGROUND_COLOR,
        StyleEntry.COLUMN_NAME_HIGHLIGHT_COLOR,
        StyleEntry.COLUMN_NAME_FOREGROUND_COLOR,
        StyleEntry.COLUMN_NAME_CHROME_HEADER_COLOR,
        StyleEntry.COLUMN_NAME_MAP_PIN,
        StyleEntry.COLUMN_NAME_ICON,
    };
  }

  /**
   * Settings
   */
  public static class SettingEntry implements BaseColumns {
    public static final String TABLE_NAME = "Settings";
    public static final String COLUMN_NAME_JSON_ID = "json_id";
    public static final String COLUMN_NAME_SYSTEM_RELATION = "system";
    public static final String COLUMN_NAME_PLAYSTORE_ID = "playstore_id";
    public static final String COLUMN_NAME_APPSTORE_ID = "appstore_id";
    public static final String COLUMN_NAME_SOCIAL_SHARING_ENABLED = "socialSharingEnabled";

    public static final String CREATE_TABLE =
        "CREATE TABLE " + SettingEntry.TABLE_NAME + " (" +
            SettingEntry._ID + " INTEGER PRIMARY KEY," +
            SettingEntry.COLUMN_NAME_JSON_ID + TEXT_TYPE + UNIQUE + COMMA_SEP +
            SettingEntry.COLUMN_NAME_SYSTEM_RELATION + INTEGER_TYPE + COMMA_SEP +
            SettingEntry.COLUMN_NAME_PLAYSTORE_ID + TEXT_TYPE + COMMA_SEP +
            SettingEntry.COLUMN_NAME_APPSTORE_ID + TEXT_TYPE + COMMA_SEP +
            SettingEntry.COLUMN_NAME_SOCIAL_SHARING_ENABLED + INTEGER_TYPE + " )";

    public static final String[] PROJECTION = {
        SettingEntry._ID,
        SettingEntry.COLUMN_NAME_JSON_ID,
        SettingEntry.COLUMN_NAME_SYSTEM_RELATION,
        SettingEntry.COLUMN_NAME_PLAYSTORE_ID,
        SettingEntry.COLUMN_NAME_APPSTORE_ID,
        SettingEntry.COLUMN_NAME_SOCIAL_SHARING_ENABLED
    };
  }

  /**
   * Spot
   */
  public static class SpotEntry implements BaseColumns {
    public static final String TABLE_NAME = "Spot";
    public static final String COLUMN_NAME_JSON_ID = "json_id";
    public static final String COLUMN_NAME_RELATION_SYSTEM = "system";
    public static final String COLUMN_NAME_RELATION_CONTENT = "content";
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_DESCRIPTION = "description";
    public static final String COLUMN_NAME_PUBLIC_IMAGE_URL = "publicImageUrl";
    public static final String COLUMN_NAME_LOCATION_LAT = "locationLat";
    public static final String COLUMN_NAME_LOCATION_LON = "locationLon";
    public static final String COLUMN_NAME_TAGS = "tags";
    public static final String COLUMN_NAME_CATEGORY = "category";
    public static final String COLUMN_NAME_CUSTOM_META = "customMeta";

    public static final String CREATE_TABLE =
        "CREATE TABLE " + SpotEntry.TABLE_NAME + " (" +
            SpotEntry._ID + " INTEGER PRIMARY KEY," +
            SpotEntry.COLUMN_NAME_JSON_ID + TEXT_TYPE + UNIQUE + COMMA_SEP +
            SpotEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
            SpotEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
            SpotEntry.COLUMN_NAME_PUBLIC_IMAGE_URL + TEXT_TYPE + COMMA_SEP +
            SpotEntry.COLUMN_NAME_LOCATION_LAT + REAL_TYPE + COMMA_SEP +
            SpotEntry.COLUMN_NAME_LOCATION_LON + REAL_TYPE + COMMA_SEP +
            SpotEntry.COLUMN_NAME_TAGS + TEXT_TYPE + COMMA_SEP +
            SpotEntry.COLUMN_NAME_CATEGORY + INTEGER_TYPE + COMMA_SEP +
            SpotEntry.COLUMN_NAME_RELATION_SYSTEM + INTEGER_TYPE + COMMA_SEP +
            SpotEntry.COLUMN_NAME_RELATION_CONTENT + INTEGER_TYPE + COMMA_SEP +
            SpotEntry.COLUMN_NAME_CUSTOM_META + TEXT_TYPE + " )";

    public static final String[] PROJECTION = {
        _ID,
        COLUMN_NAME_JSON_ID,
        COLUMN_NAME_NAME,
        COLUMN_NAME_DESCRIPTION,
        COLUMN_NAME_PUBLIC_IMAGE_URL,
        COLUMN_NAME_LOCATION_LAT,
        COLUMN_NAME_LOCATION_LON,
        COLUMN_NAME_TAGS,
        COLUMN_NAME_CATEGORY,
        COLUMN_NAME_RELATION_SYSTEM,
        COLUMN_NAME_RELATION_CONTENT,
        COLUMN_NAME_CUSTOM_META
    };
  }

  /**
   * Marker
   */
  public static class MarkerEntry implements BaseColumns {
    public static final String TABLE_NAME = "Marker";
    public static final String COLUMN_NAME_JSON_ID = "json_id";
    public static final String COLUMN_NAME_SPOT_RELATION = "spotRelation";
    public static final String COLUMN_NAME_QR = "qr";
    public static final String COLUMN_NAME_NFC = "nfc";
    public static final String COLUMN_NAME_BEACON_UUID = "beaconUUID";
    public static final String COLUMN_NAME_BEACON_MAJOR = "beaconMajor";
    public static final String COLUMN_NAME_BEACON_MINOR = "beaconMinor";
    public static final String COLUMN_NAME_EDDYSTONE_URL = "eddystoneUrl";

    public static final String CREATE_TABLE =
        "CREATE TABLE " + MarkerEntry.TABLE_NAME + " (" +
            MarkerEntry._ID + " INTEGER PRIMARY KEY," +
            MarkerEntry.COLUMN_NAME_JSON_ID + TEXT_TYPE + UNIQUE + COMMA_SEP +
            MarkerEntry.COLUMN_NAME_SPOT_RELATION + INTEGER_TYPE + COMMA_SEP +
            MarkerEntry.COLUMN_NAME_QR + TEXT_TYPE + COMMA_SEP +
            MarkerEntry.COLUMN_NAME_NFC + TEXT_TYPE + COMMA_SEP +
            MarkerEntry.COLUMN_NAME_BEACON_UUID + TEXT_TYPE + COMMA_SEP +
            MarkerEntry.COLUMN_NAME_BEACON_MAJOR + TEXT_TYPE + COMMA_SEP +
            MarkerEntry.COLUMN_NAME_BEACON_MINOR + TEXT_TYPE + COMMA_SEP +
            MarkerEntry.COLUMN_NAME_EDDYSTONE_URL + TEXT_TYPE + " )";

    public static final String[] PROJECTION = {
        MarkerEntry._ID,
        MarkerEntry.COLUMN_NAME_JSON_ID,
        MarkerEntry.COLUMN_NAME_SPOT_RELATION,
        MarkerEntry.COLUMN_NAME_QR,
        MarkerEntry.COLUMN_NAME_NFC,
        MarkerEntry.COLUMN_NAME_BEACON_UUID,
        MarkerEntry.COLUMN_NAME_BEACON_MAJOR,
        MarkerEntry.COLUMN_NAME_BEACON_MINOR,
        MarkerEntry.COLUMN_NAME_EDDYSTONE_URL
    };
  }

  /**
   * Menu
   */
  public static class MenuEntry implements BaseColumns {
    public static final String TABLE_NAME = "Menu";
    public static final String COLUMN_NAME_JSON_ID = "json_id";
    public static final String COLUMN_NAME_SYSTEM_RELATION = "system";

    public static final String CREATE_TABLE =
        "CREATE TABLE " + MenuEntry.TABLE_NAME + " (" +
            MenuEntry._ID + " INTEGER PRIMARY KEY," +
            MenuEntry.COLUMN_NAME_JSON_ID + TEXT_TYPE + COMMA_SEP +
            MenuEntry.COLUMN_NAME_SYSTEM_RELATION + INTEGER_TYPE + " )";

    public static final String[] PROJECTION = {
        MenuEntry._ID,
        MenuEntry.COLUMN_NAME_JSON_ID,
        MenuEntry.COLUMN_NAME_SYSTEM_RELATION
    };
  }

  /**
   * Content
   */
  public static class ContentEntry implements BaseColumns {
    public static final String TABLE_NAME = "Content";
    public static final String COLUMN_NAME_JSON_ID = "json_id";
    public static final String COLUMN_NAME_SYSTEM_RELATION = "systemRelation";
    public static final String COLUMN_NAME_MENU_RELATION = "menuRelation";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_DESCRIPTION = "description";
    public static final String COLUMN_NAME_LANGUAGE = "language";
    public static final String COLUMN_NAME_CATEGORY = "category";
    public static final String COLUMN_NAME_TAGS = "tags";
    public static final String COLUMN_NAME_PUBLIC_IMAGE_URL = "imageUrl";
    public static final String COLUMN_NAME_CUSTOM_META = "customMeta";
    public static final String COLUMN_NAME_SOCIAL_SHARING_URL = "socialSharingUrl";
    public static final String COLUMN_NAME_FROM_DATE = "fromDate";
    public static final String COLUMN_NAME_TO_DATE = "toDate";
    public static final String COLUMN_NAME_RELATED_SPOT = "relatedSpot";

    public static final String CREATE_TABLE =
        "CREATE TABLE " + ContentEntry.TABLE_NAME + " (" +
            ContentEntry._ID + " INTEGER PRIMARY KEY," +
            ContentEntry.COLUMN_NAME_JSON_ID + TEXT_TYPE + UNIQUE + COMMA_SEP +
            ContentEntry.COLUMN_NAME_SYSTEM_RELATION + INTEGER_TYPE + COMMA_SEP +
            ContentEntry.COLUMN_NAME_MENU_RELATION + INTEGER_TYPE + COMMA_SEP +
            ContentEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
            ContentEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
            ContentEntry.COLUMN_NAME_LANGUAGE + TEXT_TYPE + COMMA_SEP +
            ContentEntry.COLUMN_NAME_CATEGORY + INTEGER_TYPE + COMMA_SEP +
            ContentEntry.COLUMN_NAME_TAGS + TEXT_TYPE + COMMA_SEP +
            ContentEntry.COLUMN_NAME_PUBLIC_IMAGE_URL + TEXT_TYPE + COMMA_SEP +
            ContentEntry.COLUMN_NAME_CUSTOM_META + TEXT_TYPE + COMMA_SEP +
            ContentEntry.COLUMN_NAME_SOCIAL_SHARING_URL + TEXT_TYPE + COMMA_SEP +
            ContentEntry.COLUMN_NAME_FROM_DATE + INTEGER_TYPE + COMMA_SEP +
            ContentEntry.COLUMN_NAME_TO_DATE + INTEGER_TYPE + COMMA_SEP +
            ContentEntry.COLUMN_NAME_RELATED_SPOT + INTEGER_TYPE + " )";

    public static final String[] PROJECTION = {
        ContentEntry._ID,
        ContentEntry.COLUMN_NAME_JSON_ID,
        ContentEntry.COLUMN_NAME_SYSTEM_RELATION,
        ContentEntry.COLUMN_NAME_TITLE,
        ContentEntry.COLUMN_NAME_DESCRIPTION,
        ContentEntry.COLUMN_NAME_LANGUAGE,
        ContentEntry.COLUMN_NAME_CATEGORY,
        ContentEntry.COLUMN_NAME_TAGS,
        ContentEntry.COLUMN_NAME_PUBLIC_IMAGE_URL,
        ContentEntry.COLUMN_NAME_CUSTOM_META,
        ContentEntry.COLUMN_NAME_SOCIAL_SHARING_URL,
        ContentEntry.COLUMN_NAME_FROM_DATE,
        ContentEntry.COLUMN_NAME_TO_DATE,
        ContentEntry.COLUMN_NAME_RELATED_SPOT
    };
  }

  /**
   * ContentBlock
   */
  public static class ContentBlockEntry implements BaseColumns {
    public static final String TABLE_NAME = "ContentBlock";
    public static final String COLUMN_NAME_JSON_ID = "json_id";
    public static final String COLUMN_NAME_CONTENT_RELATION = "contentRelation";
    public static final String COLUMN_NAME_BLOCK_TYPE = "blockType";
    public static final String COLUMN_NAME_PUBLIC_STATUS = "publicStatus";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_TEXT = "text";
    public static final String COLUMN_NAME_ARTISTS = "artists";
    public static final String COLUMN_NAME_FILE_ID = "fileId";
    public static final String COLUMN_NAME_SOUNDCLOUD_URL = "soundcloudUrl";
    public static final String COLUMN_NAME_LINK_TYPE = "linkType";
    public static final String COLUMN_NAME_LINK_URL = "linkUrl";
    public static final String COLUMN_NAME_CONTENT_ID = "contentId";
    public static final String COLUMN_NAME_DOWNLOAD_TYPE = "downloadType";
    public static final String COLUMN_NAME_SPOT_MAP_TAGS = "spotMapTags";
    public static final String COLUMN_NAME_SCALE_X = "scaleX";
    public static final String COLUMN_NAME_VIDEO_URL = "videoUrl";
    public static final String COLUMN_NAME_SHOW_CONTENT_ON_SPOTMAP = "showContentOnSpotmap";
    public static final String COLUMN_NAME_ALT_TEXT = "altText";
    public static final String COLUMN_NAME_COPYRIGHT = "copyright";
    public static final String COLUMN_NAME_CONTENT_LIST_TAGS = "contentListTags";
    public static final String COLUMN_NAME_CONTENT_LIST_PAGE_SIZE = "contentListPageSize";
    public static final String COLUMN_NAME_CONTENT_LIST_SORT_ASC = "contentListSortAsc";

    public static final String CREATE_TABLE =
        "CREATE TABLE " + ContentBlockEntry.TABLE_NAME + " (" +
            ContentBlockEntry._ID + " INTEGER PRIMARY KEY," +
            ContentBlockEntry.COLUMN_NAME_JSON_ID + TEXT_TYPE + UNIQUE + COMMA_SEP +
            ContentBlockEntry.COLUMN_NAME_CONTENT_RELATION + INTEGER_TYPE + COMMA_SEP +
            ContentBlockEntry.COLUMN_NAME_BLOCK_TYPE + TEXT_TYPE + COMMA_SEP +
            ContentBlockEntry.COLUMN_NAME_PUBLIC_STATUS + TEXT_TYPE + COMMA_SEP +
            ContentBlockEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
            ContentBlockEntry.COLUMN_NAME_TEXT + TEXT_TYPE + COMMA_SEP +
            ContentBlockEntry.COLUMN_NAME_ARTISTS + TEXT_TYPE + COMMA_SEP +
            ContentBlockEntry.COLUMN_NAME_FILE_ID + TEXT_TYPE + COMMA_SEP +
            ContentBlockEntry.COLUMN_NAME_SOUNDCLOUD_URL + TEXT_TYPE + COMMA_SEP +
            ContentBlockEntry.COLUMN_NAME_LINK_TYPE + INTEGER_TYPE + COMMA_SEP +
            ContentBlockEntry.COLUMN_NAME_LINK_URL + TEXT_TYPE + COMMA_SEP +
            ContentBlockEntry.COLUMN_NAME_CONTENT_ID + TEXT_TYPE + COMMA_SEP +
            ContentBlockEntry.COLUMN_NAME_DOWNLOAD_TYPE + INTEGER_TYPE + COMMA_SEP +
            ContentBlockEntry.COLUMN_NAME_SPOT_MAP_TAGS + TEXT_TYPE + COMMA_SEP +
            ContentBlockEntry.COLUMN_NAME_SCALE_X + REAL_TYPE + COMMA_SEP +
            ContentBlockEntry.COLUMN_NAME_VIDEO_URL + TEXT_TYPE + COMMA_SEP +
            ContentBlockEntry.COLUMN_NAME_SHOW_CONTENT_ON_SPOTMAP + INTEGER_TYPE + COMMA_SEP +
            ContentBlockEntry.COLUMN_NAME_ALT_TEXT + TEXT_TYPE + COMMA_SEP +
            ContentBlockEntry.COLUMN_NAME_COPYRIGHT + TEXT_TYPE + COMMA_SEP +
            ContentBlockEntry.COLUMN_NAME_CONTENT_LIST_TAGS + TEXT_TYPE + COMMA_SEP +
            ContentBlockEntry.COLUMN_NAME_CONTENT_LIST_PAGE_SIZE + INTEGER_TYPE + COMMA_SEP +
            ContentBlockEntry.COLUMN_NAME_CONTENT_LIST_SORT_ASC + INTEGER_TYPE +
            " )";

    public static final String[] PROJECTION = {
        ContentBlockEntry._ID,
        ContentBlockEntry.COLUMN_NAME_JSON_ID,
        ContentBlockEntry.COLUMN_NAME_BLOCK_TYPE,
        ContentBlockEntry.COLUMN_NAME_PUBLIC_STATUS,
        ContentBlockEntry.COLUMN_NAME_TITLE,
        ContentBlockEntry.COLUMN_NAME_TEXT,
        ContentBlockEntry.COLUMN_NAME_ARTISTS,
        ContentBlockEntry.COLUMN_NAME_FILE_ID,
        ContentBlockEntry.COLUMN_NAME_SOUNDCLOUD_URL,
        ContentBlockEntry.COLUMN_NAME_LINK_TYPE,
        ContentBlockEntry.COLUMN_NAME_LINK_URL,
        ContentBlockEntry.COLUMN_NAME_CONTENT_ID,
        ContentBlockEntry.COLUMN_NAME_DOWNLOAD_TYPE,
        ContentBlockEntry.COLUMN_NAME_SPOT_MAP_TAGS,
        ContentBlockEntry.COLUMN_NAME_SCALE_X,
        ContentBlockEntry.COLUMN_NAME_VIDEO_URL,
        ContentBlockEntry.COLUMN_NAME_SHOW_CONTENT_ON_SPOTMAP,
        ContentBlockEntry.COLUMN_NAME_ALT_TEXT,
        ContentBlockEntry.COLUMN_NAME_COPYRIGHT,
        ContentBlockEntry.COLUMN_NAME_CONTENT_LIST_TAGS,
        ContentBlockEntry.COLUMN_NAME_CONTENT_LIST_PAGE_SIZE,
        ContentBlockEntry.COLUMN_NAME_CONTENT_LIST_SORT_ASC
    };
  }
}
