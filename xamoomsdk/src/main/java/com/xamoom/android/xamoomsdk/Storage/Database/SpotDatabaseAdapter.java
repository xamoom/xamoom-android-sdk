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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.xamoom.android.xamoomsdk.Resource.Location;
import com.xamoom.android.xamoomsdk.Resource.Marker;
import com.xamoom.android.xamoomsdk.Resource.Spot;
import com.xamoom.android.xamoomsdk.Storage.TableContracts.OfflineEnduserContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

public class SpotDatabaseAdapter extends DatabaseAdapter {
  private static SpotDatabaseAdapter mSharedInstance;
  private SystemDatabaseAdapter mSystemDatabaseAdapter;
  private ContentDatabaseAdapter mContentDatabaseAdapter;
  private MarkerDatabaseAdapter mMarkerDatabaseAdapter;

  public static SpotDatabaseAdapter getInstance(Context context) {
    if (mSharedInstance == null) {
      mSharedInstance = new SpotDatabaseAdapter(context);
    }
    return mSharedInstance;
  }

  private SpotDatabaseAdapter(Context context) {
    super(context);
  }

  public Spot getSpot(String jsonId) {
    String selection = OfflineEnduserContract.SpotEntry.COLUMN_NAME_JSON_ID + " = ?";
    String[] selectionArgs = {jsonId};

    return getSpot(selection, selectionArgs);
  }

  public Spot getSpot(long spotId) {
    String selection = OfflineEnduserContract.SpotEntry._ID + " = ?";
    String[] selectionArgs = {String.valueOf(spotId)};

    return getSpot(selection, selectionArgs);
  }

  public ArrayList<Spot> getSpots(String name) {
    String selection = "LOWER(" + OfflineEnduserContract.SpotEntry.COLUMN_NAME_NAME + ") LIKE LOWER(?)";
    String[] selectionArgs = {"%"+name+"%"};

    open();
    Cursor cursor = querySpot(selection, selectionArgs);

    ArrayList<Spot> spots = cursorToSpots(cursor);
    close();

    return spots;
  }

  private Spot getSpot(String selection, String[] selectionArgs) {
    open();
    Cursor cursor = querySpot(selection, selectionArgs);
    ArrayList<Spot> spots = cursorToSpots(cursor);
    close();

    if (spots.size() > 0) {
      return spots.get(0);
    }
    return null;
  }

  public ArrayList<Spot> getAllSpots() {
    open();
    Cursor cursor = querySpot(null, null);

    ArrayList<Spot> spots = cursorToSpots(cursor);
    close();

    return spots;
  }

  public ArrayList<Spot> getSpotsWithImage(String imageUrl) {
    String selection = OfflineEnduserContract.SpotEntry.COLUMN_NAME_PUBLIC_IMAGE_URL + " = ?";
    String[] selectionArgs = { imageUrl };

    open();
    Cursor cursor = querySpot(selection, selectionArgs);

    ArrayList<Spot> spots = cursorToSpots(cursor);
    close();

    return spots;
  }

