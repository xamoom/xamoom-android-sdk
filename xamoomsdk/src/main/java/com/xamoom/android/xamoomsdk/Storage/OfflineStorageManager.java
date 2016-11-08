package com.xamoom.android.xamoomsdk.Storage;

import android.content.Context;

import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.Menu;
import com.xamoom.android.xamoomsdk.Resource.Spot;
import com.xamoom.android.xamoomsdk.Resource.System;
import com.xamoom.android.xamoomsdk.Resource.Style;
import com.xamoom.android.xamoomsdk.Resource.SystemSetting;
import com.xamoom.android.xamoomsdk.Storage.Database.ContentDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.Database.MenuDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.Database.SettingDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.Database.SpotDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.Database.StyleDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.Database.SystemDatabaseAdapter;

import java.net.MalformedURLException;
import java.net.URL;

public class OfflineStorageManager {
  private static OfflineStorageManager mInstance;

  private DownloadManager mDownloadManager;
  private ContentDatabaseAdapter mContentDatabaseAdapter;
  private SpotDatabaseAdapter mSpotDatabaseAdapter;
  private SystemDatabaseAdapter mSystemDatabaseAdapter;
  private StyleDatabaseAdapter mStyleDatabaseAdapter;
  private SettingDatabaseAdapter mSettingDatabaseAdapter;
  private MenuDatabaseAdapter mMenuDatabaseAdapter;

  public static OfflineStorageManager getInstance(Context context) {
    if (mInstance == null) {
      mInstance = new OfflineStorageManager(context);
    }
    return mInstance;
  }

  private OfflineStorageManager(Context context) {
    mDownloadManager = new DownloadManager(FileManager.getInstance(context));
    mContentDatabaseAdapter = ContentDatabaseAdapter.getInstance(context);
    mSpotDatabaseAdapter = SpotDatabaseAdapter.getInstance(context);
    mSystemDatabaseAdapter = SystemDatabaseAdapter.getInstance(context);
    mStyleDatabaseAdapter = StyleDatabaseAdapter.getInstance(context);
    mSettingDatabaseAdapter = SettingDatabaseAdapter.getInstance(context);
    mMenuDatabaseAdapter = MenuDatabaseAdapter.getInstance(context);
  }

  // Saving

  public boolean saveContent(Content content, DownloadManager.OnDownloadManagerCompleted completion)
      throws MalformedURLException {
    long row = mContentDatabaseAdapter.insertOrUpdateContent(content, false, -1);

    if (content.getPublicImageUrl() != null) {
      mDownloadManager.saveFileFromUrl(new URL(content.getPublicImageUrl()),
          false, completion);
    }

    return row != -1;
  }

  public boolean saveSpot(Spot spot, DownloadManager.OnDownloadManagerCompleted completion) throws MalformedURLException {
    long row = mSpotDatabaseAdapter.insertOrUpdateSpot(spot);

    if (spot.getPublicImageUrl() != null) {
      mDownloadManager.saveFileFromUrl(new URL(spot.getPublicImageUrl()),
          false, completion);
    }

    return row != -1;
  }

  public boolean saveSystem(System system) {
    long row = mSystemDatabaseAdapter.insertOrUpdateSystem(system);
    return row != -1;
  }

  public boolean saveStyle(Style style) {
    long row = mStyleDatabaseAdapter.insertOrUpdateStyle(style, -1);
    return row != -1;
  }

  public boolean saveSetting(SystemSetting setting) {
    long row = mSettingDatabaseAdapter.insertOrUpdateSetting(setting, -1);
    return row != -1;
  }

  public boolean saveMenu(Menu menu) {
    long row = mMenuDatabaseAdapter.insertOrUpdate(menu, -1);
    return row != -1;
  }

  // Query

  public Content getContent(String jsonId) {
    return mContentDatabaseAdapter.getContent(jsonId);
  }

  // getter & setter

  public void setContentDatabaseAdapter(ContentDatabaseAdapter contentDatabaseAdapter) {
    mContentDatabaseAdapter = contentDatabaseAdapter;
  }

  public void setDownloadManager(DownloadManager downloadManager) {
    mDownloadManager = downloadManager;
  }

  public void setSpotDatabaseAdapter(SpotDatabaseAdapter spotDatabaseAdapter) {
    mSpotDatabaseAdapter = spotDatabaseAdapter;
  }

  public void setSystemDatabaseAdapter(SystemDatabaseAdapter systemDatabaseAdapter) {
    mSystemDatabaseAdapter = systemDatabaseAdapter;
  }

  public void setStyleDatabaseAdapter(StyleDatabaseAdapter styleDatabaseAdapter) {
    mStyleDatabaseAdapter = styleDatabaseAdapter;
  }

  public void setSettingDatabaseAdapter(SettingDatabaseAdapter settingDatabaseAdapter) {
    mSettingDatabaseAdapter = settingDatabaseAdapter;
  }

  public void setMenuDatabaseAdapter(MenuDatabaseAdapter menuDatabaseAdapter) {
    mMenuDatabaseAdapter = menuDatabaseAdapter;
  }
}
