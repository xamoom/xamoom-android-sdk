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
