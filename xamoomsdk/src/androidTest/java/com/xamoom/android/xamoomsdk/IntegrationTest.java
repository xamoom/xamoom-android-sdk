package com.xamoom.android.xamoomsdk;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.LargeTest;

import com.xamoom.android.xamoomsdk.Enums.SpotFlags;
import com.xamoom.android.xamoomsdk.Enums.SpotSortFlags;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Marker;
import com.xamoom.android.xamoomsdk.Resource.Menu;
import com.xamoom.android.xamoomsdk.Resource.Spot;
import com.xamoom.android.xamoomsdk.Resource.Style;
import com.xamoom.android.xamoomsdk.Resource.System;
import com.xamoom.android.xamoomsdk.Resource.SystemSetting;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;

@RunWith(AndroidJUnit4.class)
public class IntegrationTest extends InstrumentationTestCase {
  private static final String TAG = IntegrationTest.class.getSimpleName();

  private Context mContext;
  private MockWebServer mMockWebServer;
  private EnduserApi api;
  private String mQRMarker = "";
  private String mContentId = "";
  private String systemId = "";

  @Before
  public void setUp() {
    mMockWebServer = new MockWebServer();
    mContext = InstrumentationRegistry.getContext();

    OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
    builder.addInterceptor(new Interceptor() {
      @Override
      public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
            .addHeader("Content-Type", "application/vnd.api+json")
            .build();
        return chain.proceed(request);
      }
    });
    OkHttpClient httpClient = builder.build();

    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(mMockWebServer.url(""))
        .client(httpClient)
        .build();

    api = new EnduserApi(retrofit, mContext);
  }
  /*
  @Test
  public void testGetContent() throws Exception {
    mMockWebServer.enqueue(new MockResponse().setBody(loadJsonFile("contentbyid")));

    final CountDownLatch signal = new CountDownLatch(1);

    final List<String> checkTags = new ArrayList<>();
    checkTags.add("tests");
    checkTags.add("Wörthersee");

    final Content[] content = {null};
    api.getContent(mContentId, new APICallback<Content, List<at.rags.morpheus.Error>>() {
      @Override
      public void finished(Content result) {
        assertNotNull(result);
        assertEquals(result.getId(), "7cf2c58e6d374ce3888c32eb80be53b5");
        assertEquals(58, result.getCategory());
        assertEquals(result.getDescription(), "Testing Hub excerpt.");
        assertEquals(result.getLanguage(), "de");
        assertEquals(result.getTags(), checkTags);
        assertEquals(result.getPublicImageUrl(), "https://storage.googleapis.com/xamoom-files-dev/mobile/d8baa2bad1ce4d9da21297098ce4ff00.jpg?v=b87374bc207bd8267e5e89e439b3e475eb90b63fd974fbe338421cdd63944370e98dd495fab54ceaf3efef6b9223f7de9f44097a39c630c08a969d1545c4aa63");
        assertEquals(result.getTitle(), "APP | Testing Hub");

        assertEquals(result.getSystem().getId(), "5755996320301056");

        assertEquals(result.getContentBlocks().size(), 10);

        content[0] = result;
        signal.countDown();
      }

      @Override
      public void error(List<Error> error) {
        fail();
      }
    });

    signal.await();

    testContentBlocks(content[0].getContentBlocks());
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

  private void testContentBlocks(List<ContentBlock> blocks) throws InterruptedException {

    for (final ContentBlock contentBlock : blocks) {
      if (contentBlock.getContentId() == null) {
        continue;
      }

      final CountDownLatch signal = new CountDownLatch(1);
      api.getContent(contentBlock.getContentId(), new APICallback<Content, List<Error>>() {
        @Override
        public void finished(Content result) {
          if (result.getId().equalsIgnoreCase("5f6a24e9ec5e4090890b7911c791a0c7")) {
            assertEquals(true, result.getContentBlocks().get(0).isPublicStatus());
            assertEquals(0, result.getContentBlocks().get(0).getBlockType());
            assertEquals("Titel 2", result.getContentBlocks().get(0).getTitle());
            assertEquals("<p>Text 1</p>", result.getContentBlocks().get(0).getText());
          }

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

    assertEquals(true, blocks.get(0).isPublicStatus());
    assertEquals(0, blocks.get(0).getBlockType());
    assertEquals("Welcome to the land of testing", blocks.get(0).getTitle());
    assertEquals("<p><br></p>", blocks.get(0).getText());
  }
  */

  // helper

  private String loadJsonFile(String name) {
    int identifier = mContext.getResources().getIdentifier("a"+name, "raw",
        mContext.getPackageName());
    InputStream inputStream = mContext.getResources().
        openRawResource(identifier);

    BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
    StringBuilder total = new StringBuilder();
    String line;
    try {
      while ((line = r.readLine()) != null) {
        total.append(line).append('\n');
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return total.toString();
  }
}