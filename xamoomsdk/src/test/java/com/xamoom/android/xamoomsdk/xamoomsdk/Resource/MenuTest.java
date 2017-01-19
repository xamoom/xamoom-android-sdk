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
import android.test.suitebuilder.annotation.SmallTest;

import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.Menu;
import com.xamoom.android.xamoomsdk.Resource.Spot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "AndroidManifest.xml")
public class MenuTest {

  private Menu mMenu;

  @Before
  public void setup() {
    mMenu = new Menu();
    mMenu.setId("id");
    Content content1 = new Content();
    content1.setId("id1");
    Content content2 = new Content();
    content2.setId("id2");
    ArrayList<Content> contents = new ArrayList<>();
    contents.add(content1);
    contents.add(content2);
    mMenu.setItems(contents);
  }

  @Test
  public void testConstructor() {
    Menu menu = new Menu();

    assertNotNull(menu);
  }

  @Test
  public void testParcelable() {
    Parcel parcel = Parcel.obtain();
    mMenu.writeToParcel(parcel, 0);
    parcel.setDataPosition(0);
    Menu createdFromParcel = Menu.CREATOR.createFromParcel(parcel);

    assertEquals(mMenu.getId(), createdFromParcel.getId());
    assertEquals(mMenu.getItems().get(0).getId(), createdFromParcel.getItems().get(0).getId());
    assertEquals(mMenu.getItems().get(1).getId(), createdFromParcel.getItems().get(1).getId());
  }

}
