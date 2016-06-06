package com.xamoom.android.xamoomsdk.xamoomsdk.Resource;


import android.os.Parcel;

import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.Location;
import com.xamoom.android.xamoomsdk.Resource.Marker;
import com.xamoom.android.xamoomsdk.Resource.Spot;
import com.xamoom.android.xamoomsdk.Resource.System;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "AndroidManifest.xml")
public class SpotTest {

  private Spot mSpot;

  @Before
  public void setup() {
    mSpot = new Spot();
    mSpot.setId("id");
    mSpot.setName("name");
    mSpot.setPublicImageUrl("www.image.url");
    mSpot.setDescription("description");
    Location location = new Location();
    location.setLongitude(1.0);
    location.setLatitude(2.0);
    mSpot.setLocation(location);
    ArrayList<String> tags = new ArrayList<String>();
    tags.add("tag1");
    tags.add("tag2");
    mSpot.setTags(tags);
    ArrayList<Marker> markers = new ArrayList<>();
    Marker marker = new Marker();
    marker.setId("id");
    markers.add(marker);
    mSpot.setMarkers(markers);
    System system = new System();
    system.setName("SystemName");
    mSpot.setSystem(system);
    Content content = new Content();
    content.setId("id");
    mSpot.setContent(content);
  }

  @Test
  public void testConstructor() {
    Spot spot = new Spot();

    assertNotNull(spot);
  }

  @Test
  public void testParcelable() {
    Parcel parcel = Parcel.obtain();
    mSpot.writeToParcel(parcel, 0);
    parcel.setDataPosition(0);
    Spot createdFromParcel = Spot.CREATOR.createFromParcel(parcel);

    assertEquals(mSpot.getId(), createdFromParcel.getId());
    assertEquals(mSpot.getName(), createdFromParcel.getName());
    assertEquals(mSpot.getPublicImageUrl(), createdFromParcel.getPublicImageUrl());
    assertEquals(mSpot.getDescription(), createdFromParcel.getDescription());
    assertEquals(mSpot.getLocation().getLatitude(), createdFromParcel.getLocation().getLatitude());
    assertEquals(mSpot.getLocation().getLongitude(), createdFromParcel.getLocation().getLongitude());
    assertEquals(mSpot.getTags(), createdFromParcel.getTags());
    assertEquals(mSpot.getMarkers().get(0).getId(), createdFromParcel.getMarkers().get(0).getId());
    assertEquals(mSpot.getSystem().getName(), createdFromParcel.getSystem().getName());
    assertEquals(mSpot.getContent().getId(), createdFromParcel.getContent().getId());
  }

}
