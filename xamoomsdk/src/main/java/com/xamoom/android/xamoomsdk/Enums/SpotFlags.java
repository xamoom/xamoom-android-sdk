package com.xamoom.android.xamoomsdk.Enums;

import android.location.Location;

import com.xamoom.android.xamoomsdk.APICallback;
import com.xamoom.android.xamoomsdk.APIListCallback;
import com.xamoom.android.xamoomsdk.EnduserApi;

import java.util.EnumSet;

/**
 * SpotFlags are options you can use when getting spots.
 *
 * @see EnduserApi#getSpotsByLocation(Location, int, EnumSet, EnumSet, APIListCallback)
 */
public enum SpotFlags {
  /**
   * Will include connected content.
   */
  INCLUDE_CONTENT,
  /**
   * Will included assigned markers.
   */
  INCLUDE_MARKERS
}
