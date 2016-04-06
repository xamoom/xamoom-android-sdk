package com.xamoom.android.xamoomsdkexample;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.xamoom.android.xamoomcontentblocks.XamoomContentFragment;
import com.xamoom.android.xamoomsdk.APICallback;
import com.xamoom.android.xamoomsdk.APIListCallback;
import com.xamoom.android.xamoomsdk.Enums.ContentFlags;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.Resource.*;
import com.xamoom.android.xamoomsdk.Resource.System;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import at.rags.morpheus.Error;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements XamoomContentFragment.OnXamoomContentFragmentInteractionListener {
  private static final String TAG = MainActivity.class.getSimpleName();
  private static final String API_URL = "https://xamoom-cloud-dev.appspot.com/";

  private EnduserApi mEnduserApi;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    setupEnduserApi();

    getContent();
    getContentOption();
    getContentLocationIdentifier();
    getContentsLocation();
    getSpotsWithLocation();
    getSpotsWithTags();
    getSystem();
    getMenu();
    getSystemSetting();
    getStyle();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  public void setupEnduserApi() {
    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
    loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
    builder.addInterceptor(new Interceptor() {
      @Override
      public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
            .addHeader("Content-Type", "application/vnd.api+json")
            .addHeader("APIKEY", getResources().getString(R.string.APIKEY))
            .addHeader("X-DEVKEY", getResources().getString(R.string.XDEVKEY))
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

    mEnduserApi = new EnduserApi(retrofit);
  }

  public void getContent() {
    mEnduserApi.getContent("e5be72be162d44b189893a406aff5227", new APICallback<Content, List<at.rags.morpheus.Error>>() {
      @Override
      public void finished(Content result) {
        Log.v(TAG, "getContent: " + result);

        XamoomContentFragment xamoomFragment = XamoomContentFragment.newInstance("#000000", "API_KEY"); //create new instance
        xamoomFragment.setContent(result);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, xamoomFragment).commit(); //replace with xamoomFragment
      }

      @Override
      public void error(List<Error> error) {
        Log.v(TAG, "error: " + error);
      }
    });
  }

  public void getContentOption() {
    mEnduserApi.getContent("e5be72be162d44b189893a406aff5227", EnumSet.of(ContentFlags.PREVIEW, ContentFlags.PRIVATE),
        new APICallback<Content, List<Error>>() {
          @Override
          public void finished(Content result) {
            Log.v(TAG, "getContent: " + result);
          }

          @Override
          public void error(List<Error> error) {

          }
        });
  }

  public void getContentLocationIdentifier() {
    mEnduserApi.getContentByLocationIdentifier(getResources().getString(R.string.qrMarker),
        new APICallback<Content, List<Error>>() {
          @Override
          public void finished(Content result) {
            Log.v(TAG, "getContentByLocationIdentifier: " + result);
          }

          @Override
          public void error(List<Error> error) {

          }
        });
  }

  public void getContentsLocation() {
    Location location = new Location("Custom");
    location.setLatitude(46.615106);
    location.setLongitude(14.262126);
    mEnduserApi.getContentsByLocation(location, 1, null, null, new APIListCallback<List<Content>, List<Error>>() {
      @Override
      public void finished(List<Content> result, String cursor, boolean hasMore) {
        Log.v(TAG, "getContentsLocation: " + result.get(0));
        Log.v(TAG, "getContentsLocation: " + cursor);
        Log.v(TAG, "getContentsLocation: " + hasMore);
      }

      @Override
      public void error(List<Error> error) {

      }
    });
  }

  public void getSpotsWithLocation() {
    Location location = new Location("Custom");
    location.setLatitude(46.615106);
    location.setLongitude(14.262126);
    mEnduserApi.getSpotsByLocation(location, 1000, null, null, new APIListCallback<List<Spot>, List<Error>>() {
      @Override
      public void finished(List<Spot> result, String cursor, boolean hasMore) {
        Log.v(TAG, "getSpotsWithLocation: " + result.get(0));
        Log.v(TAG, "getSpotsWithLocation: " + cursor);
        Log.v(TAG, "getSpotsWithLocation: " + hasMore);
      }

      @Override
      public void error(List<Error> error) {

      }
    });
  }

  public void getSpotsWithTags() {
    List<String> tags = new ArrayList<>();
    tags.add("tag1");
    mEnduserApi.getSpotsByTags(tags, 0, null, null, null, new APIListCallback<List<Spot>, List<Error>>() {
      @Override
      public void finished(List<Spot> result, String cursor, boolean hasMore) {
        Log.v(TAG, "getSpotsWithTags: " + result.get(0));
        Log.v(TAG, "getSpotsWithTags: " + cursor);
        Log.v(TAG, "getSpotsWithTags: " + hasMore);
      }

      @Override
      public void error(List<Error> error) {

      }
    });
  }

  public void getSystem() {
    mEnduserApi.getSystem(new APICallback<com.xamoom.android.xamoomsdk.Resource.System, List<Error>>() {
      @Override
      public void finished(System result) {
        Log.v(TAG, "getSystem: " + result.getId());
      }

      @Override
      public void error(List<Error> error) {

      }
    });
  }

  public void getMenu() {
    mEnduserApi.getMenu("5755996320301056", new APICallback<com.xamoom.android.xamoomsdk.Resource.Menu, List<Error>>() {
      @Override
      public void finished(com.xamoom.android.xamoomsdk.Resource.Menu result) {
        Log.v(TAG, "getMenu: " + result.getItems());
      }

      @Override
      public void error(List<Error> error) {

      }
    });
  }

  public void getSystemSetting() {
    mEnduserApi.getSystemSetting("5755996320301056", new APICallback<SystemSetting, List<Error>>() {
      @Override
      public void finished(SystemSetting result) {
        Log.v(TAG, "getSystemSetting: " + result);
      }

      @Override
      public void error(List<Error> error) {

      }
    });
  }

  public void getStyle() {
    mEnduserApi.getStyle("5755996320301056", new APICallback<Style, List<Error>>() {
      @Override
      public void finished(Style result) {
        Log.v(TAG, "getStyle: " + result);
      }

      @Override
      public void error(List<Error> error) {

      }
    });
  }

  @Override
  public void clickedContentBlock(Content content) {
    Log.v(TAG, "Click Content: " + content);
  }

  @Override
  public void clickedSpotMapContentLink(String contentId) {
    Log.v(TAG, "Click Spot: " + contentId);

  }
}
