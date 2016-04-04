package com.xamoom.android.xamoomsdk;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.LargeTest;

import android.support.test.runner.AndroidJUnit4;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.converter.GsonConverter;
import retrofit2.Retrofit;

import static junit.framework.Assert.assertTrue;

/**
 * Created by raphaelseher on 04/03/16.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class IntegrationTest extends InstrumentationTestCase {
  private static final String TAG = IntegrationTest.class.getSimpleName();
  private static final String API_URL = "https://xamoom-cloud-dev.appspot.com/_api/v2/consumer";

  public EnduserApi api;

  @Before
  public void setUp() {
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
    builder.addInterceptor(new Interceptor() {
      @Override
      public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
            .addHeader("ContentAttributesMessage-Type", "application/vnd.api+json")
            .addHeader("APIKEY", getInstrumentation().getContext().getResources().getString(R.string.))
            .addHeader("X-DEVKEY", getInstrumentation().getContext()..getString(R.string.XDEVKEY))
            .build();
        return chain.proceed(request);
      }
    });

    //builder.addInterceptor(loggingInterceptor);

    OkHttpClient httpClient = builder.build();

    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(API_URL)
        .client(httpClient)
        .build();

    api = new EnduserApi(retrofit);
  }

  @Test
  public void testGetContent(){
    getInstrumentation().getContext().getString(R.string.APIKEY);
  }
}