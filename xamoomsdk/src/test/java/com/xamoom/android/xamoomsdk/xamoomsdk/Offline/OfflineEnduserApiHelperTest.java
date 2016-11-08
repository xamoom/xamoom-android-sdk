package com.xamoom.android.xamoomsdk.xamoomsdk.Offline;

import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.Offline.OfflineEnduserApiHelper;
import com.xamoom.android.xamoomsdk.Resource.Location;
import com.xamoom.android.xamoomsdk.Resource.Spot;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class OfflineEnduserApiHelperTest {

  @Test
  public void testGetSpotsInGeofence() {
    Spot spot1 = new Spot();
    spot1.setLocation(new Location(46.6222743, 14.2619214));

    Spot spot2 = new Spot();
    spot2.setLocation(new Location(46.6182128, 14.2610747));

    ArrayList<Spot> spots = new ArrayList<>();
    spots.add(spot1);
    spots.add(spot2);

    android.location.Location location = new android.location.Location("custom");
    location.setLatitude(46.6222743);
    location.setLongitude(14.2619214);

    ArrayList<Spot> geofenceSpots = OfflineEnduserApiHelper.getSpotsInGeofence(location, spots);

    Assert.assertEquals(1, geofenceSpots.size());
    Assert.assertEquals(spot1, geofenceSpots.get(0));
  }

}
