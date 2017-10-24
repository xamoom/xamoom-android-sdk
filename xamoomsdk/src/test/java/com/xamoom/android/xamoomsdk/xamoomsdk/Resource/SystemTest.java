/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomsdk.xamoomsdk.Resource;

import android.os.Parcel;

import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.Resource.Menu;
import com.xamoom.android.xamoomsdk.Resource.Style;
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
public class SystemTest {

  private System mSystem;

  @Before
  public void setup() {
    mSystem = new System();
    mSystem.setName("name");
    mSystem.setId("id");
    mSystem.setUrl("www.system.url");
    Style style = new Style();
    style.setId("id");
    mSystem.setStyle(style);
    SystemSetting setting = new SystemSetting();
    setting.setId("id");
    mSystem.setSystemSetting(setting);
    Menu menu = new Menu();
    menu.setId("id");
    mSystem.setMenu(menu);
    mSystem.setWebClientUrl("www.xamoom.com");
  }

  @Test
  public void testConstructor() {
    System system = new System();

    assertNotNull(system);
  }

  @Test
  public void testParcelable() {
    Parcel parcel = Parcel.obtain();
    mSystem.writeToParcel(parcel, 0);
    parcel.setDataPosition(0);
    System createdFromParcel = System.CREATOR.createFromParcel(parcel);

    assertEquals(mSystem.getId(), createdFromParcel.getId());
    assertEquals(mSystem.getName(), createdFromParcel.getName());
    assertEquals(mSystem.getUrl(), createdFromParcel.getUrl());
    assertEquals(mSystem.getStyle().getId(), createdFromParcel.getStyle().getId());
    assertEquals(mSystem.getSystemSetting().getId(), createdFromParcel.getSystemSetting().getId());
    assertEquals(mSystem.getMenu().getId(), createdFromParcel.getMenu().getId());
    assertEquals(mSystem.getWebClientUrl(), createdFromParcel.getWebClientUrl());
  }

}
