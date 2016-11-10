package com.xamoom.android.xamoomsdk.xamoomsdk.Offline;

import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.Offline.OfflineEnduserApiHelper;
import com.xamoom.android.xamoomsdk.Resource.Content;
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

  @Test
  public void testGetContentsWithTags() {
    ArrayList<Content> contents = new ArrayList<>();

    ArrayList<String> tags1 = new ArrayList<>();
    tags1.add("tag1");
    Content content1 = new Content();
    content1.setTags(tags1);
    contents.add(content1);

    ArrayList<String> tags2 = new ArrayList<>();
    tags2.add("tag1");
    tags2.add("tag2");
    Content content2 = new Content();
    content2.setTags(tags2);
    contents.add(content2);

    ArrayList<Content> contentsWithTag1 = OfflineEnduserApiHelper
        .getContentsWithTags(tags1, contents);

    ArrayList<String> onlyTag2 = new ArrayList<>();
    onlyTag2.add("tag2");
    ArrayList<Content> contentsWithTag2 = OfflineEnduserApiHelper
        .getContentsWithTags(onlyTag2, contents);

    Assert.assertEquals(2, contentsWithTag1.size());
    Assert.assertEquals(1, contentsWithTag2.size());
  }

  @Test
  public void testPageResult() {
    ArrayList<String> list = new ArrayList<>();
    list.add("1");
    list.add("2");

    OfflineEnduserApiHelper.PagedResult<String> result1 =
        OfflineEnduserApiHelper.pageResults(list, 1, null);
    OfflineEnduserApiHelper.PagedResult<String> result2 =
        OfflineEnduserApiHelper.pageResults(list, 1, "1");

    Assert.assertEquals(result1.getObjects().get(0), "1");
    Assert.assertTrue(result1.hasMore());
    Assert.assertEquals(result1.getCursor(), "1");

    Assert.assertEquals(result2.getObjects().get(0), "2");
    Assert.assertFalse(result2.hasMore());
  }

  @Test
  public void testPageResultBiggerPageSizeThanListSize() {
    ArrayList<String> list = new ArrayList<>();
    list.add("1");
    list.add("2");

    OfflineEnduserApiHelper.PagedResult<String> result1 =
        OfflineEnduserApiHelper.pageResults(list, 10, null);

    Assert.assertEquals(result1.getObjects().size(), 2);
    Assert.assertFalse(result1.hasMore());
    Assert.assertEquals(result1.getCursor(), "10");
  }

  @Test
  public void testGetSpotsInRadius() {
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

    ArrayList<Spot> geofenceSpots = OfflineEnduserApiHelper.getSpotsInRadius(location, 100, spots);

    Assert.assertEquals(1, geofenceSpots.size());
    Assert.assertEquals(spot1, geofenceSpots.get(0));
  }

  @Test
  public void testGetSpotsWithTags() {
    Spot spot1 = new Spot();
    ArrayList<String> tags1 = new ArrayList<>();
    tags1.add("tag1");
    spot1.setTags(tags1);

    Spot spot2 = new Spot();
    ArrayList<String> tags2 = new ArrayList<>();
    tags2.add("tag1");
    tags2.add("tag2");
    spot2.setTags(tags2);

    ArrayList<Spot> spots = new ArrayList<>();
    spots.add(spot1);
    spots.add(spot2);

    ArrayList<Spot> spotsWithTag1 = OfflineEnduserApiHelper.getSpotsWithTags(tags1, spots);

    Assert.assertEquals(2, spotsWithTag1.size());
    Assert.assertEquals(spot1, spotsWithTag1.get(0));

    ArrayList<Spot> spotsWithTag2 = OfflineEnduserApiHelper.getSpotsWithTags(tags2, spots);

    Assert.assertEquals(2, spotsWithTag2.size());
  }
}
