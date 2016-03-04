package com.xamoom.android.xamoomsdk;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.LargeTest;

import android.support.test.runner.AndroidJUnit4;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.converter.GsonConverter;

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
    RestAdapter restAdapter = new RestAdapter.Builder()
        .setEndpoint(API_URL)
        .setLogLevel(RestAdapter.LogLevel.FULL)
        .setLog(new AndroidLog(TAG))
        .setRequestInterceptor(new RequestInterceptor() {
          @Override
          public void intercept(RequestFacade request) {
            request.addHeader("ContentAttributesMessage-Type", "application/vnd.api+json");
            request.addHeader("APIKEY", getInstrumentation().getContext().getString(R.string.APIKEY));
            request.addHeader("X-DEVKEY", getInstrumentation().getContext().getString(R.string.XDEVKEY));
          }
        })
        .setConverter(new GsonConverter(new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()))
        .build();

    api = new EnduserApi(restAdapter);
  }

  @Test
  public void testGetContent(){
    getInstrumentation().getContext().getString(R.string.APIKEY);
  }
}