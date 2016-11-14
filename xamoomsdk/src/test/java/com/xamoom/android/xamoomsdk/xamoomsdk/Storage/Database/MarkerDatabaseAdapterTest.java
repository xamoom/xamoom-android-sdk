package com.xamoom.android.xamoomsdk.xamoomsdk.Storage.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.Resource.Marker;
import com.xamoom.android.xamoomsdk.Storage.Database.DatabaseHelper;
import com.xamoom.android.xamoomsdk.Storage.Database.MarkerDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.TableContracts.OfflineEnduserContract;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.matchers.MatchersPrinter;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class MarkerDatabaseAdapterTest {
  private MarkerDatabaseAdapter mMarkerDatabaseAdapter;
  private DatabaseHelper mMockedDatabaseHelper;
  private SQLiteDatabase mMockedDatabase;
  private Cursor mMockedCursor;

  @Before
  public void setup() {
    mMarkerDatabaseAdapter =
        MarkerDatabaseAdapter.getInstance(RuntimeEnvironment.application);
    mMockedDatabaseHelper = mock(DatabaseHelper.class);
    mMockedDatabase = mock(SQLiteDatabase.class);
    mMarkerDatabaseAdapter.setDatabaseHelper(mMockedDatabaseHelper);
    mMockedCursor = mock(Cursor.class);

    Mockito.stub(mMockedDatabaseHelper.getWritableDatabase())
        .toReturn(mMockedDatabase);
    Mockito.stub(mMockedDatabaseHelper.getReadableDatabase())
        .toReturn(mMockedDatabase);
    Mockito.stub(mMockedDatabase.query(anyString(), any(String[].class), anyString(),
        any(String[].class), anyString(), anyString(), anyString())).toReturn(mMockedCursor);
  }

  @Test
  public void testGetMarker() {
    Mockito.stub(mMockedCursor.getCount()).toReturn(1);
    Mockito.stub(mMockedCursor.moveToNext()).toReturn(true).toReturn(false);

    Marker marker = mMarkerDatabaseAdapter.getMarker("1");

    Mockito.verify(mMockedDatabase).query(
        eq(OfflineEnduserContract.MarkerEntry.TABLE_NAME),
        any(String[].class), anyString(), any(String[].class), anyString(),
        anyString(), anyString());

    Assert.assertNotNull(marker);
  }

  @Test
  public void testGetMarkerWithLocId() {
    String query = String.format("%s = ? OR %s = ? OR %s = ? OR %s = ?",
        OfflineEnduserContract.MarkerEntry.COLUMN_NAME_QR,
        OfflineEnduserContract.MarkerEntry.COLUMN_NAME_NFC,
        OfflineEnduserContract.MarkerEntry.COLUMN_NAME_BEACON_MINOR,
        OfflineEnduserContract.MarkerEntry.COLUMN_NAME_EDDYSTONE_URL);

    Mockito.stub(mMockedCursor.getCount()).toReturn(1);
    Mockito.stub(mMockedCursor.moveToNext()).toReturn(true).toReturn(false);

    Marker marker = mMarkerDatabaseAdapter.getMarkerWithLocId("1");

    Mockito.verify(mMockedDatabase).query(
        eq(OfflineEnduserContract.MarkerEntry.TABLE_NAME),
        any(String[].class), eq(query), any(String[].class), anyString(),
        anyString(), anyString());

    Assert.assertNotNull(marker);
  }

  @Test
  public void testInsertOrUpdateNewEntity() {
    Marker marker = new Marker();
    marker.setId("1");

    long row = mMarkerDatabaseAdapter.insertOrUpdateMarker(marker, 0);

    Mockito.verify(mMockedDatabase).query(
        eq(OfflineEnduserContract.MarkerEntry.TABLE_NAME),
        any(String[].class), anyString(), any(String[].class), anyString(),
        anyString(), anyString());

    Mockito.verify(mMockedDatabase).insert(
        eq(OfflineEnduserContract.MarkerEntry.TABLE_NAME),
        anyString(), any(ContentValues.class));
  }

  @Test
  public void testInsertOrUpdateExistingEntity() {
    Marker marker = new Marker();
    marker.setId("1");

    Mockito.stub(mMockedDatabase.query(anyString(), any(String[].class), anyString(),
        any(String[].class), anyString(), anyString(), anyString())).toReturn(mMockedCursor);
    Mockito.stub(mMockedCursor.moveToFirst()).toReturn(true);
    Mockito.stub(mMockedCursor.getInt(anyInt())).toReturn(1);

    long row = mMarkerDatabaseAdapter.insertOrUpdateMarker(marker, 0);

    Mockito.verify(mMockedDatabase).query(
        eq(OfflineEnduserContract.MarkerEntry.TABLE_NAME),
        any(String[].class), anyString(), any(String[].class), anyString(),
        anyString(), anyString());

    Mockito.verify(mMockedDatabase).update(
        eq(OfflineEnduserContract.MarkerEntry.TABLE_NAME),
        any(ContentValues.class), anyString(), any(String[].class));
    Assert.assertNotEquals(row, -1L);
  }

  @Test
  public void testGetRelatedMarkers() {
    String query = OfflineEnduserContract.MarkerEntry.COLUMN_NAME_SPOT_RELATION + " = ?";

    Mockito.stub(mMockedCursor.getCount()).toReturn(1);
    Mockito.stub(mMockedCursor.moveToNext()).toReturn(true).toReturn(false);

    ArrayList<Marker> markers = mMarkerDatabaseAdapter.getRelatedMarkers(1);

    Mockito.verify(mMockedDatabase).query(
        eq(OfflineEnduserContract.MarkerEntry.TABLE_NAME),
        any(String[].class), eq(query), any(String[].class), anyString(),
        anyString(), anyString());

    Assert.assertNotNull(markers);
  }

  @Test
  public void testGetSpotRelation() {
    Mockito.stub(mMockedCursor.getCount()).toReturn(1);
    Mockito.stub(mMockedCursor.moveToNext()).toReturn(true).toReturn(false);

    long spotRelation = mMarkerDatabaseAdapter.getSpotRelation("locId");

    Mockito.verify(mMockedDatabase).query(
        eq(OfflineEnduserContract.MarkerEntry.TABLE_NAME),
        any(String[].class), anyString(), any(String[].class), anyString(),
        anyString(), anyString());
  }

  @Test
  public void testGetSpotRelationBeacon() {
    String query = String.format("%s = ? AND %s = ?",
        OfflineEnduserContract.MarkerEntry.COLUMN_NAME_BEACON_MAJOR,
        OfflineEnduserContract.MarkerEntry.COLUMN_NAME_BEACON_MINOR);

    Mockito.stub(mMockedCursor.getCount()).toReturn(1);
    Mockito.stub(mMockedCursor.moveToNext()).toReturn(true).toReturn(false);

    long spotRelation = mMarkerDatabaseAdapter.getSpotRelation("major", "minor");

    Mockito.verify(mMockedDatabase).query(
        eq(OfflineEnduserContract.MarkerEntry.TABLE_NAME),
        any(String[].class), eq(query), any(String[].class), anyString(),
        anyString(), anyString());
  }

  @Test
  public void testDelete() {
    Mockito.stub(mMockedDatabase.delete(anyString(), anyString(), any(String[].class))).toReturn(1);

    boolean deleted = mMarkerDatabaseAdapter.deleteMarker("1");

    junit.framework.Assert.assertTrue(deleted);
  }

  @Test
  public void testDeleteContent() {
    Mockito.stub(mMockedDatabase.delete(anyString(), anyString(), any(String[].class))).toReturn(0);

    boolean deleted = mMarkerDatabaseAdapter.deleteMarker("1");

    junit.framework.Assert.assertFalse(deleted);
  }
}
