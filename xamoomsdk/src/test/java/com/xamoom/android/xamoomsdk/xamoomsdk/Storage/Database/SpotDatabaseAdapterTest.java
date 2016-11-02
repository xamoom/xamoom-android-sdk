package com.xamoom.android.xamoomsdk.xamoomsdk.Storage.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.suitebuilder.annotation.SmallTest;

import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.Spot;
import com.xamoom.android.xamoomsdk.Resource.System;
import com.xamoom.android.xamoomsdk.Storage.Database.ContentDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.Database.DatabaseHelper;
import com.xamoom.android.xamoomsdk.Storage.Database.SpotDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.Database.SystemDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.TableContracts.OfflineEnduserContract;

import org.bouncycastle.asn1.dvcs.Data;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class SpotDatabaseAdapterTest {
  private SpotDatabaseAdapter mSpotDatabaseAdapter;
  private DatabaseHelper mMockedDatabaseHelper;
  private SQLiteDatabase mMockedDatabase;
  private Cursor mMockedCursor;
  private SystemDatabaseAdapter mMockedSystemDatabaseAdapter;
  private ContentDatabaseAdapter mMockedContentDatabaseadapter;

  @Before
  public void setup() {
    mSpotDatabaseAdapter = new SpotDatabaseAdapter(RuntimeEnvironment.application);
    mMockedDatabaseHelper = mock(DatabaseHelper.class);
    mMockedDatabase = mock(SQLiteDatabase.class);
    mSpotDatabaseAdapter.setDatabaseHelper(mMockedDatabaseHelper);
    mMockedContentDatabaseadapter = mock(ContentDatabaseAdapter.class);
    mMockedSystemDatabaseAdapter = mock(SystemDatabaseAdapter.class);
    mSpotDatabaseAdapter.setContentDatabaseAdapter(mMockedContentDatabaseadapter);
    mSpotDatabaseAdapter.setSystemDatabaseAdapter(mMockedSystemDatabaseAdapter);
    mMockedCursor = mock(Cursor.class);

    Mockito.stub(mMockedDatabaseHelper.getWritableDatabase())
        .toReturn(mMockedDatabase);
    Mockito.stub(mMockedDatabaseHelper.getReadableDatabase())
        .toReturn(mMockedDatabase);
    Mockito.stub(mMockedDatabase.query(anyString(), any(String[].class), anyString(),
        any(String[].class), anyString(), anyString(), anyString())).toReturn(mMockedCursor);
  }

  @Test
  public void testGetSpot() {
    Mockito.stub(mMockedCursor.getCount()).toReturn(1);
    Mockito.stub(mMockedCursor.moveToFirst()).toReturn(true);

    Spot spot = mSpotDatabaseAdapter.getSpot("1");

    Mockito.verify(mMockedDatabase).query(
        Mockito.eq(OfflineEnduserContract.SpotEntry.TABLE_NAME),
        any(String[].class), anyString(), any(String[].class), anyString(),
        anyString(), anyString());

    Assert.assertNotNull(spot);
  }

  @Test
  public void testInsertOrUpdateNewEntity() {
    Spot spot = new Spot();
    spot.setId("1");

    Content content = new Content();
    content.setId("2");

    System system = new System();
    system.setId("3");

    spot.setSystem(system);
    spot.setContent(content);

    long row = mSpotDatabaseAdapter.insertOrUpdateSpot(spot);

    Mockito.verify(mMockedDatabase).query(
        Mockito.eq(OfflineEnduserContract.SpotEntry.TABLE_NAME),
        any(String[].class), anyString(), any(String[].class), anyString(),
        anyString(), anyString());

    Mockito.verify(mMockedDatabase).insert(
        Mockito.eq(OfflineEnduserContract.SpotEntry.TABLE_NAME),
        anyString(), any(ContentValues.class));

    Mockito.verify(mMockedContentDatabaseadapter).insertOrUpdateContent(
        Mockito.eq(content), anyBoolean(), anyLong());

    Mockito.verify(mMockedSystemDatabaseAdapter).insertOrUpdateSystem(
        Mockito.eq(system));
  }

  @Test
  public void testInsertOrUpdateExistingEntity() {
    Spot spot = new Spot();
    spot.setId("1");

    Mockito.stub(mMockedDatabase.query(anyString(), any(String[].class), anyString(),
        any(String[].class), anyString(), anyString(), anyString())).toReturn(mMockedCursor);
    Mockito.stub(mMockedCursor.moveToFirst()).toReturn(true);
    Mockito.stub(mMockedCursor.getInt(anyInt())).toReturn(1);

    long row = mSpotDatabaseAdapter.insertOrUpdateSpot(spot);

    Mockito.verify(mMockedDatabase).query(
        Mockito.eq(OfflineEnduserContract.SpotEntry.TABLE_NAME),
        any(String[].class), anyString(), any(String[].class), anyString(),
        anyString(), anyString());

    Mockito.verify(mMockedDatabase).update(
        Mockito.eq(OfflineEnduserContract.SpotEntry.TABLE_NAME),
        any(ContentValues.class), anyString(), any(String[].class));
    Assert.assertNotEquals(row, -1L);
  }
}
