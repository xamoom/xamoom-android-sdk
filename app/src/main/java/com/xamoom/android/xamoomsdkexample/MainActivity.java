package com.xamoom.android.xamoomsdkexample;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.xamoom.android.xamoomsdk.APICallback;
import com.xamoom.android.xamoomsdk.APIListCallback;
import com.xamoom.android.xamoomsdk.ContentFlags;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.Resource.Attributes.ContentAttributesMessage;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.Error.ErrorMessage;

import java.util.EnumSet;
import java.util.List;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.converter.GsonConverter;

public class MainActivity extends AppCompatActivity {
  private static final String TAG = MainActivity.class.getSimpleName();
  private static final String API_URL = "https://xamoom-cloud-dev.appspot.com/_api/v2/consumer";

  private EnduserApi mEnduserApi;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    setupEnduserApi();

    //getContent();
    //getContentOption();
    //getContentLocationIdentifier();
    getContentsLocation();
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
    RestAdapter restAdapter = new RestAdapter.Builder()
        .setEndpoint(API_URL)
        .setLogLevel(RestAdapter.LogLevel.FULL)
        .setLog(new AndroidLog(TAG))
        .setRequestInterceptor(new RequestInterceptor() {
          @Override
          public void intercept(RequestFacade request) {
            request.addHeader("ContentAttributesMessage-Type", "application/vnd.api+json");
            request.addHeader("APIKEY", getResources().getString(R.string.APIKEY));
            request.addHeader("X-DEVKEY", getResources().getString(R.string.XDEVKEY));
          }
        })
        .setConverter(new GsonConverter(new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()))
        .build();

     mEnduserApi = new EnduserApi(restAdapter);
  }

  public void getContent() {
    mEnduserApi.getContent("e9c917086aca465eb454e38c0146428b", new APICallback<Content, ErrorMessage>() {
      @Override
      public void finished(Content content) {
        Log.v(TAG, "Content: " + content.getSystem().getID());
      }

      @Override
      public void error(ErrorMessage error) {
        Log.e(TAG, "Error: " + error);
      }
    });
  }

  public void getContentOption() {
    mEnduserApi.getContent("e5be72be162d44b189893a406aff5227", EnumSet.of(ContentFlags.PREVIEW, ContentFlags.PRIVATE), new APICallback<Content, ErrorMessage>() {
      @Override
      public void finished(Content content) {
        Log.v(TAG, "Content: " + content.getSystem().getID());
      }

      @Override
      public void error(ErrorMessage error) {

      }
    });
  }

  public void getContentLocationIdentifier() {
    mEnduserApi.getContentByLocationIdentifier(getResources().getString(R.string.qrMarker), new APICallback<Content, ErrorMessage>() {
      @Override
      public void finished(Content result) {

      }

      @Override
      public void error(ErrorMessage error) {

      }
    });
  }

  public void getContentsLocation() {
    Location location = new Location("Custom");
    location.setLatitude(46.615106);
    location.setLongitude(14.262126);
    mEnduserApi.getContentsByLocation(location, 10, null, null, new APIListCallback<List<Content>,
        ErrorMessage>() {
      @Override
      public void finished(List<Content> result, String cursor, boolean hasMore) {
        Log.v(TAG, "Content: " + result.get(0));
        Log.v(TAG, "Finished: " + cursor);
        Log.v(TAG, "Finished: " + hasMore);
      }

      @Override
      public void error(ErrorMessage error) {

      }
    });
  }
}
