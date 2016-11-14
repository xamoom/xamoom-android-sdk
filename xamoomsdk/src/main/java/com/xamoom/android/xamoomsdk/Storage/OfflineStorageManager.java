package com.xamoom.android.xamoomsdk.Storage;

import android.content.Context;
import android.location.Location;
import android.support.annotation.Nullable;

import com.xamoom.android.xamoomsdk.APICallback;
import com.xamoom.android.xamoomsdk.APIListCallback;
import com.xamoom.android.xamoomsdk.Enums.ContentSortFlags;
import com.xamoom.android.xamoomsdk.Enums.SpotFlags;
import com.xamoom.android.xamoomsdk.Enums.SpotSortFlags;
import com.xamoom.android.xamoomsdk.Offline.OfflineEnduserApi;
import com.xamoom.android.xamoomsdk.Offline.OfflineEnduserApiHelper;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Menu;
import com.xamoom.android.xamoomsdk.Resource.Spot;
import com.xamoom.android.xamoomsdk.Resource.System;
import com.xamoom.android.xamoomsdk.Resource.Style;
import com.xamoom.android.xamoomsdk.Resource.SystemSetting;
import com.xamoom.android.xamoomsdk.Storage.Database.ContentBlockDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.Database.ContentDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.Database.MarkerDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.Database.MenuDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.Database.SettingDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.Database.SpotDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.Database.StyleDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.Database.SystemDatabaseAdapter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import at.rags.morpheus.Error;

public class OfflineStorageManager {
  private static OfflineStorageManager mInstance;

  private DownloadManager mDownloadManager;
  private FileManager mFileManager;
  private ContentDatabaseAdapter mContentDatabaseAdapter;
  private ContentBlockDatabaseAdapter mContentBlockDatabaseAdapter;
  private SpotDatabaseAdapter mSpotDatabaseAdapter;
  private SystemDatabaseAdapter mSystemDatabaseAdapter;
  private StyleDatabaseAdapter mStyleDatabaseAdapter;
  private SettingDatabaseAdapter mSettingDatabaseAdapter;
  private MenuDatabaseAdapter mMenuDatabaseAdapter;
  private MarkerDatabaseAdapter mMarkerDatabaseAdapter;

  private ArrayList<String> mSaveDeletionFiles = new ArrayList<>();

  public static OfflineStorageManager getInstance(Context context) {
    if (mInstance == null) {
      mInstance = new OfflineStorageManager(context);
    }
    return mInstance;
  }

  private OfflineStorageManager(Context context) {
    mFileManager = FileManager.getInstance(context);
    mDownloadManager = new DownloadManager(mFileManager);
    mContentDatabaseAdapter = ContentDatabaseAdapter.getInstance(context);
    mContentBlockDatabaseAdapter = ContentBlockDatabaseAdapter.getInstance(context);
    mSpotDatabaseAdapter = SpotDatabaseAdapter.getInstance(context);
    mSystemDatabaseAdapter = SystemDatabaseAdapter.getInstance(context);
    mStyleDatabaseAdapter = StyleDatabaseAdapter.getInstance(context);
    mSettingDatabaseAdapter = SettingDatabaseAdapter.getInstance(context);
    mMenuDatabaseAdapter = MenuDatabaseAdapter.getInstance(context);
    mMarkerDatabaseAdapter = MarkerDatabaseAdapter.getInstance(context);
  }

  // Saving

