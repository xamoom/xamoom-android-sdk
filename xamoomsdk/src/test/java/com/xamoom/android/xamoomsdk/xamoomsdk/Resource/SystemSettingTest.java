package com.xamoom.android.xamoomsdk.xamoomsdk.Resource;

import android.os.Parcel;
import android.test.suitebuilder.annotation.SmallTest;

import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.Resource.System;
import com.xamoom.android.xamoomsdk.Resource.SystemSetting;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "AndroidManifest.xml")
public class SystemSettingTest {

  private SystemSetting mSystemSetting;

  @Before
  public void setup() {
    mSystemSetting = new SystemSetting();
    mSystemSetting.setId("id");
    mSystemSetting.setGooglePlayAppId("googleplayappid");
    mSystemSetting.setItunesAppId("itunesappid");
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
  }

}
