/*
* Copyright 2017 by xamoom GmbH <apps@xamoom.com>
*
* This file is part of some open source application.
*
* Some open source application is free software: you can redistribute
* it and/or modify it under the terms of the GNU General Public
* License as published by the Free Software Foundation, either
* version 2 of the License, or (at your option) any later version.
*
* Some open source application is distributed in the hope that it will
* be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
* of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with xamoom-android-sdk. If not, see <http://www.gnu.org/licenses/>.
*
* author: Raphael Seher <raphael@xamoom.com>
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
