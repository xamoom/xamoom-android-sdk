/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomsdk.xamoomsdk.Storage.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.Storage.Database.DatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.Database.DatabaseHelper;
import com.xamoom.android.xamoomsdk.Storage.TableContracts.OfflineEnduserContract;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class DatabaseAdapterTest {

  private DatabaseAdapter mDatabaseAdapter;
  private DatabaseHelper mMockedDatabaseHelper;
  private SQLiteDatabase mMockedDatabase;

  @Before
  public void setup() {
    mDatabaseAdapter =
        new DatabaseAdapter(RuntimeEnvironment.application);
    mMockedDatabaseHelper = Mockito.mock(DatabaseHelper.class);
    mMockedDatabase = Mockito.mock(SQLiteDatabase.class);
    mDatabaseAdapter.setDatabaseHelper(mMockedDatabaseHelper);

    Mockito.stub(mMockedDatabaseHelper.getWritableDatabase())
        .toReturn(mMockedDatabase);
    Mockito.stub(mMockedDatabaseHelper.getReadableDatabase())
        .toReturn(mMockedDatabase);
  }

  @Test
  public void testConstructor() {
    Assert.assertNotNull(new DatabaseAdapter(RuntimeEnvironment.application));
  }

  @Test
  public void testOpen() {
    DatabaseAdapter adapter = mDatabaseAdapter.open();
    Assert.assertNotNull(adapter);
    Assert.assertEquals(adapter, mDatabaseAdapter);
  }

  @Test
  public void testClose() {
    mDatabaseAdapter.close();

    Mockito.verify(mMockedDatabaseHelper).close();
  }
}
