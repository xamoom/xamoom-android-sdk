package com.xamoom.android.xamoomsdk.xamoomsdk.Storage.Database;

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
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.mockito.Matchers.any;
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
        new MarkerDatabaseAdapter(RuntimeEnvironment.application);
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
}
