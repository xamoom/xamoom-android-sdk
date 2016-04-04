package com.xamoom.android.xamoomsdk;
import android.support.test.InstrumentationRegistry;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.LargeTest;

import android.support.test.runner.AndroidJUnit4;

import com.xamoom.android.xamoomsdk.Enums.SpotFlags;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.Spot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

import at.rags.morpheus.*;
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
  }

  @Test
  public void testGetContent() throws Exception {
    final CountDownLatch signal = new CountDownLatch(1);

    final List<String> checkTags = new ArrayList<>();
    checkTags.add("tag1");
    checkTags.add("tag2");
    checkTags.add("tag3");

    api.getContent("e9c917086aca465eb454e38c0146428b", new APICallback<Content, List<at.rags.morpheus.Error>>() {
      @Override
      public void finished(Content result) {
        assertNotNull(result);
        assertEquals(result.getCategory(), 0);
        assertEquals(result.getDescription(), "Test");
        assertEquals(result.getLanguage(), "de");
        assertEquals(result.getTags(), checkTags);
        assertEquals(result.getPublicImageUrl(), "https://storage.googleapis.com/xamoom-files-dev/mobile/e7f670906b464ea58352d90d3c4674fa.jpg?v=2c4043bffd9384096b1e89c62be25c5ee1e3c0af83a59dee83498a9738f0954900db1e36c86e8f2893bcb4608780925eb82ecd109b8325cd3ac940087e1e655d");
        assertEquals(result.getTitle(), "DO NOT TOUCH | APP | Testsite 1");

        assertEquals(result.getSystem().getId(), "5755996320301056");

        assertEquals(result.getContentBlocks().size(), 10);

        assertEquals(result.getContentBlocks().get(0).isPublicStatus(), true);
        assertEquals(result.getContentBlocks().get(0).getBlockType(), 0);
        assertEquals(result.getContentBlocks().get(0).getTitle(), "Test");
        assertEquals(result.getContentBlocks().get(0).getText(), "<p>Test</p>");
        //assertEquals(result.getContentBlocks().get(0).getId(), "e9c917086aca465eb454e38c0146428b");
        signal.countDown();
      }

      @Override
      public void error(List<Error> error) {
      }
    });

    signal.await();
  }
  /*
  @Test
  public void testGetContentLocationIdentifier() throws Exception {
    final Semaphore semaphore = new Semaphore(1);

    api.getContentByLocationIdentifier(InstrumentationRegistry.getContext().getString(R.string.qrMarker), new APICallback<Content, List<Error>>() {
      @Override
      public void finished(Content result) {
        assertEquals(result.getId(), InstrumentationRegistry.getContext().getString(R.string.contentID));
        assertEquals(result.getTitle(), "DO NOT TOUCH | APP | Testsite 1");
        semaphore.release();
      }

      @Override
      public void error(List<Error> error) {
      }
    });
    semaphore.acquire();
  }

  @Test
  public void testGetSpotsWithTags() throws Exception {
    final List<String> tags = new ArrayList<>();
    tags.add("tag1");
    tags.add("Spot1");
    tags.add("donottouchspot");

    final CountDownLatch signal = new CountDownLatch(1);
    final List<Spot>[] checkSpots = new List[]{null};


    api.getSpotsByTags(tags, EnumSet.of(SpotFlags.INCLUDE_CONTENT, SpotFlags.INCLUDE_MARKERS), null, new APIListCallback<List<Spot>, List<Error>>() {
      @Override
      public void finished(List<Spot> result, String cursor, boolean hasMore) {
        checkSpots[0] = result;

        signal.countDown();
      }

      @Override
      public void error(List<Error> error) {

      }
    });

    signal.await();

    assertEquals(checkSpots[0].size(), 100);
  }
  */
}