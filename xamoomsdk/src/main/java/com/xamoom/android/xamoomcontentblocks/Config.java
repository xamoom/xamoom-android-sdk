/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomcontentblocks;

import com.xamoom.android.xamoomsdk.BuildConfig;

/**
 * Config used in complete SDK.
 */

public class Config {
  /**
   * Authority for the SDK fileprovider.
   * Uses the applicationId so it can be used in different apps.
   */
  public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".xamoomsdk.fileprovider";
}
