package com.xamoom.android.xamoomsdk.xamoomsdk;

import android.os.Parcel;

import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.Resource.Content;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class EnduserApiParcelableTest {

  private EnduserApi mEnduserApi;

  @Before
  public void setup() {
    mEnduserApi = new EnduserApi("api_key");
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