  public boolean saveContent(Content content, DownloadManager.OnDownloadManagerCompleted completion)
      throws MalformedURLException {
    long row = mContentDatabaseAdapter.insertOrUpdateContent(content, false, -1);

    if (content.getPublicImageUrl() != null) {
      mDownloadManager.saveFileFromUrl(new URL(content.getPublicImageUrl()),
          false, completion);
    }

    if (content.getContentBlocks() != null) {
      for (ContentBlock contentBlock : content.getContentBlocks()) {
        if (contentBlock.getFileId() != null) {
          mDownloadManager.saveFileFromUrl(new URL(contentBlock.getFileId()),
              false, completion);
        }

        if (contentBlock.getVideoUrl() != null) {
          if (!contentBlock.getVideoUrl().contains("youtube.com") ||
              !contentBlock.getVideoUrl().contains("youtu.be") ||
              !contentBlock.getVideoUrl().contains("vimeo.com")) {
            mDownloadManager.saveFileFromUrl(new URL(contentBlock.getVideoUrl()),
                false, completion);
          }
        }
      }

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

  public Content getContentWithLocationIdentifier(String locId) {
    long spotId = -1;
    if (locId.contains("|")){
      String[] beaconIds = locId.split("|");
      spotId = mMarkerDatabaseAdapter.getSpotRelation(beaconIds[0], beaconIds[1]);
    } else {
      spotId = mMarkerDatabaseAdapter.getSpotRelation(locId);
    }

    Spot spot = null;
    if (spotId != -1) {
      spot = mSpotDatabaseAdapter.getSpot(spotId);
    }

    if (spot != null && spot.getContent() != null) {
      return spot.getContent();
    }

    return null;
  }

  public void getContentsByLocation(Location location, int pageSize, String cursor,
                                       EnumSet<ContentSortFlags> sortFlags,
                                       APIListCallback<List<Content>, List<Error>> callback) {
    ArrayList<Spot> allSpots = mSpotDatabaseAdapter.getAllSpots();
    allSpots = OfflineEnduserApiHelper.getSpotsInGeofence(location, allSpots);

    ArrayList<Content> contents = new ArrayList<>();
    for (Spot spot : allSpots) {
      if (spot.getContent() != null) {
        contents.add(spot.getContent());
      }
    }

    sortContents(contents, sortFlags);

    OfflineEnduserApiHelper.PagedResult<Content> contentPagedResult =
        OfflineEnduserApiHelper.pageResults(contents, pageSize, cursor);

    if (callback != null) {
      callback.finished(contentPagedResult.getObjects(), contentPagedResult.getCursor(),
          contentPagedResult.hasMore());
    }
  }

  public void getContentByTags(List<String> tags, int pageSize, String cursor,
                               EnumSet<ContentSortFlags> sortFlags,
                               APIListCallback<List<Content>, List<Error>> callback) {

    ArrayList<Content> contents = mContentDatabaseAdapter.getAllContents();
    contents = OfflineEnduserApiHelper.getContentsWithTags(tags, contents);
    OfflineEnduserApiHelper.PagedResult<Content> contentPagedResult =
        OfflineEnduserApiHelper.pageResults(contents, pageSize, cursor);

    sortContents(contents, sortFlags);

    if (callback != null) {
      callback.finished(contentPagedResult.getObjects(), contentPagedResult.getCursor(),
          contentPagedResult.hasMore());
    }
  }

  public void searchContentsByName(String name, int pageSize, @Nullable String cursor,
                                   EnumSet<ContentSortFlags> sortFlags,
                                   APIListCallback<List<Content>, List<Error>> callback) {
    ArrayList<Content> contents = mContentDatabaseAdapter.getContents(name);

    sortContents(contents, sortFlags);

    OfflineEnduserApiHelper.PagedResult<Content> contentPagedResult =
        OfflineEnduserApiHelper.pageResults(contents, pageSize, cursor);

    if (callback != null) {
      callback.finished(contentPagedResult.getObjects(), contentPagedResult.getCursor(),
          contentPagedResult.hasMore());
    }
  }

  public Spot getSpot(String spotId) {
    return mSpotDatabaseAdapter.getSpot(spotId);
  }

  public void getSpotsByLocation(Location location, int radius, int pageSize, @Nullable String cursor,
                                 @Nullable EnumSet<SpotFlags> spotFlags,
                                 @Nullable EnumSet<SpotSortFlags> sortFlags,
                                 APIListCallback<List<Spot>, List<Error>> callback) {

    ArrayList<Spot> allSpots = mSpotDatabaseAdapter.getAllSpots();
    allSpots = OfflineEnduserApiHelper.getSpotsInRadius(location, radius, allSpots);

    allSpots = sortSpots(allSpots, sortFlags, location);

    OfflineEnduserApiHelper.PagedResult<Spot> spotPagedResult =
        OfflineEnduserApiHelper.pageResults(allSpots, pageSize, cursor);

    if (callback != null) {
      callback.finished(spotPagedResult.getObjects(), spotPagedResult.getCursor(),
          spotPagedResult.hasMore());
    }
  }

  public void getSpotsByTags(List<String> tags, int pageSize, @Nullable String cursor,
                             @Nullable EnumSet<SpotFlags> spotFlags,
                             @Nullable EnumSet<SpotSortFlags> sortFlags,
                             APIListCallback<List<Spot>, List<Error>> callback) {
    ArrayList<Spot> allSpots = mSpotDatabaseAdapter.getAllSpots();
    allSpots = OfflineEnduserApiHelper.getSpotsWithTags(tags, allSpots);

    allSpots = sortSpots(allSpots, sortFlags, null);

    OfflineEnduserApiHelper.PagedResult<Spot> spotPagedResult =
        OfflineEnduserApiHelper.pageResults(allSpots, pageSize, cursor);

    if (callback != null) {
      callback.finished(spotPagedResult.getObjects(), spotPagedResult.getCursor(),
          spotPagedResult.hasMore());
    }
  }

  public void searchSpotsByName(String name, int pageSize, @Nullable String cursor,
                                @Nullable EnumSet<SpotFlags> spotFlags,
                                @Nullable EnumSet<SpotSortFlags> sortFlags,
                                APIListCallback<List<Spot>, List<Error>> callback) {
    ArrayList<Spot> spots = mSpotDatabaseAdapter.getSpots(name);

    spots = sortSpots(spots, sortFlags, null);

    OfflineEnduserApiHelper.PagedResult<Spot> spotPagedResult =
        OfflineEnduserApiHelper.pageResults(spots, pageSize, cursor);

    if (callback != null) {
      callback.finished(spotPagedResult.getObjects(), spotPagedResult.getCursor(),
          spotPagedResult.hasMore());
    }
  }

  public System getSystem() {
    return mSystemDatabaseAdapter.getSystem();
  }

  public Menu getMenu(String jsonId) {
    return mMenuDatabaseAdapter.getMenu(jsonId);
  }

  public SystemSetting getSystemSetting(String jsonId) {
    return mSettingDatabaseAdapter.getSystemSetting(jsonId);
  }

  public Style getStyle(String jsonId) {
    return mStyleDatabaseAdapter.getStyle(jsonId);
  }

  /**
   * Delete file from phone storage.
   *
   * @param url Url of the file.
   * @param saveDeletion True to save and delete a patch with check if the file is somewhere else.
   * @return true when deleted. False when not deleted or when saveDeletion is true.
   * @throws IOException
   */
  public boolean deleteFile(String url, boolean saveDeletion) throws IOException {
    if (saveDeletion) {
      mSaveDeletionFiles.add(url);
      return false;
    }

    return mFileManager.deleteFile(url);
  }

  public void deleteFilesWithSafetyCheck() throws IOException {
    for (String url : mSaveDeletionFiles) {
      if (mContentBlockDatabaseAdapter.getContentBlocksWithFile(url).size() > 0) {
        continue;
      }

      if (mContentDatabaseAdapter.getContentsWithFileId(url).size() > 0) {
        continue;
      }

      if (mSpotDatabaseAdapter.getSpotsWithImage(url).size() > 0) {
        continue;
      }

      mFileManager.deleteFile(url);
    }

    mSaveDeletionFiles.clear();
  }

  // private helpers

  private ArrayList<Content> sortContents(ArrayList<Content> contents,
                                          EnumSet<ContentSortFlags> sortFlags) {
    if (sortFlags != null) {
      // sortFlags.contains(ContentSortFlags.NAME) true if ASC, false if desc
      contents = OfflineEnduserApiHelper.sortContentsByTitle(contents,
          sortFlags.contains(ContentSortFlags.NAME));
    }

    return contents;
  }

  private ArrayList<Spot> sortSpots(ArrayList<Spot> spots, EnumSet<SpotSortFlags> sortFlags,
                                    Location location) {
    if (sortFlags != null) {
      if ((sortFlags.contains(SpotSortFlags.DISTANCE) ||
          sortFlags.contains(SpotSortFlags.DISTANCE_DESC)) &&
          location != null) {
        spots = OfflineEnduserApiHelper.sortSpotsByDistance(spots, location,
            sortFlags.contains(SpotSortFlags.DISTANCE));
      } else if (sortFlags.contains(SpotSortFlags.NAME) ||
          sortFlags.contains(SpotSortFlags.NAME_DESC)) {
        spots = OfflineEnduserApiHelper.sortSpotsByName(spots,
            sortFlags.contains(SpotSortFlags.NAME));
      }
    }

    return spots;
  }

  // getter & setter

  public void setContentDatabaseAdapter(ContentDatabaseAdapter contentDatabaseAdapter) {
    mContentDatabaseAdapter = contentDatabaseAdapter;
  }

  public void setContentBlockDatabaseAdapter(ContentBlockDatabaseAdapter contentBlockDatabaseAdapter) {
    mContentBlockDatabaseAdapter = contentBlockDatabaseAdapter;
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

  public void setMarkerDatabaseAdapter(MarkerDatabaseAdapter markerDatabaseAdapter) {
    mMarkerDatabaseAdapter = markerDatabaseAdapter;
  }

  public void setFileManager(FileManager fileManager) {
    mFileManager = fileManager;
  }

  public ArrayList<String> getSaveDeletionFiles() {
    return mSaveDeletionFiles;
  }
}
