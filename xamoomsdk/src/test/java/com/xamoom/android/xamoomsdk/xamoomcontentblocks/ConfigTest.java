package com.xamoom.android.xamoomsdk.xamoomcontentblocks;

import com.xamoom.android.xamoomcontentblocks.Config;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by raphaelseher on 12/01/2017.
 */

public class ConfigTest {

  @Test
  public void testThatAuthorityGetsSet() {
    Assert.assertEquals(Config.AUTHORITY, "com.xamoom.android.xamoomsdk.xamoomsdk.fileprovider");
  }
}