  public long insertOrUpdateSpot(Spot spot) {
    ContentValues values = new ContentValues();
    values.put(OfflineEnduserContract.SpotEntry.COLUMN_NAME_JSON_ID, spot.getId());
    values.put(OfflineEnduserContract.SpotEntry.COLUMN_NAME_NAME, spot.getName());
    values.put(OfflineEnduserContract.SpotEntry.COLUMN_NAME_DESCRIPTION, spot.getDescription());
    values.put(OfflineEnduserContract.SpotEntry.COLUMN_NAME_PUBLIC_IMAGE_URL, spot.getPublicImageUrl());
    values.put(OfflineEnduserContract.SpotEntry.COLUMN_NAME_CATEGORY, spot.getCategory());

    if (spot.getLocation() != null) {
      values.put(OfflineEnduserContract.SpotEntry.COLUMN_NAME_LOCATION_LAT, spot.getLocation().getLatitude());
      values.put(OfflineEnduserContract.SpotEntry.COLUMN_NAME_LOCATION_LON, spot.getLocation().getLongitude());
    }

    if (spot.getTags() != null) {
      values.put(OfflineEnduserContract.SpotEntry.COLUMN_NAME_TAGS,
          TextUtils.join(",", spot.getTags()));
    }

    if (spot.getCustomMeta() != null) {
      String json = new JSONObject(spot.getCustomMeta()).toString();
      values.put(OfflineEnduserContract.SpotEntry.COLUMN_NAME_CUSTOM_META,
          json);
    }

    if (spot.getSystem() != null) {
      values.put(OfflineEnduserContract.SpotEntry.COLUMN_NAME_RELATION_SYSTEM,
          getSystemDatabaseAdapter().insertOrUpdateSystem(spot.getSystem()));
    }

    if (spot.getContent() != null) {
      values.put(OfflineEnduserContract.SpotEntry.COLUMN_NAME_RELATION_CONTENT,
          getContentDatabaseAdapter().insertOrUpdateContent(spot.getContent(), false, 0));
    }

    long row = getPrimaryKey(spot.getId());
    if (row != -1) {
      updateSpot(row, values);
    } else {
      open();
      row = mDatabase.insert(OfflineEnduserContract.SpotEntry.TABLE_NAME, null, values);
      close();
    }

    if (spot.getMarkers() != null) {
      for (Marker marker : spot.getMarkers()) {
        getMarkerDatabaseAdapter().insertOrUpdateMarker(marker, row);
      }
    }

    return row;
  }

  public boolean deleteSpot(String jsonId) {
    String selection = OfflineEnduserContract.SpotEntry.COLUMN_NAME_JSON_ID + " = ?";
    String[] selectionArgs = { jsonId };

    open();
    int rowsAffected = mDatabase.delete(OfflineEnduserContract.SpotEntry.COLUMN_NAME_JSON_ID, selection,
        selectionArgs);
    close();

    return rowsAffected >= 1;
  }

  private long updateSpot(long row, ContentValues values) {
    String selection = OfflineEnduserContract.SpotEntry._ID + " = ?";
    String[] selectionArgs = {String.valueOf(row)};

    open();
    long updatedRow = mDatabase.update(OfflineEnduserContract.SpotEntry.TABLE_NAME,
        values, selection, selectionArgs);
    close();

    return updatedRow;
  }

  public long getPrimaryKey(String jsonId) {
    String selection = OfflineEnduserContract.SpotEntry.COLUMN_NAME_JSON_ID + " = ?";
    String[] selectionArgs = {jsonId};

    open();
    Cursor cursor = querySpot(selection, selectionArgs);
    if (cursor != null) {
      if (cursor.moveToFirst()) {
        int id = cursor.getInt(cursor.getColumnIndex(OfflineEnduserContract.SpotEntry._ID));
        close();
        return id;
      }
    }
    close();
    return -1;
  }

  public SpotDatabaseAdapter(Context context, SystemDatabaseAdapter systemDatabaseAdapter) {
    super(context);
    mSystemDatabaseAdapter = systemDatabaseAdapter;
  }

