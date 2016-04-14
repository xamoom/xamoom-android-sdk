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

        signal.countDown();
      }

      @Override
      public void error(List<Error> error) {
      }
    });

    signal.await();
  }


}