package com.xamoom.android.xamoomsdk.Enums;

import android.location.Location;

import com.xamoom.android.xamoomsdk.APIListCallback;
import com.xamoom.android.xamoomsdk.EnduserApi;

import java.util.EnumSet;

/**
 * ContentSortFlags are options to sort the result when getting contents.
 *
 * @see EnduserApi#getSpotsByLocation(Location, int, EnumSet, EnumSet, APIListCallback)
 */
public enum ContentSortFlags {
  /**
   * Sort ascending by name.
   */
  NAME,
  /**
   * Sort descending by name.
   */
  NAME_DESC
}
