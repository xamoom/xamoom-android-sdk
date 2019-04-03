/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomsdk.xamoomsdk.Resource;

import android.os.Parcel;
import android.test.suitebuilder.annotation.SmallTest;

import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.Resource.System;
import com.xamoom.android.xamoomsdk.Resource.SystemSetting;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "AndroidManifest.xml")
public class SystemSettingTest {

  private SystemSetting mSystemSetting;

  @Before
  public void setup() {
    mSystemSetting = new SystemSetting();
    mSystemSetting.setId("id");
    mSystemSetting.setGooglePlayAppId("googleplayappid");
    mSystemSetting.setItunesAppId("itunesappid");
    mSystemSetting.setSocialSharingEnabled(true);
    mSystemSetting.setCookieWarningEnabled(true);
    mSystemSetting.setRecommandationEnabled(true);
    mSystemSetting.setEventPackageEnabled(true);
  }

  @Test
  public void testConstructor() {
    SystemSetting systemSetting = new SystemSetting();

    assertNotNull(systemSetting);
  }

  @Test
  public void testParcelable() {
    Parcel parcel = Parcel.obtain();
    mSystemSetting.writeToParcel(parcel, 0);
    parcel.setDataPosition(0);
    SystemSetting createdFromParcel = SystemSetting.CREATOR.createFromParcel(parcel);

    assertEquals(mSystemSetting.getId(), createdFromParcel.getId());
    assertEquals(mSystemSetting.getGooglePlayAppId(), createdFromParcel.getGooglePlayAppId());
    assertEquals(mSystemSetting.getItunesAppId(), createdFromParcel.getItunesAppId());
    assertEquals(mSystemSetting.getSocialSharingEnabled(), createdFromParcel.getSocialSharingEnabled());
    assertEquals(mSystemSetting.getCookieWarningEnabled(), createdFromParcel.getCookieWarningEnabled());
    assertEquals(mSystemSetting.getRecommandationEnabled(), createdFromParcel.getRecommandationEnabled());
    assertEquals(mSystemSetting.getEventPackageEnabled(), createdFromParcel.getEventPackageEnabled());
  }

}
