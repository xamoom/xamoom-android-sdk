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
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Location;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "AndroidManifest.xml")
public class LocationTest {

  private Location mLocation;

  @Before
  public void setup() {
    mLocation = new Location();
    mLocation.setLatitude(1.0);
    mLocation.setLongitude(2.0);
  }

  @Test
  public void testConstuctor() {
    Location location = new Location();

    assertNotNull(location);
  }

  @Test
  public void testParcelable() {
    Parcel parcel = Parcel.obtain();
    mLocation.writeToParcel(parcel, 0);
    parcel.setDataPosition(0);
    Location createdFromParcel = Location.CREATOR.createFromParcel(parcel);

    assertEquals(mLocation.getLatitude(), createdFromParcel.getLatitude());
    assertEquals(mLocation.getLongitude(), createdFromParcel.getLongitude());
  }
}