  private ArrayList<Spot> cursorToSpots(Cursor cursor) {
    ArrayList<Spot> spots = new ArrayList<>();
    while (cursor.moveToNext()) {
      Spot spot = new Spot();
      spot.setId(cursor.getString(cursor.getColumnIndex(
          OfflineEnduserContract.SpotEntry.COLUMN_NAME_JSON_ID)));
      spot.setDescription(cursor.getColumnName(cursor.getColumnIndex(
          OfflineEnduserContract.SpotEntry.COLUMN_NAME_DESCRIPTION)));
      spot.setName(cursor.getString(cursor.getColumnIndex(
          OfflineEnduserContract.SpotEntry.COLUMN_NAME_NAME)));
      spot.setPublicImageUrl(cursor.getString(cursor.getColumnIndex(
          OfflineEnduserContract.SpotEntry.COLUMN_NAME_PUBLIC_IMAGE_URL)));
      double lat = cursor.getDouble(cursor.getColumnIndex(
          OfflineEnduserContract.SpotEntry.COLUMN_NAME_LOCATION_LAT));
      double lon = cursor.getDouble(cursor.getColumnIndex(
          OfflineEnduserContract.SpotEntry.COLUMN_NAME_LOCATION_LON));
      spot.setLocation(new Location(lat, lon));
      String tags = cursor.getString(cursor
          .getColumnIndex(OfflineEnduserContract.SpotEntry.COLUMN_NAME_TAGS));
      if (tags != null) {
        spot.setTags(Arrays.asList(tags.split(",")));
      }
      spot.setCategory(cursor.getInt(cursor.getColumnIndex(
          OfflineEnduserContract.SpotEntry.COLUMN_NAME_CATEGORY)));

      String customMetaJson = cursor.getString(cursor.getColumnIndex(
          OfflineEnduserContract.SpotEntry.COLUMN_NAME_CUSTOM_META));
      if (customMetaJson != null) {
        try {
          JSONObject jsonData = new JSONObject(customMetaJson);
          HashMap<String, String> outMap = new HashMap<String, String>();
          Iterator<String> iter = jsonData.keys();
          while (iter.hasNext()) {
            String name = iter.next();
            outMap.put(name, jsonData.getString(name));
          }
          spot.setCustomMeta(outMap);
        } catch (JSONException e) {
          // customMeta will be null
          Log.e(SpotDatabaseAdapter.class.getSimpleName(),
              "Cannot parse customMetaJson from sqlite. Error: " + e.toString());
        }
      }

      long systemRow = cursor.getLong(cursor.getColumnIndex(
          OfflineEnduserContract.SpotEntry.COLUMN_NAME_RELATION_SYSTEM));
      if (systemRow != -1) {
        spot.setSystem(getSystemDatabaseAdapter().getSystem(systemRow));
      }

      long contentRow = cursor.getLong(cursor.getColumnIndex(
          OfflineEnduserContract.SpotEntry.COLUMN_NAME_RELATION_CONTENT));
      if (contentRow != -1) {
        spot.setContent(getContentDatabaseAdapter().getContent(contentRow));
      }

      long spotId = cursor.getLong(cursor.getColumnIndex(
          OfflineEnduserContract.SpotEntry._ID));
      spot.setMarkers(getMarkerDatabaseAdapter().getRelatedMarkers(spotId));

      spots.add(spot);
    }
    return spots;
  }

  private Cursor querySpot(String selection, String[] selectionArgs) {
    Cursor cursor = mDatabase.query(OfflineEnduserContract.SpotEntry.TABLE_NAME,
        OfflineEnduserContract.SpotEntry.PROJECTION, selection,
        selectionArgs, null, null, null);
    return cursor;
  }

  // setter

  public ContentDatabaseAdapter getContentDatabaseAdapter() {
    if (mContentDatabaseAdapter == null) {
      mContentDatabaseAdapter = ContentDatabaseAdapter.getInstance(mContext);
    }
    return mContentDatabaseAdapter;
  }

  public SystemDatabaseAdapter getSystemDatabaseAdapter() {
    if (mSystemDatabaseAdapter == null) {
      mSystemDatabaseAdapter = SystemDatabaseAdapter.getInstance(mContext);
    }
    return mSystemDatabaseAdapter;
  }

  public MarkerDatabaseAdapter getMarkerDatabaseAdapter() {
    if (mMarkerDatabaseAdapter == null) {
      mMarkerDatabaseAdapter = MarkerDatabaseAdapter.getInstance(mContext);
    }
    return mMarkerDatabaseAdapter;
  }

  public void setSystemDatabaseAdapter(SystemDatabaseAdapter systemDatabaseAdapter) {
    mSystemDatabaseAdapter = systemDatabaseAdapter;
  }

  public void setContentDatabaseAdapter(ContentDatabaseAdapter contentDatabaseAdapter) {
    mContentDatabaseAdapter = contentDatabaseAdapter;
  }

  public void setMarkerDatabaseAdapter(MarkerDatabaseAdapter markerDatabaseAdapter) {
    mMarkerDatabaseAdapter = markerDatabaseAdapter;
  }
}
