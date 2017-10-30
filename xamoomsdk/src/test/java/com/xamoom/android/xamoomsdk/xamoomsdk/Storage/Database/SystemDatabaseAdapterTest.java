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
import com.xamoom.android.xamoomsdk.Resource.Menu;
import com.xamoom.android.xamoomsdk.Resource.Style;
import com.xamoom.android.xamoomsdk.Resource.System;
import com.xamoom.android.xamoomsdk.Resource.SystemSetting;
import com.xamoom.android.xamoomsdk.Storage.Database.DatabaseHelper;
import com.xamoom.android.xamoomsdk.Storage.Database.MenuDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.Database.SettingDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.Database.StyleDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.Database.SystemDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.TableContracts.OfflineEnduserContract;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.res.StyleData;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class SystemDatabaseAdapterTest {

  private SystemDatabaseAdapter mSystemDatabaseAdapter;
  private StyleDatabaseAdapter mMockedStyleDatabaseAdapter;
  private SettingDatabaseAdapter mMockedSettingDatabaseAdapter;
  private MenuDatabaseAdapter mMockedMenuDatabaseAdapter;
  private DatabaseHelper mMockedDatabaseHelper;
  private SQLiteDatabase mMockedDatabase;

  @Before
  public void setup() {
    mSystemDatabaseAdapter =
        SystemDatabaseAdapter.getInstance(RuntimeEnvironment.application);
    mMockedStyleDatabaseAdapter = mock(StyleDatabaseAdapter.class);
    mMockedSettingDatabaseAdapter = mock(SettingDatabaseAdapter.class);
    mMockedMenuDatabaseAdapter = mock(MenuDatabaseAdapter.class);
    mMockedDatabaseHelper = mock(DatabaseHelper.class);
    mMockedDatabase = mock(SQLiteDatabase.class);
    mSystemDatabaseAdapter.setDatabaseHelper(mMockedDatabaseHelper);
    mSystemDatabaseAdapter.setStyleDatabaseAdapter(mMockedStyleDatabaseAdapter);
    mSystemDatabaseAdapter.setSettingDatabaseAdapter(mMockedSettingDatabaseAdapter);
    mSystemDatabaseAdapter.setMenuDatabaseAdapter(mMockedMenuDatabaseAdapter);

    Mockito.stub(mMockedDatabaseHelper.getWritableDatabase())
        .toReturn(mMockedDatabase);
    Mockito.stub(mMockedDatabaseHelper.getReadableDatabase())
        .toReturn(mMockedDatabase);
  }

  @Test
  public void testGetSystemWithoutId() {
    Cursor mockedCursor = mock(Cursor.class);

    Mockito.stub(mMockedDatabase.query(
        Mockito.anyString(), Mockito.any(String[].class), Mockito.anyString(),
        Mockito.any(String[].class), Mockito.anyString(), Mockito.anyString(),
        Mockito.anyString())).toReturn(mockedCursor);

    Mockito.stub(mockedCursor.getCount()).toReturn(1);
    Mockito.stub(mockedCursor.moveToFirst()).toReturn(true);

    System savedSystem = mSystemDatabaseAdapter.getSystem();

    Mockito.verify(mMockedDatabase).query(
        Mockito.anyString(), Mockito.any(String[].class), anyString(),
        Mockito.any(String[].class), Mockito.anyString(), Mockito.anyString(),
        Mockito.anyString());

    Assert.assertNotNull(savedSystem);
  }

  @Test
  public void testGetSystem() {
    Cursor mockedCursor = mock(Cursor.class);

    Mockito.stub(mMockedDatabase.query(
        Mockito.anyString(), Mockito.any(String[].class), Mockito.anyString(),
        Mockito.any(String[].class), Mockito.anyString(), Mockito.anyString(),
        Mockito.anyString())).toReturn(mockedCursor);

    Mockito.stub(mockedCursor.getCount()).toReturn(1);
    Mockito.stub(mockedCursor.moveToFirst()).toReturn(true);

    System savedSystem = mSystemDatabaseAdapter.getSystem("1");

    Mockito.verify(mMockedDatabase).query(
        Mockito.anyString(), Mockito.any(String[].class), Mockito.eq(OfflineEnduserContract.SystemEntry.COLUMN_NAME_JSON_ID + " = ?"),
        Mockito.any(String[].class), Mockito.anyString(), Mockito.anyString(),
        Mockito.anyString());

    Assert.assertNotNull(savedSystem);
  }

  @Test
  public void testGetSystemWithRow() {
    Cursor mockedCursor = mock(Cursor.class);

    System system = new System();
    system.setId("1");
    system.setName("Content name");
    mSystemDatabaseAdapter.insertOrUpdateSystem(system);

    Mockito.stub(mMockedDatabase.query(
        Mockito.anyString(), Mockito.any(String[].class), anyString(),
        Mockito.any(String[].class), Mockito.anyString(), Mockito.anyString(),
        Mockito.anyString())).toReturn(mockedCursor);

    Mockito.stub(mockedCursor.getCount()).toReturn(1);
    Mockito.stub(mockedCursor.moveToFirst()).toReturn(true);

    System savedSystem = mSystemDatabaseAdapter.getSystem(1);

    Mockito.verify(mMockedDatabase).query(
        Mockito.anyString(), Mockito.any(String[].class), Mockito.eq(OfflineEnduserContract.SystemEntry._ID + " = ?"),
        Mockito.any(String[].class), Mockito.anyString(), Mockito.anyString(),
        Mockito.anyString());

    Assert.assertNotNull(savedSystem);
  }

  @Test
  public void insertOrUpdateSystemInsertingNew() {
    System system = new System();
    system.setId("1");
    system.setName("Name");

    ContentValues values = new ContentValues();
    values.put(OfflineEnduserContract.SystemEntry.COLUMN_NAME_JSON_ID, system.getId());
    values.put(OfflineEnduserContract.SystemEntry.COLUMN_NAME_NAME, system.getName());
    values.put(OfflineEnduserContract.SystemEntry.COLUMN_NAME_WEBCLIENT_URL, system.getWebClientUrl());

    mSystemDatabaseAdapter.insertOrUpdateSystem(system);

    Mockito.verify(mMockedDatabase)
        .insert(Mockito.eq(OfflineEnduserContract.SystemEntry.TABLE_NAME),
            Mockito.isNull(String.class),
            Mockito.eq(values));
  }

  @Test
  public void insertOrUpdateSystemUpdateExisting() {
    System system = new System();
    system.setId("1");
    system.setName("Name");

    ContentValues values = new ContentValues();
    values.put(OfflineEnduserContract.SystemEntry.COLUMN_NAME_JSON_ID, system.getId());
    values.put(OfflineEnduserContract.SystemEntry.COLUMN_NAME_NAME, system.getName());

    Cursor mockCursor = mock(Cursor.class);
    Mockito.stub(mMockedDatabase.query(
        Mockito.anyString(),
        Mockito.any(String[].class),
        Mockito.anyString(),
        Mockito.any(String[].class),
        Mockito.anyString(),
        Mockito.anyString(),
        Mockito.anyString()
    )).toReturn(mockCursor);
    Mockito.stub(mockCursor.moveToFirst()).toReturn(true);

    mSystemDatabaseAdapter.insertOrUpdateSystem(system);

    Mockito.verify(mMockedDatabase)
        .update(Mockito.eq(
            OfflineEnduserContract.SystemEntry.TABLE_NAME),
            Mockito.any(ContentValues.class),
            Mockito.anyString(),
            Mockito.any(String[].class));
  }

  @Test
  public void insertOrUpdateSystemWithRelations() {
    System system = new System();
    system.setId("1");

    Style style = new Style();
    style.setId("2");
    system.setStyle(style);

    Menu menu = new Menu();
    menu.setId("3");
    system.setMenu(menu);

    SystemSetting setting = new SystemSetting();
    setting.setId("4");
    system.setSystemSetting(setting);

    Cursor mockCursor = mock(Cursor.class);
    Mockito.stub(mMockedDatabase.query(
        Mockito.anyString(),
        Mockito.any(String[].class),
        Mockito.anyString(),
        Mockito.any(String[].class),
        Mockito.anyString(),
        Mockito.anyString(),
        Mockito.anyString()
    )).toReturn(mockCursor);
    Mockito.stub(mockCursor.moveToFirst()).toReturn(true);

    mSystemDatabaseAdapter.insertOrUpdateSystem(system);

    Mockito.verify(mMockedStyleDatabaseAdapter)
      .insertOrUpdateStyle(Mockito.eq(style), anyLong());
    Mockito.verify(mMockedSettingDatabaseAdapter)
        .insertOrUpdateSetting(Mockito.eq(setting), anyLong());
    Mockito.verify(mMockedMenuDatabaseAdapter)
        .insertOrUpdate(Mockito.eq(menu), anyLong());
  }

  @Test
  public void testDelete() {
    Mockito.stub(mMockedDatabase.delete(anyString(), anyString(), any(String[].class))).toReturn(1);

    boolean deleted = mSystemDatabaseAdapter.deleteSystem("1");

    junit.framework.Assert.assertTrue(deleted);
  }

  @Test
  public void testDeleteFail() {
    Mockito.stub(mMockedDatabase.delete(anyString(), anyString(), any(String[].class))).toReturn(0);

    boolean deleted = mSystemDatabaseAdapter.deleteSystem("1");

    junit.framework.Assert.assertFalse(deleted);
  }
}
