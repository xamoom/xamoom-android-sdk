package com.xamoom.android.xamoomsdk.Offline;


import android.content.Context;
import android.location.Location;
import android.support.annotation.Nullable;

import com.xamoom.android.xamoomsdk.APICallback;
import com.xamoom.android.xamoomsdk.APIListCallback;
import com.xamoom.android.xamoomsdk.Enums.ContentFlags;
import com.xamoom.android.xamoomsdk.Enums.ContentSortFlags;
import com.xamoom.android.xamoomsdk.Enums.SpotFlags;
import com.xamoom.android.xamoomsdk.Enums.SpotSortFlags;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.Marker;
import com.xamoom.android.xamoomsdk.Resource.Menu;
import com.xamoom.android.xamoomsdk.Resource.Spot;
import com.xamoom.android.xamoomsdk.Resource.Style;
import com.xamoom.android.xamoomsdk.Resource.System;
import com.xamoom.android.xamoomsdk.Resource.SystemSetting;
import com.xamoom.android.xamoomsdk.Storage.OfflineStorageManager;
import com.xamoom.android.xamoomsdk.Utils.JsonListUtil;
import com.xamoom.android.xamoomsdk.Utils.UrlUtil;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import at.rags.morpheus.Error;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class OfflineEnduserApi {
  private OfflineStorageManager mOfflineStorageManager;

  public OfflineEnduserApi(Context context) {
    mOfflineStorageManager = OfflineStorageManager.getInstance(context);
  }

  public void getContent(String contentID, APICallback<Content, List<Error>> callback) {
    Content content = mOfflineStorageManager.getContent(contentID);

    if (callback != null) {
      callback.finished(content);
    }
  }

  public void getContentByLocationIdentifier(String locationIdentifier, APICallback<Content,
      List<Error>> callback) {
    Content content = mOfflineStorageManager.getContentWithLocationIdentifier(locationIdentifier);

    if (callback != null) {
      callback.finished(content);
    }
  }

  public void getContentByBeacon(int major, int minor, APICallback<Content, List<Error>>
      callback) {
  }

  public void getContentByBeacon(int major, int minor, EnumSet<ContentFlags> contentFlags,
                                 APICallback<Content, List<Error>> callback) {
  }

  public void getContentsByLocation(Location location, int pageSize, @Nullable String cursor,
                                    final EnumSet<ContentSortFlags> sortFlags,
                                    APIListCallback<List<Content>,
                                                                            List<Error>> callback) {
  }

  public void getContentsByTags(List<String> tags, int pageSize, @Nullable String cursor,
                                EnumSet<ContentSortFlags> sortFlags,
                                APIListCallback<List<Content>, List<Error>> callback) {
  }

  public void searchContentsByName(String name, int pageSize, @Nullable String cursor,
                                   EnumSet<ContentSortFlags> sortFlags,
                                   APIListCallback<List<Content>, List<Error>> callback) {
  }

  public void getSpot(String spotId, APICallback<Spot, List<Error>> callback) {
  }

  public void getSpot(String spotId, EnumSet<SpotFlags> spotFlags, APICallback<Spot, List<Error>> callback) {

  }

  public void getSpotsByLocation(Location location, int radius, EnumSet<SpotFlags> spotFlags,
                                 @Nullable EnumSet<SpotSortFlags> sortFlags,
                                 APIListCallback<List<Spot>, List<Error>> callback) {
  }

  public void getSpotsByLocation(Location location, int radius, int pageSize, @Nullable String cursor,
                                 @Nullable EnumSet<SpotFlags> spotFlags,
                                 @Nullable EnumSet<SpotSortFlags> sortFlags,
                                 APIListCallback<List<Spot>, List<Error>> callback) {

  }

  public void getSpotsByTags(List<String> tags, @Nullable EnumSet<SpotFlags> spotFlags,
                             @Nullable EnumSet<SpotSortFlags> sortFlags,
                             APIListCallback<List<Spot>, List<Error>> callback) {
  }

  public void getSpotsByTags(List<String> tags, int pageSize, @Nullable String cursor,
                             @Nullable EnumSet<SpotFlags> spotFlags,
                             @Nullable EnumSet<SpotSortFlags> sortFlags,
                             APIListCallback<List<Spot>, List<Error>> callback) {

  }

  public void searchSpotsByName(String name, int pageSize, @Nullable String cursor,
                                @Nullable EnumSet<SpotFlags> spotFlags,
                                @Nullable EnumSet<SpotSortFlags> sortFlags,
                                APIListCallback<List<Spot>, List<Error>> callback) {

  }

  public void getSystem(final APICallback<System, List<Error>> callback) {

  }

  public void getMenu(String systemId, final APICallback<Menu, List<Error>> callback) {

  }

  public void getSystemSetting(String systemId, final APICallback<SystemSetting, List<Error>> callback) {

  }

  public void getStyle(String systemId, final APICallback<Style, List<Error>> callback) {

  }

  // getter & setter

  public void setOfflineStorageManager(OfflineStorageManager offlineStorageManager) {
    mOfflineStorageManager = offlineStorageManager;
  }
}
