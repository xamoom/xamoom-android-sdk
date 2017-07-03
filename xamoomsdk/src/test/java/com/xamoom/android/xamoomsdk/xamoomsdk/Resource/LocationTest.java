/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
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
