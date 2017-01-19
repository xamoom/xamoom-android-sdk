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

package com.xamoom.android.xamoomsdk.Offline;

import android.content.Context;
import android.location.Location;
import android.support.annotation.Nullable;

import com.xamoom.android.xamoomsdk.APICallback;
import com.xamoom.android.xamoomsdk.APIListCallback;
import com.xamoom.android.xamoomsdk.Enums.ContentSortFlags;
import com.xamoom.android.xamoomsdk.Enums.SpotFlags;
import com.xamoom.android.xamoomsdk.Enums.SpotSortFlags;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.Menu;
import com.xamoom.android.xamoomsdk.Resource.Spot;
import com.xamoom.android.xamoomsdk.Resource.Style;
import com.xamoom.android.xamoomsdk.Resource.System;
import com.xamoom.android.xamoomsdk.Resource.SystemSetting;
import com.xamoom.android.xamoomsdk.Storage.OfflineStorageManager;

import java.util.EnumSet;
import java.util.List;

import at.rags.morpheus.Error;

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
    String beaconIds = String.format("%d|%d", major, minor);
    getContentByLocationIdentifier(beaconIds, callback);
  }

  public void getContentsByLocation(Location location, int pageSize, @Nullable String cursor,
                                    final EnumSet<ContentSortFlags> sortFlags,
                                    APIListCallback<List<Content>, List<Error>> callback) {
    mOfflineStorageManager.getContentsByLocation(location, pageSize, cursor, sortFlags, callback);
  }

  public void getContentsByTags(List<String> tags, int pageSize, @Nullable String cursor,
                                EnumSet<ContentSortFlags> sortFlags,
                                APIListCallback<List<Content>, List<Error>> callback) {
    mOfflineStorageManager.getContentByTags(tags, pageSize, cursor, sortFlags, callback);
  }

  public void searchContentsByName(String name, int pageSize, @Nullable String cursor,
                                   EnumSet<ContentSortFlags> sortFlags,
                                   APIListCallback<List<Content>, List<Error>> callback) {
    mOfflineStorageManager.searchContentsByName(name, pageSize, cursor, sortFlags, callback);
  }

  public void getSpot(String spotId, APICallback<Spot, List<Error>> callback) {
    Spot spot = mOfflineStorageManager.getSpot(spotId);

    if (callback != null) {
      callback.finished(spot);
    }
  }

  public void getSpotsByLocation(Location location, int radius, EnumSet<SpotFlags> spotFlags,
                                 @Nullable EnumSet<SpotSortFlags> sortFlags,
                                 APIListCallback<List<Spot>, List<Error>> callback) {
    mOfflineStorageManager.getSpotsByLocation(location, radius, 100, null, sortFlags,
        callback);
  }

  public void getSpotsByLocation(Location location, int radius, int pageSize, @Nullable String cursor,
                                 @Nullable EnumSet<SpotFlags> spotFlags,
                                 @Nullable EnumSet<SpotSortFlags> sortFlags,
                                 APIListCallback<List<Spot>, List<Error>> callback) {
    mOfflineStorageManager.getSpotsByLocation(location, radius, pageSize, cursor, sortFlags,
        callback);
  }

  public void getSpotsByTags(List<String> tags, @Nullable EnumSet<SpotFlags> spotFlags,
                             @Nullable EnumSet<SpotSortFlags> sortFlags,
                             APIListCallback<List<Spot>, List<Error>> callback) {
    mOfflineStorageManager.getSpotsByTags(tags, 100, null, sortFlags, callback);
  }

  public void getSpotsByTags(List<String> tags, int pageSize, @Nullable String cursor,
                             @Nullable EnumSet<SpotFlags> spotFlags,
                             @Nullable EnumSet<SpotSortFlags> sortFlags,
                             APIListCallback<List<Spot>, List<Error>> callback) {
    mOfflineStorageManager.getSpotsByTags(tags, pageSize, cursor, sortFlags, callback);
  }

  public void searchSpotsByName(String name, int pageSize, @Nullable String cursor,
                                @Nullable EnumSet<SpotFlags> spotFlags,
                                @Nullable EnumSet<SpotSortFlags> sortFlags,
                                APIListCallback<List<Spot>, List<Error>> callback) {
    mOfflineStorageManager.searchSpotsByName(name, pageSize, cursor, sortFlags, callback);
  }

  public void getSystem(final APICallback<System, List<Error>> callback) {
    System system = mOfflineStorageManager.getSystem();

    if (callback != null) {
      callback.finished(system);
    }
  }

  public void getMenu(String systemId, final APICallback<Menu, List<Error>> callback) {
    Menu menu = mOfflineStorageManager.getMenu(systemId);

    if (callback != null) {
      callback.finished(menu);
    }
  }

  public void getSystemSetting(String systemId, final APICallback<SystemSetting, List<Error>> callback) {
    SystemSetting setting = mOfflineStorageManager.getSystemSetting(systemId);

    if (callback != null) {
      callback.finished(setting);
    }
  }

  public void getStyle(String systemId, final APICallback<Style, List<Error>> callback) {
    Style style = mOfflineStorageManager.getStyle(systemId);

    if (callback != null) {
      callback.finished(style);
    }
  }

  // getter & setter

  public void setOfflineStorageManager(OfflineStorageManager offlineStorageManager) {
    mOfflineStorageManager = offlineStorageManager;
  }
}
