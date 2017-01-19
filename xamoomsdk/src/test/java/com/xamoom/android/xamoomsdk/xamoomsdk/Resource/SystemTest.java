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

package com.xamoom.android.xamoomsdk.xamoomsdk.Resource;

import android.os.Parcel;

import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.Resource.Menu;
import com.xamoom.android.xamoomsdk.Resource.Style;
import com.xamoom.android.xamoomsdk.Resource.System;
import com.xamoom.android.xamoomsdk.Resource.SystemSetting;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "AndroidManifest.xml")
public class SystemTest {

  private System mSystem;

  @Before
  public void setup() {
    mSystem = new System();
    mSystem.setName("name");
    mSystem.setId("id");
    mSystem.setUrl("www.system.url");
    Style style = new Style();
    style.setId("id");
    mSystem.setStyle(style);
    SystemSetting setting = new SystemSetting();
    setting.setId("id");
    mSystem.setSystemSetting(setting);
    Menu menu = new Menu();
    menu.setId("id");
    mSystem.setMenu(menu);
  }

  @Test
  public void testConstructor() {
    System system = new System();

    assertNotNull(system);
  }

  @Test
  public void testParcelable() {
    Parcel parcel = Parcel.obtain();
    mSystem.writeToParcel(parcel, 0);
    parcel.setDataPosition(0);
    System createdFromParcel = System.CREATOR.createFromParcel(parcel);

    assertEquals(mSystem.getId(), createdFromParcel.getId());
    assertEquals(mSystem.getName(), createdFromParcel.getName());
    assertEquals(mSystem.getUrl(), createdFromParcel.getUrl());
    assertEquals(mSystem.getStyle().getId(), createdFromParcel.getStyle().getId());
    assertEquals(mSystem.getSystemSetting().getId(), createdFromParcel.getSystemSetting().getId());
    assertEquals(mSystem.getMenu().getId(), createdFromParcel.getMenu().getId());
  }

}
