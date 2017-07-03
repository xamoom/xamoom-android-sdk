/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomsdk.Enums;

import android.location.Location;

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
  INCLUDE_MARKERS,
  /**
   * Will only return spots with a location
   */
  HAS_LOCATION
}
