package com.xamoom.android.xamoomsdk.Storage.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.xamoom.android.xamoomsdk.Resource.Marker;
import com.xamoom.android.xamoomsdk.Storage.TableContracts.OfflineEnduserContract;
import com.xamoom.android.xamoomsdk.Storage.TableContracts.OfflineEnduserContract.MarkerEntry;

import java.util.ArrayList;

public class MarkerDatabaseAdapter extends DatabaseAdapter {
  private static MarkerDatabaseAdapter mSharedInstance;

  public static MarkerDatabaseAdapter getInstance(Context context) {
    if (mSharedInstance == null) {
      mSharedInstance = new MarkerDatabaseAdapter(context);
    }
    return mSharedInstance;
  }

  private MarkerDatabaseAdapter(Context context) {
    super(context);
  }

  public Marker getMarker(String jsonId) {
    String selection = MarkerEntry.COLUMN_NAME_JSON_ID + " = ?";
    String[] selectionArgs = {jsonId};

    return getMarker(selection, selectionArgs);
  }

  public Marker getMarkerWithLocId(String locId) {
    String selection = String.format("%s = ? OR %s = ? OR %s = ? OR %s = ?",
        OfflineEnduserContract.MarkerEntry.COLUMN_NAME_QR,
        OfflineEnduserContract.MarkerEntry.COLUMN_NAME_NFC,
        OfflineEnduserContract.MarkerEntry.COLUMN_NAME_BEACON_MINOR,
        OfflineEnduserContract.MarkerEntry.COLUMN_NAME_EDDYSTONE_URL);
    String[] selectionArgs = {locId};

    return getMarker(selection, selectionArgs);
  }

  public ArrayList<Marker> getRelatedMarkers(long spotId) {
    String selection = OfflineEnduserContract.MarkerEntry.COLUMN_NAME_SPOT_RELATION + " = ?";
    String[] selectionArgs = {String.valueOf(spotId)};

    open();
    Cursor cursor = queryMarker(selection, selectionArgs);
    ArrayList<Marker> markers = cursorToMarkers(cursor);
    close();

    if (markers == null) {
      return null;
    }
    return markers;
  }

  public long getSpotRelation(String locId) {
    String selection = String.format("%s = ? OR %s = ? OR %s = ? OR %s = ?",
        OfflineEnduserContract.MarkerEntry.COLUMN_NAME_QR,
        OfflineEnduserContract.MarkerEntry.COLUMN_NAME_NFC,
        OfflineEnduserContract.MarkerEntry.COLUMN_NAME_BEACON_MINOR,
        OfflineEnduserContract.MarkerEntry.COLUMN_NAME_EDDYSTONE_URL);
    String[] selectionArgs = {locId};

    return getSpotRelation(selection, selectionArgs);
  }

  public long getSpotRelation(String major, String minor) {
    String selection = String.format("%s = ? AND %s = ?",
        MarkerEntry.COLUMN_NAME_BEACON_MAJOR,
        MarkerEntry.COLUMN_NAME_BEACON_MINOR);
    String[] selectionArgs = {major, minor};

    return getSpotRelation(selection, selectionArgs);
  }

  private long getSpotRelation(String selection, String[] selectionArgs) {
    open();
    Cursor cursor = queryMarker(selection, selectionArgs);
    long spotRelation = -1L;

    if (cursor.moveToFirst()) {
      spotRelation = cursor.getLong(cursor.getColumnIndex(MarkerEntry.COLUMN_NAME_SPOT_RELATION));
    }

    close();
    return spotRelation;
  }

  private Marker getMarker(String selection, String[] selectionArgs) {
    open();
    Cursor cursor = queryMarker(selection, selectionArgs);
    ArrayList<Marker> markers = cursorToMarkers(cursor);

    close();
    if (markers == null) {
      return null;
    }

    if (markers.size() > 0) {
      return markers.get(0);
    }
    return null;
  }

