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

package com.xamoom.android.xamoomsdk.xamoomsdk.Resource;

import android.os.Parcel;

import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.Resource.Menu;
import com.xamoom.android.xamoomsdk.Resource.Style;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "AndroidManifest.xml")
public class StyleTest {

  private Style mStyle;

  @Before
  public void setup() {
    mStyle = new Style();
    mStyle.setId("id");
    mStyle.setBackgroundColor("background");
    mStyle.setHighlightFontColor("highlight");
    mStyle.setForegroundFontColor("foreground");
    mStyle.setChromeHeaderColor("chromeheader");
    mStyle.setCustomMarker("custommarker");
    mStyle.setIcon("icon");
  }

  @Test
  public void testConstructor() {
    Style style = new Style();

    assertNotNull(style);
  }

  @Test
  public void testParcelable() {
    Parcel parcel = Parcel.obtain();
    mStyle.writeToParcel(parcel, 0);
    parcel.setDataPosition(0);
    Style createdFromParcel = Style.CREATOR.createFromParcel(parcel);

    assertEquals(mStyle.getId(), createdFromParcel.getId());
    assertEquals(mStyle.getBackgroundColor(), createdFromParcel.getBackgroundColor());
    assertEquals(mStyle.getHighlightFontColor(), createdFromParcel.getHighlightFontColor());
    assertEquals(mStyle.getForegroundFontColor(), createdFromParcel.getForegroundFontColor());
    assertEquals(mStyle.getChromeHeaderColor(), createdFromParcel.getChromeHeaderColor());
    assertEquals(mStyle.getCustomMarker(), createdFromParcel.getCustomMarker());
    assertEquals(mStyle.getIcon(), createdFromParcel.getIcon());
  }
}
