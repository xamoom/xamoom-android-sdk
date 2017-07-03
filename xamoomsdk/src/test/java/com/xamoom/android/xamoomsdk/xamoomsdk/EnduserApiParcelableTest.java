/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomsdk.xamoomsdk;

import android.os.Parcel;
import android.telecom.Call;

import com.xamoom.android.xamoomsdk.APICallback;
import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.CallHandler;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.EnduserApiInterface;
import com.xamoom.android.xamoomsdk.Resource.Content;

import org.apache.maven.artifact.ant.shaded.cli.Commandline;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class EnduserApiParcelableTest {

  private EnduserApi mEnduserApi;

  @Before
  public void setup() {
    mEnduserApi = new EnduserApi("api_key", RuntimeEnvironment.application);
    mEnduserApi.setLanguage("other");
  }

  @Test
  public void testParcelable() {
    Parcel parcel = Parcel.obtain();
    mEnduserApi.writeToParcel(parcel, 0);
    parcel.setDataPosition(0);
    EnduserApi createdFromParcel = EnduserApi.CREATOR.createFromParcel(parcel);

    assertNotNull(createdFromParcel.getSystemLanguage());
    assertEquals(mEnduserApi.getLanguage(), createdFromParcel.getLanguage());
    assertNotNull(createdFromParcel.getEnduserApiInterface());
  }
}
