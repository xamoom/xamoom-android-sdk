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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
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
        Mockito.eq(OfflineEnduserContract.MarkerEntry.TABLE_NAME),
        any(String[].class), anyString(), any(String[].class), anyString(),
        anyString(), anyString());

    Assert.assertNotNull(marker);
  }

  @Test
  public void tesInsertOrUpdateNewEntity() {
    Marker marker = new Marker();
    marker.setId("1");

    long row = mMarkerDatabaseAdapter.insertOrUpdateMarker(marker, 0);

    Mockito.verify(mMockedDatabase).query(
        Mockito.eq(OfflineEnduserContract.MarkerEntry.TABLE_NAME),
        any(String[].class), anyString(), any(String[].class), anyString(),
        anyString(), anyString());

    Mockito.verify(mMockedDatabase).insert(
        Mockito.eq(OfflineEnduserContract.MarkerEntry.TABLE_NAME),
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
        Mockito.eq(OfflineEnduserContract.MarkerEntry.TABLE_NAME),
        any(String[].class), anyString(), any(String[].class), anyString(),
        anyString(), anyString());

    Mockito.verify(mMockedDatabase).update(
        Mockito.eq(OfflineEnduserContract.MarkerEntry.TABLE_NAME),
        any(ContentValues.class), anyString(), any(String[].class));
    Assert.assertNotEquals(row, -1L);
  }
}
