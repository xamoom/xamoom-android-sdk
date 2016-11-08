package com.xamoom.android.xamoomsdk.Offline;

import android.location.Location;

import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.Spot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class OfflineEnduserApiHelper {
  private static final float GEOFENCE_RADIUS = 40.0F;

  public static ArrayList<Spot> getSpotsInGeofence(Location location, ArrayList<Spot> allSpots) {
    ArrayList<Spot> spotsInGeofence = new ArrayList<>();
    for (Spot spot : allSpots) {
      Location spotLocation = new Location("custom");
      spotLocation.setLatitude(spot.getLocation().getLatitude());
      spotLocation.setLongitude(spot.getLocation().getLongitude());

      if (location.distanceTo(spotLocation) <= GEOFENCE_RADIUS) {
        spotsInGeofence.add(spot);
      }
    }

    return spotsInGeofence;
  }

  public static ArrayList<Content> getContentsWithTags(List<String> tags, ArrayList<Content> contents) {
    ArrayList<Content> contentsWithTags = new ArrayList<>();
    for (Content content : contents) {
      boolean hasTag = false;
      for (String tag : tags) {
        if (content.getTags().contains(tag)) {
          hasTag = true;
        }
      }
      if (hasTag) {
        contentsWithTags.add(content);
      }
    }

    return contentsWithTags;
  }

  public static <E> PagedResult pageResults(ArrayList<E> list, int pageSize, String cursor) {
    int intCursor = 0;
    if (cursor != null) {
      intCursor = Integer.valueOf(cursor);
    }

    ArrayList<E> pagedList = new ArrayList<>();
    pagedList.addAll(list.subList(intCursor, intCursor + pageSize));

    PagedResult<E> pagedResult = new PagedResult<>(pagedList, String.valueOf(intCursor + pageSize),
        (list.size() > intCursor+pageSize));

    return pagedResult;
  }

  public static class PagedResult <E> {
    ArrayList<E> objects;
    String cursor;
    boolean hasMore;

    public PagedResult(ArrayList<E> objects, String cursor, boolean hasMore) {
      this.objects = objects;
      this.cursor = cursor;
      this.hasMore = hasMore;
    }

    public ArrayList<E> getObjects() {
      return objects;
    }

    public String getCursor() {
      return cursor;
    }

    public boolean hasMore() {
      return hasMore;
    }
  }
}
