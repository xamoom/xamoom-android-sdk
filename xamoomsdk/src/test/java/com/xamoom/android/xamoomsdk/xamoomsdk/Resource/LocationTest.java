package com.xamoom.android.xamoomsdk.xamoomsdk.Resource;

import android.os.Parcel;

import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Location;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(RobolectricGradleTestRunner.class)
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
