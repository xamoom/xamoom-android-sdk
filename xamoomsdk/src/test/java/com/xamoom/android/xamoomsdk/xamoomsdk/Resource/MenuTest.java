package com.xamoom.android.xamoomsdk.xamoomsdk.Resource;

import android.os.Parcel;
import android.test.suitebuilder.annotation.SmallTest;

import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.Menu;
import com.xamoom.android.xamoomsdk.Resource.Spot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "AndroidManifest.xml")
public class MenuTest {

  private Menu mMenu;

  @Before
  public void setup() {
    mMenu = new Menu();
    mMenu.setId("id");
    Content content1 = new Content();
    content1.setId("id1");
    Content content2 = new Content();
    content2.setId("id2");
    ArrayList<Content> contents = new ArrayList<>();
    contents.add(content1);
    contents.add(content2);
    mMenu.setItems(contents);
  }

  @Test
  public void testConstructor() {
    Menu menu = new Menu();

    assertNotNull(menu);
  }

  @Test
  public void testParcelable() {
    Parcel parcel = Parcel.obtain();
    mMenu.writeToParcel(parcel, 0);
    parcel.setDataPosition(0);
    Menu createdFromParcel = Menu.CREATOR.createFromParcel(parcel);

    assertEquals(mMenu.getId(), createdFromParcel.getId());
    assertEquals(mMenu.getItems().get(0).getId(), createdFromParcel.getItems().get(0).getId());
    assertEquals(mMenu.getItems().get(1).getId(), createdFromParcel.getItems().get(1).getId());
  }

}
