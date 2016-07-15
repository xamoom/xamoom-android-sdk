package com.xamoom.android.xamoomsdk;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.LargeTest;

import com.xamoom.android.xamoomsdk.Enums.SpotFlags;
import com.xamoom.android.xamoomsdk.Enums.SpotSortFlags;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.Marker;
import com.xamoom.android.xamoomsdk.Resource.Menu;
import com.xamoom.android.xamoomsdk.Resource.Spot;
import com.xamoom.android.xamoomsdk.Resource.Style;
import com.xamoom.android.xamoomsdk.Resource.System;
import com.xamoom.android.xamoomsdk.Resource.SystemSetting;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import at.rags.morpheus.Error;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class IntegrationTest extends InstrumentationTestCase {
  private static final String TAG = IntegrationTest.class.getSimpleName();
  private static final String API_URL = "https://xamoom-cloud-dev.appspot.com/";

  private EnduserApi api;
  private String mQRMarker;
  private String mContentId;

  @Before
  public void setUp() {
    OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
    builder.addInterceptor(new Interceptor() {
      @Override
      public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
            .addHeader("Content-Type", "application/vnd.api+json")
            .addHeader("APIKEY", InstrumentationRegistry.getContext().getResources().getString(R.string.APIKEY))
            .addHeader("X-DEVKEY", InstrumentationRegistry.getContext().getString(R.string.XDEVKEY))
            .build();
        return chain.proceed(request);
      }
    });
    OkHttpClient httpClient = builder.build();

    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(API_URL)
        .client(httpClient)
        .build();

    api = new EnduserApi(retrofit);

    mQRMarker = InstrumentationRegistry.getContext().getResources().getString(R.string.qrMarker);
    mContentId =  InstrumentationRegistry.getContext().getResources().getString(R.string.contentID);
  }

  @Test
  public void testGetContent() throws Exception {
    final CountDownLatch signal = new CountDownLatch(1);

    final List<String> checkTags = new ArrayList<>();
    checkTags.add("tag1");
    checkTags.add("tag2");
    checkTags.add("tag3");

    api.getContent(mContentId, new APICallback<Content, List<at.rags.morpheus.Error>>() {
      @Override
      public void finished(Content result) {
        assertNotNull(result);
        assertEquals(result.getId(), "e9c917086aca465eb454e38c0146428b");
        assertEquals(result.getCategory(), 76);
        assertEquals(result.getDescription(), "Test");
        assertEquals(result.getLanguage(), "de");
        assertEquals(result.getTags(), checkTags);
        assertEquals(result.getPublicImageUrl(), "https://storage.googleapis.com/xamoom-files-dev/mobile/e7f670906b464ea58352d90d3c4674fa.jpg?v=dc8c0f42a508b67a3f95b2f84db798fe43181c51df9bb3797f4f39f4adab64800a7df9fc2d8938d9303708afae9ce5e24e6eea065b95e60cfe94d422a3cdffbc");
        assertEquals(result.getTitle(), "DO NOT TOUCH | APP | Testsite 1");

        assertEquals(result.getSystem().getId(), "5755996320301056");

        assertEquals(result.getContentBlocks().size(), 10);

        assertEquals(result.getContentBlocks().get(0).isPublicStatus(), true);
        assertEquals(result.getContentBlocks().get(0).getBlockType(), 0);
        assertEquals(result.getContentBlocks().get(0).getTitle(), "Test");
        assertEquals(result.getContentBlocks().get(0).getText(), "<p>Test</p>");

        assertEquals(result.getContentBlocks().get(1).getTitle(), "Test");
        assertEquals(result.getContentBlocks().get(1).getArtists(), "Test");
        assertEquals(result.getContentBlocks().get(1).isPublicStatus(), true);
        assertEquals(result.getContentBlocks().get(1).getBlockType(), 1);
        assertEquals(result.getContentBlocks().get(1).getFileId(),
            "https://storage.googleapis.com/xamoom-files-dev/612f64221fd34ac283cb1c5ecc4c18f8.mp3");

        assertEquals(result.getContentBlocks().get(2).getTitle(), "Test");
        assertEquals(result.getContentBlocks().get(2).isPublicStatus(), true);
        assertEquals(result.getContentBlocks().get(2).getBlockType(), 7);
        assertEquals(result.getContentBlocks().get(2).getSoundcloudUrl(),
            "https://soundcloud.com/lukasgraham/7-years");

        assertEquals(result.getContentBlocks().get(3).getTitle(), "Test");
        assertEquals(result.getContentBlocks().get(3).isPublicStatus(), true);
        assertEquals(result.getContentBlocks().get(3).getBlockType(), 2);
        assertEquals(result.getContentBlocks().get(3).getVideoUrl(),
            "https://www.youtube.com/watch?v=IxwU-h_h8Ls");

        assertEquals(result.getContentBlocks().get(4).getTitle(), "Test");
        assertEquals(result.getContentBlocks().get(4).getText(), "Test");
        assertEquals(result.getContentBlocks().get(4).isPublicStatus(), true);
        assertEquals(result.getContentBlocks().get(4).getBlockType(), 4);
        assertEquals(result.getContentBlocks().get(4).getLinkType(), 3);
        assertEquals(result.getContentBlocks().get(4).getLinkUrl(), "http://www.xamoom.com");

        assertEquals(result.getContentBlocks().get(5).getTitle(), "Test");
        assertEquals(result.getContentBlocks().get(5).getAltText(), "Test");
        assertEquals(result.getContentBlocks().get(5).isPublicStatus(), true);
        assertEquals(result.getContentBlocks().get(5).getScaleX(), 100.0);
        assertEquals(result.getContentBlocks().get(5).getBlockType(), 3);
        assertEquals(result.getContentBlocks().get(5).getFileId(),
            "https://storage.googleapis.com/xamoom-files-dev/mobile/e7f670906b464ea58352d90d3c4674fa.jpg");
        assertEquals(result.getContentBlocks().get(5).getLinkUrl(), "http://www.xamoom.com");

        assertEquals(result.getContentBlocks().get(6).getTitle(), "Test");
        assertEquals(result.getContentBlocks().get(6).getArtists(), "Test");
        assertEquals(result.getContentBlocks().get(6).isPublicStatus(), true);
        assertEquals(result.getContentBlocks().get(6).getBlockType(), 5);
        assertEquals(result.getContentBlocks().get(6).getFileId(),
            "https://storage.googleapis.com/xamoom-files-dev/c4970ddbcb9b471da3c15ed7b0087bff.epub");

        assertEquals(result.getContentBlocks().get(7).isPublicStatus(), true);
        assertEquals(result.getContentBlocks().get(7).getBlockType(), 6);
        assertEquals(result.getContentBlocks().get(7).getContentId(),
            "e5be72be162d44b189893a406aff5227");

        assertEquals(result.getContentBlocks().get(8).getTitle(), "Test");
        assertEquals(result.getContentBlocks().get(8).getText(), "Test");
        assertEquals(result.getContentBlocks().get(8).isPublicStatus(), true);
        assertEquals(result.getContentBlocks().get(8).getBlockType(), 8);
        assertEquals(result.getContentBlocks().get(8).getDownloadType(), 0);
        assertEquals(result.getContentBlocks().get(8).getFileId(),
            "https://storage.googleapis.com/xamoom-files-dev/108bfa6dd489437fb1f5c0613e457b53.vcf");

        ArrayList<String> checkTag = new ArrayList<String>();
        checkTag.add("spot1");

        assertEquals(result.getContentBlocks().get(9).getTitle(), "Test");
        assertEquals(result.getContentBlocks().get(9).isPublicStatus(), true);
        assertEquals(result.getContentBlocks().get(9).getBlockType(), 9);
        assertEquals(result.getContentBlocks().get(9).getSpotMapTags(), checkTag);
        assertEquals(result.getContentBlocks().get(9).isShowContentOnSpotmap(), true);

        signal.countDown();
      }

      @Override
      public void error(List<Error> error) {
      }
    });

    signal.await();
  }

  @Test
  public void testContentWithLocationIdentifier() throws InterruptedException {
    final CountDownLatch signal = new CountDownLatch(1);

    api.getContentByLocationIdentifier(mQRMarker, new APICallback<Content, List<Error>>() {
      @Override
      public void finished(Content result) {
        assertEquals(result.getTitle(), "DO NOT TOUCH | APP | Testsite 1");
        signal.countDown();
      }

      @Override
      public void error(List<Error> error) {
        fail();
        signal.countDown();
      }
    });

    signal.await();
  }

  @Test
  public void testContentWithBeacon() throws InterruptedException {
    final CountDownLatch signal = new CountDownLatch(1);

    api.getContentByBeacon(54222, 24265, new APICallback<Content, List<Error>>() {
      @Override
      public void finished(Content result) {
        assertEquals(result.getTitle(), "DO NOT TOUCH | APP | Testsite 1");
        signal.countDown();
      }

      @Override
      public void error(List<Error> error) {
        fail();
        signal.countDown();
      }
    });

    signal.await();
  }

  @Test
  public void testSearchContent() throws InterruptedException {
    final CountDownLatch signal = new CountDownLatch(1);

    api.searchContentByName("do not touch", 10, null, null, new APIListCallback<List<Content>, List<Error>>() {
      @Override
      public void finished(List<Content> result, String cursor, boolean hasMore) {
        assertEquals(result.get(0).getTitle(), "DO NOT TOUCH | APP | Testsite 1");
        signal.countDown();
      }

      @Override
      public void error(List<Error> error) {
        fail();
        signal.countDown();
      }
    });

    signal.await();
  }

  @Test
  public void testSpot() throws InterruptedException {
    final CountDownLatch signal = new CountDownLatch(1);

    api.getSpot("5755996320301056|5744440375246848", new APICallback<Spot, List<Error>>() {
      @Override
      public void finished(Spot result) {
        assertEquals(result.getName(), "DO NOT TOUCH | APP | Spot 1");
        signal.countDown();
      }

      @Override
      public void error(List<Error> error) {
        fail();
        signal.countDown();
      }
    });

    signal.await();
  }

  @Test
  public void testSpotsWithTag() throws InterruptedException {
    final CountDownLatch signal = new CountDownLatch(1);
    final ArrayList<String> tags = new ArrayList<>();
    tags.add("Spot1");
    //tags.add("tag1");
    //tags.add("donottouchspot");

    api.getSpotsByTags(tags, EnumSet.of(SpotFlags.INCLUDE_CONTENT, SpotFlags.INCLUDE_MARKERS),
        null, new APIListCallback<List<Spot>, List<Error>>() {
      @Override
      public void finished(List<Spot> result, String cursor, boolean hasMore) {
        assertFalse(hasMore);
        assertEquals(cursor, "");

        Spot spot = result.get(1);
        assertEquals(spot.getName(), "DO NOT TOUCH | APP | Spot 1");
        assertEquals(spot.getDescription(), "Test");
        assertNotNull(spot.getPublicImageUrl());
        assertEquals(spot.getLocation().getLatitude(), 46.61506789671181);
        assertEquals(spot.getLocation().getLongitude(), 14.2622709274292);

        ArrayList<String> checktags = new ArrayList<>();
        checktags.add("Spot1");
        checktags.add("tag1");
        checktags.add("donottouchspot");

        assertEquals(spot.getTags(), checktags);
        assertEquals(spot.getSystem().getId(), "5755996320301056");
        assertEquals(spot.getContent().getId(), "e9c917086aca465eb454e38c0146428b");

        Marker marker = spot.getMarkers().get(0);
        assertEquals(marker.getNfc(), "nbt7qa4on0sy");
        assertEquals(marker.getQr(), "7qpqr");
        assertEquals(marker.getBeaconUUID(), "de2b94ae-ed98-11e4-3432-78616d6f6f6d");
        assertEquals(marker.getBeaconMajor(), "54222");
        assertEquals(marker.getBeaconMinor(), "24265");
        assertEquals(marker.getEddystoneUrl(), "dev.xm.gl/2134hs");

        signal.countDown();
      }

      @Override
      public void error(List<Error> error) {
        fail();
        signal.countDown();
      }
    });

    signal.await();
  }

  @Test
  public void testSystem() throws InterruptedException {
    final CountDownLatch signal = new CountDownLatch(1);

    api.getSystem(new APICallback<System, List<Error>>() {
      @Override
      public void finished(System result) {
        assertEquals(result.getName(), "Dev xamoom testing environment");
        assertEquals(result.getUrl(), "http://testpavol.at");
        assertEquals(result.getSystemSetting().getId(), "5755996320301056");
        assertEquals(result.getMenu().getId(), "5755996320301056");
        assertEquals(result.getStyle().getId(), "5755996320301056");

        signal.countDown();
      }

      @Override
      public void error(List<Error> error) {
        fail();
        signal.countDown();
      }
    });

    signal.await();
  }

  @Test
  public void testSystemGerman() throws InterruptedException {
    final CountDownLatch signal = new CountDownLatch(1);

    api.setLanguage("de");
    api.getSystem(new APICallback<System, List<Error>>() {
      @Override
      public void finished(System result) {
        assertEquals(result.getName(), "Dev xamoom testing UMGEBUNG");

        signal.countDown();
      }

      @Override
      public void error(List<Error> error) {
        fail();
        signal.countDown();
      }
    });

    signal.await();
  }

  @Test
  public void testSystemSettings() throws InterruptedException {
    final CountDownLatch signal = new CountDownLatch(1);

    api.getSystemSetting("5755996320301056", new APICallback<SystemSetting, List<Error>>() {
      @Override
      public void finished(SystemSetting result) {
        assertEquals(result.getId(), "5755996320301056");
        assertEquals(result.getItunesAppId(), "998504165");
        assertEquals(result.getGooglePlayAppId(), "com.skype.raider");
        signal.countDown();
      }

      @Override
      public void error(List<Error> error) {
        fail();
        signal.countDown();
      }
    });

    signal.await();
  }

  @Test
  public void testMenu() throws InterruptedException {
    final CountDownLatch signal = new CountDownLatch(1);

    api.getMenu("5755996320301056", new APICallback<Menu, List<Error>>() {
      @Override
      public void finished(Menu result) {
        assertEquals(result.getId(), "5755996320301056");
        assertTrue(result.getItems().size() > 0);
        signal.countDown();
      }

      @Override
      public void error(List<Error> error) {
        fail();
        signal.countDown();
      }
    });

    signal.await();
  }

  @Test
  public void testStyle() throws InterruptedException {
    final CountDownLatch signal = new CountDownLatch(1);

    api.getStyle("5755996320301056", new APICallback<Style, List<Error>>() {
      @Override
      public void finished(Style result) {
        assertEquals(result.getId(), "5755996320301056");
        assertNotNull(result.getChromeHeaderColor());
        assertNotNull(result.getBackgroundColor());
        assertNotNull(result.getForegroundFontColor());
        assertNotNull(result.getHighlightFontColor());
        assertNotNull(result.getIcon());
        assertNotNull(result.getCustomMarker());
        signal.countDown();
      }

      @Override
      public void error(List<Error> error) {
        fail();
        signal.countDown();
      }
    });

    signal.await();
  }
}