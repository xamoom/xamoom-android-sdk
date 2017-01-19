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

package com.xamoom.android.xamoomsdk.xamoomsdk.Storage.Database;

import android.database.sqlite.SQLiteDatabase;

import com.xamoom.android.xamoomsdk.BuildConfig;
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
public class DatabaseHelperTest {

  private DatabaseHelper databaseHelper;

  @Before
  public void setup() {
    databaseHelper = new DatabaseHelper(RuntimeEnvironment.application);
  }

  @Test
  public void testConstructor() {
    DatabaseHelper databaseHelper = new DatabaseHelper(RuntimeEnvironment.application);

    Assert.assertNotNull(databaseHelper.getReadableDatabase());
    Assert.assertNotNull(databaseHelper);
  }

  @Test
  public void testOnCreate() {
    SQLiteDatabase mockedDb = Mockito.mock(SQLiteDatabase.class);

    databaseHelper.onCreate(mockedDb);

    Mockito.verify(mockedDb).execSQL(OfflineEnduserContract.SystemEntry.CREATE_TABLE);
    Mockito.verify(mockedDb).execSQL(OfflineEnduserContract.StyleEntry.CREATE_TABLE);
    Mockito.verify(mockedDb).execSQL(OfflineEnduserContract.SettingEntry.CREATE_TABLE);
    Mockito.verify(mockedDb).execSQL(OfflineEnduserContract.ContentBlockEntry.CREATE_TABLE);
    Mockito.verify(mockedDb).execSQL(OfflineEnduserContract.ContentEntry.CREATE_TABLE);
    Mockito.verify(mockedDb).execSQL(OfflineEnduserContract.SpotEntry.CREATE_TABLE);
    Mockito.verify(mockedDb).execSQL(OfflineEnduserContract.MenuEntry.CREATE_TABLE);
  }
}
