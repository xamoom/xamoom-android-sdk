package com.xamoom.android.xamoomsdk;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;

import com.xamoom.android.xamoomsdk.Enums.SpotFlags;
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
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
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

  @Test
  public void testGetContent() throws Exception {
    mMockWebServer.enqueue(new MockResponse().setBody(loadJsonFile("contentbyid")));

    final CountDownLatch signal = new CountDownLatch(1);

    final List<String> checkTags = new ArrayList<>();
    checkTags.add("tests");
    checkTags.add("Wörthersee");

    final HashMap<String, String> checkCustomMeta = new HashMap<>();
    checkCustomMeta.put("test-key", "test-value");
    checkCustomMeta.put("test-key2", "test-value2");

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
        assertEquals(result.getCustomMeta(), checkCustomMeta);

        assertEquals(result.getSystem().getId(), "5755996320301056");

        assertEquals(result.getContentBlocks().size(), 10);

        content[0] = result;
        signal.countDown();
      }

      @Override
      public void error(List<Error> error) {
        fail();
        signal.countDown();
      }
    });

    signal.await();

    testContentBlocks(content[0].getContentBlocks());
  }

  @Test
  public void testContentWithLocationIdentifier() throws InterruptedException {
    mMockWebServer.enqueue(new MockResponse().setBody(loadJsonFile("contentbyqr")));

    final Content[] content = {null};

    final CountDownLatch signal = new CountDownLatch(1);
    api.getContentByLocationIdentifier(mQRMarker, new APICallback<Content, List<Error>>() {
      @Override
      public void finished(Content result) {
        assertNotNull(result);
        content[0] = result;
        signal.countDown();
      }

      @Override
      public void error(List<Error> error) {
        fail();
        signal.countDown();
      }
    });
    signal.await();

    testContentBlocks(content[0].getContentBlocks());
  }

  @Test
  public void testContentWithBeacon() throws InterruptedException {
    mMockWebServer.enqueue(new MockResponse().setBody(loadJsonFile("contentbyqr")));

    final Content[] content = {null};

    final CountDownLatch signal = new CountDownLatch(1);
    api.getContentByBeacon(54222, 24265, new APICallback<Content, List<Error>>() {
      @Override
      public void finished(Content result) {
        assertNotNull(result);
        content[0] = result;
        signal.countDown();
      }

      @Override
      public void error(List<Error> error) {
        fail();
        signal.countDown();
      }
    });
    signal.await();

    testContentBlocks(content[0].getContentBlocks());
  }

  @Test
  public void testSpotWithId() throws InterruptedException {
    mMockWebServer.enqueue(new MockResponse().setBody(loadJsonFile("spotbyid")));

    final HashMap<String, String> checkCustomMeta = new HashMap<>();
    checkCustomMeta.put("test-key", "test-value");
    checkCustomMeta.put("test-key2", "test-value2");

    final CountDownLatch signal = new CountDownLatch(1);

    api.getSpot("5755996320301056|5673385510043648", new APICallback<Spot, List<Error>>() {
      @Override
      public void finished(Spot result) {
        assertEquals(result.getCustomMeta(), checkCustomMeta);
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
    mMockWebServer.enqueue(new MockResponse().setBody(loadJsonFile("spotbytags")));

    final ArrayList<String> tags = new ArrayList<>();
    tags.add("Spot1");

    final CountDownLatch signal = new CountDownLatch(1);
    api.getSpotsByTags(tags, EnumSet.of(SpotFlags.INCLUDE_CONTENT, SpotFlags.INCLUDE_MARKERS),
        null, new APIListCallback<List<Spot>, List<Error>>() {
          @Override
          public void finished(List<Spot> result, String cursor, boolean hasMore) {
            assertFalse(hasMore);
            assertEquals(cursor, "");

            Spot spot = result.get(0);
            assertEquals("APP | Spot 1", spot.getName());
            assertEquals("Thats the spot excerpt.", spot.getDescription());
            assertEquals("https://storage.googleapis.com/xamoom-files-dev/mobile/9bee8ab135bc41f392aab58dff07a8de.jpg?v=2cd9bbdc0d25b399deec11fcfd97b89614774102229b844662f08889de50f4d5ead08b76dc78c0124e7ef90e0023f96127fcab6a53bc1f87cdee24b25a576819",
                spot.getPublicImageUrl());
            assertEquals(46.615031299999998, spot.getLocation().getLatitude());
            assertEquals(14.261887000000002, spot.getLocation().getLongitude());

            ArrayList<String> checktags = new ArrayList<>();
            checktags.add("spot1");
            checktags.add("allSpots");
            checktags.add("Wörthersee");

            assertEquals(checktags, spot.getTags());
            assertEquals("5755996320301056", spot.getSystem().getId());

            Marker marker = spot.getMarkers().get(0);
            assertEquals("0c0horvyze9d", marker.getNfc());
            assertEquals("b5v2p", marker.getQr());
            assertEquals("de2b94ae-ed98-11e4-3432-78616d6f6f6d", marker.getBeaconUUID());
            assertEquals("8843", marker.getBeaconMajor());
            assertEquals("29521", marker.getBeaconMinor());
            assertEquals("dev.xm.gl/32lf0x", marker.getEddystoneUrl());

            checktags.clear();
            checktags.add("tests");
            checktags.add("Wörthersee");

            Content content = spot.getContent();
            assertEquals("APP | Testing Hub", content.getTitle());
            assertEquals("Testing Hub excerpt.", content.getDescription());
            assertEquals("https://storage.googleapis.com/xamoom-files-dev/mobile/d8baa2bad1ce4d9da21297098ce4ff00.jpg?v=e3450d7de94f20b0d89fdfe328a254478ef7f193211faa33e6db888f1f92f4bc5dc487da226baefac66209182971cacbe1b63be9c6551854e4d04da58b5465d9",
                content.getPublicImageUrl());
            assertEquals(58, content.getCategory());
            assertEquals(checktags, content.getTags());
            assertEquals(10, content.getContentBlocks().size());

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
    mMockWebServer.enqueue(new MockResponse().setBody(loadJsonFile("system")));

    final CountDownLatch signal = new CountDownLatch(1);
    api.getSystem(new APICallback<System, List<Error>>() {
      @Override
      public void finished(System result) {
        assertEquals(result.getName(), "Dev xamoom testing UMGEBUNG");
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
  public void testSystemSettings() throws InterruptedException {
    mMockWebServer.enqueue(new MockResponse().setBody(loadJsonFile("setting")));

    final CountDownLatch signal = new CountDownLatch(1);
    api.getSystemSetting("5755996320301056", new APICallback<SystemSetting, List<Error>>() {
      @Override
      public void finished(SystemSetting result) {
        assertEquals("5755996320301056", result.getId());
        assertEquals("998504165", result.getItunesAppId());
        assertEquals("com.xamoom.android.xamoom_pingeborg_android", result.getGooglePlayAppId());
        signal.countDown();
      }

      @Override
      public void error(List<Error> error) {
        fail();
        signal.countDown();
      }
    });
  }

  @Test
  public void testMenu() throws InterruptedException {
    mMockWebServer.enqueue(new MockResponse().setBody(loadJsonFile("menu")));

    final CountDownLatch signal = new CountDownLatch(1);
    api.getMenu("5755996320301056", new APICallback<Menu, List<Error>>() {
      @Override
      public void finished(Menu result) {
        assertEquals("5755996320301056", result.getId());
        assertEquals(3, result.getItems().size());

        assertEquals("Testseite", result.getItems().get(0).getTitle());
        assertEquals("APP | Testing Hub", result.getItems().get(1).getTitle());
        assertEquals("APP | Test Spotmap", result.getItems().get(2).getTitle());

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
    mMockWebServer.enqueue(new MockResponse().setBody(loadJsonFile("style")));

    final CountDownLatch signal = new CountDownLatch(1);
    api.getStyle("5755996320301056", new APICallback<Style, List<Error>>() {
      @Override
      public void finished(Style result) {
        assertEquals("#ffee00", result.getChromeHeaderColor());
        assertEquals("#f2f2f2", result.getBackgroundColor());
        assertEquals("#222222", result.getForegroundFontColor());
        assertEquals("#d6220c", result.getHighlightFontColor());
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

      mMockWebServer.enqueue(new MockResponse()
          .setBody(loadJsonFile(contentBlock.getContentId())));

      final CountDownLatch signal = new CountDownLatch(1);
      api.getContent(contentBlock.getContentId(), new APICallback<Content, List<Error>>() {
        @Override
        public void finished(Content result) {
          if (result.getId().equalsIgnoreCase("5f6a24e9ec5e4090890b7911c791a0c7")) {
            assertEquals(true, result.getContentBlocks().get(0).isPublicStatus());
            assertEquals(0, result.getContentBlocks().get(0).getBlockType());
            assertEquals("Titel 1", result.getContentBlocks().get(0).getTitle());
            assertEquals("<p>Text 1</p>", result.getContentBlocks().get(0).getText());
          }

          if (result.getId().equalsIgnoreCase("0737f96b520645cab6d71242cd43cdad")) {
            assertEquals(true, result.getContentBlocks().get(0).isPublicStatus());
            assertEquals(3, result.getContentBlocks().get(0).getBlockType());
            assertEquals("Image 1 | 100%", result.getContentBlocks().get(0).getTitle());
            assertEquals("https://storage.googleapis.com/xamoom-files-dev/mobile/62a7d76c4a14446bbc8e3155b8db025f.png",
                result.getContentBlocks().get(0).getFileId());
            assertEquals(100.0, result.getContentBlocks().get(0).getScaleX());
          }

          if (result.getId().equalsIgnoreCase("1d4f6152baa5418098d12cbf14e20275")) {
            assertEquals(true, result.getContentBlocks().get(0).isPublicStatus());
            assertEquals(5, result.getContentBlocks().get(0).getBlockType());
            assertEquals("Book 1| Epub", result.getContentBlocks().get(0).getTitle());
            assertEquals("https://storage.googleapis.com/xamoom-files-dev/10d652eeed9849d1bd6cb9168c69eecd.epub",
                result.getContentBlocks().get(0).getFileId());
            assertEquals("Book 1 Text", result.getContentBlocks().get(0).getArtists());
          }

          if (result.getId().equalsIgnoreCase("1d966300bb304a199a7ba5d9ff295269")) {
            assertEquals(true, result.getContentBlocks().get(0).isPublicStatus());
            assertEquals(8, result.getContentBlocks().get(0).getBlockType());
            assertEquals("VCard", result.getContentBlocks().get(0).getTitle());
            assertEquals("VCard Description", result.getContentBlocks().get(0).getText());
            assertEquals("https://storage.googleapis.com/xamoom-files-dev/862be8b5c7854986bb4cf9269e4fa0b9.vcf",
                result.getContentBlocks().get(0).getFileId());
          }

          if (result.getId().equalsIgnoreCase("2570fd0d2a0a48c39112bc9913461f5d")) {
            assertEquals(true, result.getContentBlocks().get(0).isPublicStatus());
            assertEquals(4, result.getContentBlocks().get(0).getBlockType());
            assertEquals("Amazon", result.getContentBlocks().get(0).getTitle());
            assertEquals("Amazon Text", result.getContentBlocks().get(0).getText());
            assertEquals("http://www.amazon.de/Stupid-Hobbit-Gedruckt-Frauen-T-Shirt/dp/B013PHYR5S/ref=sr_1_15?ie=UTF8&qid=1452180457&sr=8-15&keywords=Stupid",
                result.getContentBlocks().get(0).getLinkUrl());
          }

          if (result.getId().equalsIgnoreCase("2dacd9944946484b9df2b822c475a90c")) {
            assertEquals(true, result.getContentBlocks().get(0).isPublicStatus());
            assertEquals(1, result.getContentBlocks().get(0).getBlockType());
            assertEquals("Audio 1 | mp3", result.getContentBlocks().get(0).getTitle());
            assertEquals("Artist", result.getContentBlocks().get(0).getArtists());
            assertEquals("https://storage.googleapis.com/xamoom-files-dev/93b258c0c2d543759471cec6f102118d.mp3",
                result.getContentBlocks().get(0).getFileId());
          }

          if (result.getId().equalsIgnoreCase("3092bce1dbef409c8943ab43afa2c938")) {
            ArrayList<String> checkTags = new ArrayList<String>();
            checkTags.add("spot1");
            assertEquals(true, result.getContentBlocks().get(0).isPublicStatus());
            assertEquals(9, result.getContentBlocks().get(0).getBlockType());
            assertEquals("TestMap | TAG SPOT1", result.getContentBlocks().get(0).getTitle());
            assertEquals(checkTags, result.getContentBlocks().get(0).getSpotMapTags());
            assertEquals(true, result.getContentBlocks().get(0).isShowContentOnSpotmap());
          }

          if (result.getId().equalsIgnoreCase("49c8f22408b047598c2b00507aed04db")) {
            assertEquals(true, result.getContentBlocks().get(0).isPublicStatus());
            assertEquals(2, result.getContentBlocks().get(0).getBlockType());
            assertEquals("Youtube Video 1", result.getContentBlocks().get(0).getTitle());
            assertEquals("https://www.youtube.com/watch?v=dtm_tIkEbMc",
                result.getContentBlocks().get(0).getVideoUrl());
          }

          if (result.getId().equalsIgnoreCase("5f6a24e9ec5e4090890b7911c791a0c7")) {
            assertEquals(true, result.getContentBlocks().get(0).isPublicStatus());
            assertEquals(0, result.getContentBlocks().get(0).getBlockType());
            assertEquals("Titel 1", result.getContentBlocks().get(0).getTitle());
            assertEquals("<p>Text 1</p>", result.getContentBlocks().get(0).getText());
          }

          if (result.getId().equalsIgnoreCase("6b4102f6fd0c40eba7398e4012069d1d")) {
            assertEquals(true, result.getContentBlocks().get(0).isPublicStatus());
            assertEquals(7, result.getContentBlocks().get(0).getBlockType());
            assertEquals("Soundcloud 1", result.getContentBlocks().get(0).getTitle());
            assertEquals("https://soundcloud.com/istdochmiregal/ok-kid-verschwende-mich",
                result.getContentBlocks().get(0).getSoundcloudUrl());
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