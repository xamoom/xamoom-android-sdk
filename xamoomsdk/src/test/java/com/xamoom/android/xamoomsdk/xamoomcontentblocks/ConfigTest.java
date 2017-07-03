/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomsdk.xamoomcontentblocks;

import com.xamoom.android.xamoomcontentblocks.Config;

import junit.framework.Assert;

import org.junit.Test;

public class ConfigTest {

  @Test
  public void testThatAuthorityGetsSet() {
    Assert.assertEquals(Config.AUTHORITY, "com.xamoom.android.xamoomsdk.xamoomsdk.fileprovider");
  }
}
