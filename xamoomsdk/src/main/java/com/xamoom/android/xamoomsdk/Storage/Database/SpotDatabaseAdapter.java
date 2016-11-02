package com.xamoom.android.xamoomsdk.Storage.Database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.xamoom.android.xamoomsdk.Resource.Location;
import com.xamoom.android.xamoomsdk.Resource.Spot;
import com.xamoom.android.xamoomsdk.Storage.TableContracts.OfflineEnduserContract;

import java.util.Arrays;

public class SpotDatabaseAdapter extends DatabaseAdapter {
  private static SpotDatabaseAdapter mSharedInstance;
  private SystemDatabaseAdapter mSystemDatabaseAdapter;
  private ContentDatabaseAdapter mContentDatabaseAdapter;

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

    open();
    Cursor cursor = querySpot(selection, selectionArgs);

    if (cursor.getCount() > 1) {
      // TODO: too many exception
    }

    Spot spot = cursorToSpot(cursor);

    close();
    return spot;
  }

  public long insertOrUpdateSpot(Spot spot) {
    ContentValues values = new ContentValues();
    values.put(OfflineEnduserContract.SpotEntry.COLUMN_NAME_JSON_ID, spot.getId());
    values.put(OfflineEnduserContract.SpotEntry.COLUMN_NAME_NAME, spot.getName());
    values.put(OfflineEnduserContract.SpotEntry.COLUMN_NAME_DESCRIPTION, spot.getDescription());
    values.put(OfflineEnduserContract.SpotEntry.COLUMN_NAME_PUBLIC_IMAGE_URL, spot.getPublicImageUrl());
    if (spot.getLocation() != null) {
      values.put(OfflineEnduserContract.SpotEntry.COLUMN_NAME_LOCATION_LAT, spot.getLocation().getLatitude());
      values.put(OfflineEnduserContract.SpotEntry.COLUMN_NAME_LOCATION_LON, spot.getLocation().getLongitude());
    }
    if (spot.getTags() != null) {
      values.put(OfflineEnduserContract.SpotEntry.COLUMN_NAME_TAGS,
          TextUtils.join(",", spot.getTags()));
    }
    values.put(OfflineEnduserContract.SpotEntry.COLUMN_NAME_CATEGORY, spot.getCategory());

    if (spot.getSystem() != null) {
      values.put(OfflineEnduserContract.SpotEntry.COLUMN_NAME_RELATION_SYSTEM,
          mSystemDatabaseAdapter.insertOrUpdateSystem(spot.getSystem()));
    }

    if (spot.getContent() != null) {
      values.put(OfflineEnduserContract.SpotEntry.COLUMN_NAME_RELATION_CONTENT,
          mContentDatabaseAdapter.insertOrUpdateContent(spot.getContent(), false, 0));
    }

    long row = getPrimaryKey(spot.getId());
    if (row != -1) {
      updateSpot(row, values);
    } else {
      open();
      row = mDatabase.insert(OfflineEnduserContract.SpotEntry.TABLE_NAME, null, values);
      close();
    }

    return row;
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

  private long getPrimaryKey(String jsonId) {
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

  private Spot cursorToSpot(Cursor cursor) {
    if (cursor.moveToFirst()) {
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

      long systemRow = cursor.getLong(cursor.getColumnIndex(
          OfflineEnduserContract.SpotEntry.COLUMN_NAME_RELATION_SYSTEM));
      if (systemRow != -1) {
        // TODO: set system
      }

      long contentRow = cursor.getLong(cursor.getColumnIndex(
          OfflineEnduserContract.SpotEntry.COLUMN_NAME_RELATION_CONTENT));
      if (contentRow != -1) {
        // TODO: set content
      }
      
      return spot;
    }
    return null;
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

  public void setSystemDatabaseAdapter(SystemDatabaseAdapter systemDatabaseAdapter) {
    mSystemDatabaseAdapter = systemDatabaseAdapter;
  }

  public void setContentDatabaseAdapter(ContentDatabaseAdapter contentDatabaseAdapter) {
    mContentDatabaseAdapter = contentDatabaseAdapter;
  }
}
