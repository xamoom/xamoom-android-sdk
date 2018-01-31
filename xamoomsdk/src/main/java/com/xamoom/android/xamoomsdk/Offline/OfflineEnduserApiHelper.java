/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomsdk.Offline;

import android.location.Location;

import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.Spot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class OfflineEnduserApiHelper {
  private static final int GEOFENCE_RADIUS = 40;

  public static ArrayList<Spot> getSpotsInGeofence(Location location, ArrayList<Spot> allSpots) {
    return getSpotsInRadius(location, GEOFENCE_RADIUS, allSpots);
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

  public static ArrayList<Spot> getSpotsWithTags(List<String> tags, ArrayList<Spot> spots) {
    ArrayList<Spot> spotsWithTags = new ArrayList<>();
    for (Spot spot : spots) {
      boolean hasTag = false;
      for (String tag : tags) {
        if (spot.getTags().contains(tag)) {
          hasTag = true;
        }
      }
      if (hasTag) {
        spotsWithTags.add(spot);
      }
    }

    return spotsWithTags;
  }

  public static ArrayList<Spot> getSpotsInRadius(Location location, int radius, ArrayList<Spot> allSpots) {
    ArrayList<Spot> spotsInRadius = new ArrayList<>();
    for (Spot spot : allSpots) {
      Location spotLocation = new Location("custom");
      spotLocation.setLatitude(spot.getLocation().getLatitude());
      spotLocation.setLongitude(spot.getLocation().getLongitude());

      if (location.distanceTo(spotLocation) <= radius) {
        spotsInRadius.add(spot);
      }
    }

    return spotsInRadius;
  }

  public static ArrayList<Spot> sortSpotsByName(ArrayList<Spot> spots, final boolean asc) {
    Collections.sort(spots, new Comparator<Spot>() {
      @Override
      public int compare(Spot t1, Spot t2) {

        if (asc) {
          return t1.getName().compareTo(t2.getName());
        } else {
          return t2.getName().compareTo(t1.getName());
        }
      }
    });

    return spots;
  }

  public static ArrayList<Spot> sortSpotsByDistance(ArrayList<Spot> spots, final Location location,
                                                    final boolean asc) {
    Collections.sort(spots, new Comparator<Spot>() {
      @Override
      public int compare(Spot t1, Spot t2) {
        Location location1 = new Location("custom");
        location1.setLatitude(t1.getLocation().getLatitude());
        location1.setLongitude(t1.getLocation().getLongitude());

        Location location2 = new Location("custom");
        location2.setLatitude(t2.getLocation().getLatitude());
        location2.setLongitude(t2.getLocation().getLongitude());

        if (asc) {
          return Double.compare(location1.distanceTo(location), location2.distanceTo(location));
        } else {
          return Double.compare(location2.distanceTo(location), location1.distanceTo(location));
        }
      }
    });

    return spots;
  }

  public static ArrayList<Content> sortContentsByTitle(ArrayList<Content> contents, final boolean asc) {
    Collections.sort(contents, new Comparator<Content>() {
      @Override
      public int compare(Content t1, Content t2) {
        if (asc) {
          return t1.getTitle().compareTo(t2.getTitle());
        } else {
          return t2.getTitle().compareTo(t1.getTitle());
        }
      }
    });

    return contents;
  }

  public static <E> PagedResult pageResults(ArrayList<E> list, int pageSize, String cursor) {
    int intCursor = 0;
    if (cursor != null) {
      intCursor = Integer.valueOf(cursor);
    }

    ArrayList<E> pagedList = new ArrayList<>();

    if (list.size() >= intCursor + pageSize) {
      pagedList.addAll(list.subList(intCursor, intCursor + pageSize));
    } else {
      pagedList.addAll(list.subList(intCursor, list.size()));
    }

    boolean hasMore = list.size() > intCursor + pageSize;
    PagedResult<E> pagedResult = new PagedResult<>(pagedList, String.valueOf(intCursor + pageSize),
        hasMore);

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
