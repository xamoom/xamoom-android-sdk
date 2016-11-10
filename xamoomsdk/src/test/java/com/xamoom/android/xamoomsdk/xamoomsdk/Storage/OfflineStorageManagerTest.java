package com.xamoom.android.xamoomsdk.xamoomsdk.Storage;

import android.location.Location;

import com.xamoom.android.xamoomsdk.APIListCallback;
import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.Menu;
import com.xamoom.android.xamoomsdk.Resource.Spot;
import com.xamoom.android.xamoomsdk.Resource.Style;
import com.xamoom.android.xamoomsdk.Resource.System;
import com.xamoom.android.xamoomsdk.Resource.SystemSetting;
import com.xamoom.android.xamoomsdk.Storage.Database.ContentDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.Database.MarkerDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.Database.MenuDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.Database.SettingDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.Database.SpotDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.Database.StyleDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.Database.SystemDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.DownloadManager;
import com.xamoom.android.xamoomsdk.Storage.OfflineStorageManager;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import at.rags.morpheus.Error;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class OfflineStorageManagerTest {

  private OfflineStorageManager mOfflineStorageManager;
  private DownloadManager mMockedDownloadManager;
  private ContentDatabaseAdapter mMockedContentDatabaseAdapter;
  private SpotDatabaseAdapter mMockedSpotDatabaseAdapter;
  private SystemDatabaseAdapter mMockedSystemDatabaseAdapter;
  private StyleDatabaseAdapter mMockedStyleDatabaseAdapter;
  private SettingDatabaseAdapter mMockedSettingDatabaseAdapter;
  private MenuDatabaseAdapter mMockedMenuDatabaseAdapter;
  private MarkerDatabaseAdapter mMockedMarkerDatabaseAdapter;

  @Before
  public void setup() {
    mOfflineStorageManager = OfflineStorageManager.getInstance(RuntimeEnvironment.application);
    mMockedDownloadManager = Mockito.mock(DownloadManager.class);
    mMockedContentDatabaseAdapter = Mockito.mock(ContentDatabaseAdapter.class);
    mMockedSpotDatabaseAdapter = Mockito.mock(SpotDatabaseAdapter.class);
    mMockedSystemDatabaseAdapter = Mockito.mock(SystemDatabaseAdapter.class);
    mMockedStyleDatabaseAdapter = Mockito.mock(StyleDatabaseAdapter.class);
    mMockedSettingDatabaseAdapter = Mockito.mock(SettingDatabaseAdapter.class);
    mMockedMenuDatabaseAdapter = Mockito.mock(MenuDatabaseAdapter.class);
    mMockedMarkerDatabaseAdapter = Mockito.mock(MarkerDatabaseAdapter.class);

    mOfflineStorageManager.setDownloadManager(mMockedDownloadManager);
    mOfflineStorageManager.setContentDatabaseAdapter(mMockedContentDatabaseAdapter);
    mOfflineStorageManager.setSpotDatabaseAdapter(mMockedSpotDatabaseAdapter);
    mOfflineStorageManager.setSystemDatabaseAdapter(mMockedSystemDatabaseAdapter);
    mOfflineStorageManager.setStyleDatabaseAdapter(mMockedStyleDatabaseAdapter);
    mOfflineStorageManager.setSettingDatabaseAdapter(mMockedSettingDatabaseAdapter);
    mOfflineStorageManager.setMenuDatabaseAdapter(mMockedMenuDatabaseAdapter);
    mOfflineStorageManager.setMarkerDatabaseAdapter(mMockedMarkerDatabaseAdapter);
  }

  @Test
  public void testSaveContent() throws MalformedURLException {
    Content content = new Content();
    content.setId("1");
    content.setPublicImageUrl("http://www.xamoom.com");

    Mockito.stub(mMockedContentDatabaseAdapter.insertOrUpdateContent(eq(content), eq(false),
        eq(-1))).toReturn(1L);

    boolean saved = false;
    try {
      saved = mOfflineStorageManager.saveContent(content, null);
    } catch (MalformedURLException e) {
      Assert.fail();
    }

    Assert.assertTrue(saved);
    Mockito.verify(mMockedContentDatabaseAdapter).insertOrUpdateContent(eq(content), eq(false), eq(-1L));
    Mockito.verify(mMockedDownloadManager).saveFileFromUrl(
        eq(new URL("http://www.xamoom.com")),
        eq(false),
        any(DownloadManager.OnDownloadManagerCompleted.class));
  }

  @Test
  public void testSaveSpot() throws MalformedURLException {
    Spot spot = new Spot();
    spot.setPublicImageUrl("http://www.xamoom.com");

    Mockito.stub(mMockedSpotDatabaseAdapter.insertOrUpdateSpot(eq(spot))).toReturn(1L);

    boolean saved = false;
    try {
      saved = mOfflineStorageManager.saveSpot(spot, null);
    } catch (MalformedURLException e) {
      Assert.fail();
    }

    Assert.assertTrue(saved);
    Mockito.verify(mMockedSpotDatabaseAdapter).insertOrUpdateSpot(eq(spot));
    Mockito.verify(mMockedDownloadManager).saveFileFromUrl(
        eq(new URL("http://www.xamoom.com")),
        eq(false),
        any(DownloadManager.OnDownloadManagerCompleted.class));
  }

  @Test
  public void testSaveSystem() {
    System system = new System();
    system.setId("1");

    Mockito.stub(mMockedSystemDatabaseAdapter.insertOrUpdateSystem(eq(system))).toReturn(1L);

    boolean saved = mOfflineStorageManager.saveSystem(system);

    Assert.assertTrue(saved);
    Mockito.verify(mMockedSystemDatabaseAdapter).insertOrUpdateSystem(eq(system));
  }

  @Test
  public void testSaveStyle() {
    Style style = new Style();
    style.setId("1");

    Mockito.stub(mMockedStyleDatabaseAdapter.insertOrUpdateStyle(eq(style), anyLong())).toReturn(1L);

    boolean saved = mOfflineStorageManager.saveStyle(style);

    Assert.assertTrue(saved);
    Mockito.verify(mMockedStyleDatabaseAdapter).insertOrUpdateStyle(eq(style), eq(-1L));
  }

  @Test
  public void testSaveSetting() {
    SystemSetting setting = new SystemSetting();
    setting.setId("1");

    Mockito.stub(mMockedSettingDatabaseAdapter.insertOrUpdateSetting(eq(setting), anyLong()))
        .toReturn(1L);

    boolean saved = mOfflineStorageManager.saveSetting(setting);

    Assert.assertTrue(saved);
    Mockito.verify(mMockedSettingDatabaseAdapter).insertOrUpdateSetting(eq(setting), eq(-1L));
  }

  @Test
  public void testSaveMenu() {
    Menu menu = new Menu();
    menu.setId("1");

    Mockito.stub(mMockedMenuDatabaseAdapter.insertOrUpdate(eq(menu), anyLong()))
        .toReturn(1L);

    boolean saved = mOfflineStorageManager.saveMenu(menu);

    Assert.assertTrue(saved);
    Mockito.verify(mMockedMenuDatabaseAdapter).insertOrUpdate(eq(menu), eq(-1L));
  }

  @Test
  public void testGetContent() {
    mOfflineStorageManager.getContent("1");

    Mockito.verify(mMockedContentDatabaseAdapter).getContent(eq("1"));
  }

  @Test
  public void testGetContentWithLocationIdentifier() {
    Content content = new Content();
    content.setId("2");

    Spot spot = new Spot();
    spot.setId("1");
    spot.setContent(content);

    Mockito.stub(mMockedMarkerDatabaseAdapter.getSpotRelation(anyString())).toReturn(1L);
    Mockito.stub(mMockedSpotDatabaseAdapter.getSpot(anyLong())).toReturn(spot);

    Content savedContent = mOfflineStorageManager.getContentWithLocationIdentifier("locId");

    Assert.assertEquals(content, savedContent);
  }

  @Test
  public void testGetContentWithLocationIdentifierBeacon() {
    Content content = new Content();
    content.setId("2");

    Spot spot = new Spot();
    spot.setId("1");
    spot.setContent(content);

    Mockito.stub(mMockedMarkerDatabaseAdapter.getSpotRelation(anyString())).toReturn(1L);
    Mockito.stub(mMockedSpotDatabaseAdapter.getSpot(anyLong())).toReturn(spot);

    Content savedContent = mOfflineStorageManager.getContentWithLocationIdentifier("locId|locId");

    Mockito.verify(mMockedMarkerDatabaseAdapter).getSpotRelation(anyString(), anyString());
    Assert.assertEquals(content, savedContent);
  }

  @Test
  public void testGetContentsByLocation() throws InterruptedException {
    ArrayList<Spot> spots = new ArrayList<>();
    Spot spot = new Spot();
    spot.setLocation(new com.xamoom.android.xamoomsdk.Resource.Location(46.1, 15.2));
    spot.setId("1");

    Content content = new Content();
    content.setId("2");
    spot.setContent(content);
    spots.add(spot);

    Spot spot2 = new Spot();
    spot2.setLocation(new com.xamoom.android.xamoomsdk.Resource.Location(46.1, 15.2));
    spot2.setId("3");
    spot2.setContent(content);
    spots.add(spot2);

    final Location location = new Location("custom");
    location.setLatitude(46.1);
    location.setLongitude(15.2);

    Mockito.stub(mMockedSpotDatabaseAdapter.getAllSpots()).toReturn(spots);

    final Semaphore semaphore = new Semaphore(0);
    mOfflineStorageManager.getContentsByLocation(location, 1, null, null, new APIListCallback<List<Content>, List<Error>>() {
      @Override
      public void finished(List<Content> result, String cursor, boolean hasMore) {
        Assert.assertEquals(1, result.size());
        Assert.assertTrue(hasMore);
        Assert.assertEquals("1", cursor);

        mOfflineStorageManager.getContentsByLocation(location, 1, cursor, null, new APIListCallback<List<Content>, List<Error>>() {
          @Override
          public void finished(List<Content> result2, String cursor, boolean hasMore) {
              Assert.assertEquals(1, result2.size());
              Assert.assertFalse(hasMore);
              Assert.assertEquals("2", cursor);
              semaphore.release();
            }

          @Override
          public void error(List<Error> error) {
            Assert.fail();
          }
        });
      }

      @Override
      public void error(List<Error> error) {
        Assert.fail();
      }
    });
    semaphore.acquire();

    Mockito.verify(mMockedSpotDatabaseAdapter, times(2)).getAllSpots();
  }

  @Test
  public void testGetContentWithTags() throws InterruptedException {
    ArrayList<String> tags = new ArrayList<>();
    tags.add("tag1");

    Content content1 = new Content();
    content1.setTags(tags);
    ArrayList<Content> contents = new ArrayList<>();
    contents.add(content1);

    Mockito.stub(mMockedContentDatabaseAdapter.getAllContents()).toReturn(contents);

    final Semaphore semaphore = new Semaphore(0);
    mOfflineStorageManager
        .getContentByTags(tags, 1, null, null,
            new APIListCallback<List<Content>, List<Error>>() {
              @Override
              public void finished(List<Content> result, String cursor, boolean hasMore) {
                Assert.assertEquals(1, result.size());
                Assert.assertFalse(hasMore);
                Assert.assertEquals("1", cursor);
                semaphore.release();
              }

              @Override
              public void error(List<Error> error) {
                Assert.fail();
              }
            });
    semaphore.acquire();
  }

  @Test
  public void testSearchContentsByName() throws InterruptedException {
    Content content1 = new Content();
    ArrayList<Content> contents = new ArrayList<>();
    contents.add(content1);

    Mockito.stub(mMockedContentDatabaseAdapter.getContents(anyString())).toReturn(contents);

    final Semaphore semaphore = new Semaphore(0);
    mOfflineStorageManager
        .searchContentsByName("test", 1, null, null, new APIListCallback<List<Content>, List<Error>>() {
          @Override
          public void finished(List<Content> result, String cursor, boolean hasMore) {
            Assert.assertEquals(1, result.size());
            Assert.assertFalse(hasMore);
            Assert.assertEquals("1", cursor);
            semaphore.release();
          }

          @Override
          public void error(List<Error> error) {

          }
        });
    semaphore.acquire();

  }

  @Test
  public void testGetSpot() {
    mOfflineStorageManager.getSpot("1");

    Mockito.verify(mMockedSpotDatabaseAdapter).getSpot("1");
  }

  @Test
  public void testGetSpotsByLocation() throws InterruptedException {
    Spot spot1 = new Spot();
    spot1.setLocation(new com.xamoom.android.xamoomsdk.Resource.Location(46.6222743, 14.2619214));

    Spot spot2 = new Spot();
    spot2.setLocation(new com.xamoom.android.xamoomsdk.Resource.Location(46.6182128, 14.2610747));

    ArrayList<Spot> spots = new ArrayList<>();
    spots.add(spot1);
    spots.add(spot2);

    android.location.Location location = new android.location.Location("custom");
    location.setLatitude(46.6222743);
    location.setLongitude(14.2619214);

    Mockito.stub(mMockedSpotDatabaseAdapter.getAllSpots()).toReturn(spots);

    final Semaphore semaphore = new Semaphore(0);
    mOfflineStorageManager.getSpotsByLocation(location, 100, 10, null, null,
        null, new APIListCallback<List<Spot>, List<Error>>() {
      @Override
      public void finished(List<Spot> result, String cursor, boolean hasMore) {
        Assert.assertEquals(result.size(), 1);
        semaphore.release();
      }

      @Override
      public void error(List<Error> error) {

      }
    });
    semaphore.acquire();

    Mockito.verify(mMockedSpotDatabaseAdapter).getAllSpots();
  }

  @Test
  public void testGetSpotsWithTags() throws InterruptedException {
    Spot spot1 = new Spot();
    ArrayList<String> tags1 = new ArrayList<>();
    tags1.add("tag1");
    spot1.setTags(tags1);

    Spot spot2 = new Spot();
    ArrayList<String> tags2 = new ArrayList<>();
    tags2.add("tag1");
    tags2.add("tag2");
    spot2.setTags(tags2);

    ArrayList<Spot> spots = new ArrayList<>();
    spots.add(spot1);
    spots.add(spot2);

    Mockito.stub(mMockedSpotDatabaseAdapter.getAllSpots()).toReturn(spots);

    final Semaphore semaphore = new Semaphore(0);
    mOfflineStorageManager.getSpotsByTags(tags1, 10, null, null, null, new APIListCallback<List<Spot>, List<Error>>() {
      @Override
      public void finished(List<Spot> result, String cursor, boolean hasMore) {
        Assert.assertNotNull(result);
        Assert.assertEquals(2, result.size());
        Assert.assertFalse(hasMore);
        Assert.assertEquals("10", cursor);
        semaphore.release();
      }

      @Override
      public void error(List<Error> error) {

      }
    });
    semaphore.acquire();

    Mockito.verify(mMockedSpotDatabaseAdapter).getAllSpots();
  }

  @Test
  public void testSearchSpotsByName() throws InterruptedException {
    Spot spot1 = new Spot();
    spot1.setName("test");

    ArrayList<Spot> spots = new ArrayList<>();
    spots.add(spot1);

    Mockito.stub(mMockedSpotDatabaseAdapter.getSpots(anyString())).toReturn(spots);

    final Semaphore semaphore = new Semaphore(0);
    mOfflineStorageManager.searchSpotsByName("test", 10, null, null, null, new APIListCallback<List<Spot>, List<Error>>() {
      @Override
      public void finished(List<Spot> result, String cursor, boolean hasMore) {
        Assert.assertEquals(1, result.size());
        Assert.assertFalse(hasMore);
        Assert.assertEquals("10", cursor);
        semaphore.release();
      }

      @Override
      public void error(List<Error> error) {

      }
    });
    semaphore.acquire();

    Mockito.verify(mMockedSpotDatabaseAdapter).getSpots(eq("test"));
  }

  @Test
  public void testGetSystem() {
    System system = new System();
    system.setId("1");

    Mockito.stub(mMockedSystemDatabaseAdapter.getSystem()).toReturn(system);

    System savedSystem = mOfflineStorageManager.getSystem();

    Assert.assertEquals(system, savedSystem);
  }

  @Test
  public void testGetMenu() {
    Menu menu = new Menu();
    menu.setId("1");

    Mockito.stub(mMockedMenuDatabaseAdapter.getMenu(anyString())).toReturn(menu);

    Menu savedMenu = mOfflineStorageManager.getMenu("1");

    Assert.assertEquals(menu, savedMenu);
  }

  @Test
  public void testGetSystemSettings() {
    SystemSetting setting = new SystemSetting();
    setting.setId("1");

    Mockito.stub(mMockedSettingDatabaseAdapter.getSystemSetting(anyString())).toReturn(setting);

    SystemSetting savedSetting = mOfflineStorageManager.getSystemSetting("1");

    Assert.assertEquals(setting, savedSetting);
  }

  @Test
  public void testGetStyle() {
    Style style = new Style();
    style.setId("1");

    Mockito.stub(mMockedStyleDatabaseAdapter.getStyle(anyString())).toReturn(style);

    Style savedStyle = mOfflineStorageManager.getStyle("1");

    Assert.assertEquals(style, savedStyle);
  }
}
