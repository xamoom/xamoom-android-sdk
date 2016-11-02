package com.xamoom.android.xamoomsdk.xamoomsdk.Storage.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.Resource.Style;
import com.xamoom.android.xamoomsdk.Storage.Database.DatabaseHelper;
import com.xamoom.android.xamoomsdk.Storage.Database.StyleDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.Database.SystemDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.TableContracts.OfflineEnduserContract;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class StyleDatabaseAdapterTest {

  private StyleDatabaseAdapter mStyleDatabaseAdapter;
  private DatabaseHelper mMockedDatabaseHelper;
  private SQLiteDatabase mMockedDatabase;
  private Style mStyle;

  @Before
  public void setup() {
    mStyleDatabaseAdapter =
        StyleDatabaseAdapter.getInstance(RuntimeEnvironment.application);

    mMockedDatabaseHelper = Mockito.mock(DatabaseHelper.class);
    mMockedDatabase = Mockito.mock(SQLiteDatabase.class);
    mStyleDatabaseAdapter.setDatabaseHelper(mMockedDatabaseHelper);

    stub(mMockedDatabaseHelper.getWritableDatabase())
        .toReturn(mMockedDatabase);
    stub(mMockedDatabaseHelper.getReadableDatabase())
        .toReturn(mMockedDatabase);

    mStyle = new Style();
    mStyle.setId("1");
    mStyle.setBackgroundColor("back");
    mStyle.setForegroundFontColor("fore");
    mStyle.setHighlightFontColor("high");
    mStyle.setChromeHeaderColor("chrome");
    mStyle.setIcon("icon");
    mStyle.setCustomMarker("marker");
  }

  @Test
  public void testGetStyle() {
    Cursor mockCursor = Mockito.mock(Cursor.class);
    stub(mMockedDatabase.query(
        anyString(),
        Mockito.any(String[].class),
        anyString(),
        Mockito.any(String[].class),
        anyString(),
        anyString(),
        anyString()
    )).toReturn(mockCursor);
    stub(mockCursor.getCount()).toReturn(1);
    stub(mockCursor.moveToFirst()).toReturn(true);

    Style savedStyle = mStyleDatabaseAdapter.getStyle("1");

    Assert.assertNotNull(savedStyle);
    verify(mockCursor, times(7)).getString(Mockito.anyInt());
  }

  @Test
  public void testInserOrUpdateStyleInsertNew() {
    mStyleDatabaseAdapter.insertOrUpdateStyle(mStyle, 0);

    verify(mMockedDatabase).insert(
        eq(OfflineEnduserContract.StyleEntry.TABLE_NAME), anyString(), any(ContentValues.class));
  }

  @Test
  public void testInserOrUpdateWithUpdate() {
    Cursor mockCursor = Mockito.mock(Cursor.class);
    stub(mMockedDatabase.query(
        anyString(),
        Mockito.any(String[].class),
        anyString(),
        Mockito.any(String[].class),
        anyString(),
        anyString(),
        anyString()
    )).toReturn(mockCursor);
    stub(mockCursor.moveToFirst()).toReturn(true);

    mStyleDatabaseAdapter.insertOrUpdateStyle(mStyle, 0);

    verify(mMockedDatabase).update(
        eq(OfflineEnduserContract.StyleEntry.TABLE_NAME), any(ContentValues.class),
        anyString(), any(String[].class));
  }
}