  public long insertOrUpdateMarker(Marker marker, long spotId) {
    ContentValues values = new ContentValues();
    values.put(MarkerEntry.COLUMN_NAME_JSON_ID, marker.getId());
    values.put(MarkerEntry.COLUMN_NAME_QR, marker.getQr());
    values.put(MarkerEntry.COLUMN_NAME_NFC, marker.getNfc());
    values.put(MarkerEntry.COLUMN_NAME_BEACON_UUID, marker.getBeaconUUID());
    values.put(MarkerEntry.COLUMN_NAME_BEACON_MAJOR, marker.getBeaconMajor());
    values.put(MarkerEntry.COLUMN_NAME_BEACON_MINOR, marker.getBeaconMinor());
    values.put(MarkerEntry.COLUMN_NAME_EDDYSTONE_URL, marker.getEddystoneUrl());
    values.put(MarkerEntry.COLUMN_NAME_SPOT_RELATION, spotId);

    long row = getPrimaryKey(marker.getId());
    if (row != -1) {
      updateMarker(row, values);
    } else {
      open();
      row = mDatabase.insert(MarkerEntry.TABLE_NAME, null, values);
      close();
    }

    return row;
  }

  public boolean deleteMarker(String jsonId) {
    String selection = OfflineEnduserContract.MarkerEntry.COLUMN_NAME_JSON_ID + " = ?";
    String[] selectionArgs = { jsonId };

    open();
    int rowsAffected = mDatabase.delete(OfflineEnduserContract.MarkerEntry.COLUMN_NAME_JSON_ID, selection,
        selectionArgs);
    close();

    return rowsAffected >= 1;
  }

  private int updateMarker(long row, ContentValues values) {
    String selection = MarkerEntry._ID + " = ?";
    String[] selectionArgs = {String.valueOf(row)};

    open();
    int rowsUpdated = mDatabase.update(
        MarkerEntry.TABLE_NAME,
        values,
        selection,
        selectionArgs);
    close();

    return rowsUpdated;
  }

  private long getPrimaryKey(String jsonId) {
    String selection = MarkerEntry.COLUMN_NAME_JSON_ID + " = ?";
    String[] selectionArgs = {jsonId};

    open();
    Cursor cursor = queryMarker(selection, selectionArgs);
    if (cursor != null) {
      if (cursor.moveToFirst()) {
        long id = cursor.getInt(cursor.getColumnIndex(MarkerEntry._ID));
        close();
        return id;
      }
    }
    close();
    return -1;

  }

  private Cursor queryMarker(String selection, String[] selectionArgs) {
    Cursor cursor = mDatabase.query(MarkerEntry.TABLE_NAME,
        MarkerEntry.PROJECTION, selection, selectionArgs, null,null,null);
    return cursor;
  }

  private ArrayList<Marker> cursorToMarkers(Cursor cursor) {
    ArrayList<Marker> markers = new ArrayList<>();
    while (cursor.moveToNext()) {
      Marker marker = new Marker();
      marker.setId(cursor.getString(cursor.getColumnIndex(MarkerEntry.COLUMN_NAME_JSON_ID)));
      marker.setQr(cursor.getString(cursor.getColumnIndex(MarkerEntry.COLUMN_NAME_QR)));
      marker.setNfc(cursor.getString(cursor.getColumnIndex(MarkerEntry.COLUMN_NAME_NFC)));
      marker.setBeaconUUID(cursor.getString(cursor.getColumnIndex(MarkerEntry.COLUMN_NAME_BEACON_UUID)));
      marker.setBeaconMajor(cursor.getString(cursor.getColumnIndex(MarkerEntry.COLUMN_NAME_BEACON_MAJOR)));
      marker.setBeaconMinor(cursor.getString(cursor.getColumnIndex(MarkerEntry.COLUMN_NAME_BEACON_MINOR)));
      marker.setEddystoneUrl(cursor.getString(cursor.getColumnIndex(MarkerEntry.COLUMN_NAME_EDDYSTONE_URL)));
      markers.add(marker);
    }
    return markers;
  }
}
