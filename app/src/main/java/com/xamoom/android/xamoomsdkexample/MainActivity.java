/*
 * Copyright 2017 by xamoom GmbH <apps@xamoom.com>
 *
 * This file is part of some open source application.
 *
 * Some open source application is free software: you can redistribute
 * it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either
 * version 2 of the License, or (at your option) any later version.
 *
 * Some open source application is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with xamoom-android-sdk. If not, see <http://www.gnu.org/licenses/>.
 *
 * author: Raphael Seher <raphael@xamoom.com>
 */

package com.xamoom.android.xamoomsdkexample;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.mapbox.mapboxsdk.Mapbox;
import com.xamoom.android.xamoomcontentblocks.XamoomContentFragment;
import com.xamoom.android.xamoomsdk.APICallback;
import com.xamoom.android.xamoomsdk.APIListCallback;
import com.xamoom.android.xamoomsdk.APIPasswordCallback;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.Enums.ContentFlags;
import com.xamoom.android.xamoomsdk.Enums.SpotFlags;
import com.xamoom.android.xamoomsdk.Filter;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.Spot;
import com.xamoom.android.xamoomsdk.Resource.Style;
import com.xamoom.android.xamoomsdk.Resource.System;
import com.xamoom.android.xamoomsdk.Resource.SystemSetting;
import com.xamoom.android.xamoomsdk.Storage.Database.ContentDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.Database.MenuDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.Database.SpotDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Utils.DateUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

import at.rags.morpheus.Error;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity
    implements XamoomContentFragment.OnXamoomContentFragmentInteractionListener {
  private static final String TAG = MainActivity.class.getSimpleName();
  private static final String API_URL = "https://xamoom3-dev.appspot.com/";
  private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;

  private EnduserApi mEnduserApi;
  private Style mStyle;
  private boolean mOffline = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    Mapbox.getInstance(this, "pk.eyJ1IjoieGFtb29tLWJydW5vIiwiYSI6ImNqcmc1MWxqbTFsNms0Nm1yZGcycTFqbjAifQ.sDuEiFnBOHNoS-o7uTHvdA");
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    checkPermission();

    setupEnduserApi();

    getContent();
  }

  @Override
  protected void onStart() {
    super.onStart();
  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void onStop() {
    super.onStop();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
  }

  @Override
  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
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
    if (id == R.id.action_close) {
      Fragment currentFragment = getSupportFragmentManager().findFragmentByTag("XamoomFragment");
      getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
    } else if (id == R.id.action_open) {
      mOffline = true;
      getContentOption();
    }

    return super.onOptionsItemSelected(item);
  }

  private void checkPermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("This app needs location access");
        builder.setMessage("Please grant location access so this app can detect beacons.");
        builder.setPositiveButton(android.R.string.ok, null);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
          @TargetApi(Build.VERSION_CODES.M)
          @Override
          public void onDismiss(DialogInterface dialog) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
          }
        });
        builder.show();
      }
    }
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

  }

  public void getContent() {
    List tags = new ArrayList();
    tags.add("x-start");
    mEnduserApi = new EnduserApi("3226a5a4-451e-4f40-943e-be55d43266f1", getApplicationContext(), false, null, 0);
    mEnduserApi.getContentsByTags(tags, 10, null, null, new APIListCallback<List<Content>, List<Error>>() {
      @Override
      public void finished(List<Content> result, String cursor, boolean hasMore) {

        if (result.size() == 0) {
          return;
        }


        ArrayList<String> urls = new ArrayList<>();
        urls.add("google.com");
        XamoomContentFragment xamoomFragment = XamoomContentFragment.newInstance(getResources().getString(R.string.youtube_key), urls, "", com.mapbox.mapboxsdk.maps.Style.LIGHT, "#ABCDEF", "#FEDCBA", "w"); //create new instance
        xamoomFragment.setShowSpotMapContentLinks(true);
        xamoomFragment.setEnduserApi(mEnduserApi);
        xamoomFragment.setDisplayAllStoreLinks(true);
        xamoomFragment.setContent(result.get(0), true, mOffline);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_frame, xamoomFragment, "XamoomFragment")
                .commit(); //replace with xamoomFragment
      }

      @Override
      public void error(List<Error> error) {
          Log.e("Error", error.get(0).getDetail());
      }
    });
  }

  public void getContentOption() {
    mEnduserApi.getContent("7cf2c58e6d374ce3888c32eb80be53b5", EnumSet.of(ContentFlags.PREVIEW, ContentFlags.PRIVATE), null,
        new APIPasswordCallback<Content, List<Error>>() {
          @Override
          public void finished(Content result) {
            Log.v(TAG, "getContent: " + result);
            XamoomContentFragment xamoomFragment = XamoomContentFragment.newInstance(getResources().getString(R.string.youtube_key), "", com.mapbox.mapboxsdk.maps.Style.LIGHT); //create new instance
            xamoomFragment.setEnduserApi(mEnduserApi);
            xamoomFragment.setDisplayAllStoreLinks(true);

            xamoomFragment.setContent(result, true, mOffline);
            xamoomFragment.setShowSpotMapContentLinks(true);
            //xamoomFragment.setStyle(mStyle);
            xamoomFragment.setBackgroundColor(Color.TRANSPARENT);
            //xamoomFragment.getContentBlockAdapter().getDelegatesManager().addDelegate(0, new CustomContentBlock0Adapter());


            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_frame, xamoomFragment, "XamoomFragment")
                    .commit(); //replace with xamoomFragment
          }

          @Override
          public void error(List<Error> error) {
            Log.e(TAG, "Error: " + error);
          }

          @Override
          public void passwordRequested() {

          }
        });

  }

  public void getContentLocationIdentifier() {
    mEnduserApi.getContentByLocationIdentifier(getResources().getString(R.string.qrMarker), null,
        new APIPasswordCallback<Content, List<Error>>() {
          @Override
          public void finished(Content result) {
            Log.v(TAG, "getContentByLocationIdentifier: " + result);
          }

          @Override
          public void error(List<Error> error) {

          }

          @Override
          public void passwordRequested() {

          }
        });

    mEnduserApi.getContentByLocationIdentifier(getResources().getString(R.string.qrMarker),
        EnumSet.of(ContentFlags.PREVIEW), null, new APIPasswordCallback<Content, List<Error>>() {
          @Override
          public void finished(Content result) {
            Log.v(TAG, "getContentByLocationIdentifier: " + result);
          }

          @Override
          public void error(List<Error> error) {

          }

              @Override
              public void passwordRequested() {

              }
            });
  }

  private void getContentWithConditions() {
    HashMap<String, Object> conditions = new HashMap<>(3);
    conditions.put("name", "myname");
    conditions.put("number", 5);
    conditions.put("float", 2.0f);
    conditions.put("double", (double) 1.000002358923523523523523535);
    conditions.put("date", new Date());

    mEnduserApi.getContentByLocationIdentifier(getResources().getString(R.string.qrMarker), null, conditions, null, new APIPasswordCallback<Content, List<Error>>() {
      @Override
      public void finished(Content result) {
        Log.v(TAG, "getContentByLocationIdentifier: " + result);
      }

      @Override
      public void error(List<Error> error) {
        Log.e(TAG, "Error contentByLocationIdentifier: " + error);
      }

      @Override
      public void passwordRequested() {

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

  private void getContentsWithTags() {
    ArrayList<String> tags = new ArrayList<>();
    tags.add("tests");

    Filter filter = new Filter.FilterBuilder()
        .fromDate(DateUtil.parse("2017-10-15T07:00:00Z"))
        .build();

    mEnduserApi.getContentsByTags(tags, 10, null, null, filter,
        new APIListCallback<List<Content>, List<Error>>() {
          @Override
          public void finished(List<Content> result, String cursor, boolean hasMore) {
            Log.v(TAG, "byTags: " + result);
          }

          @Override
          public void error(List<Error> error) {

          }
        });
  }

  private void getContentByDates() {
    mEnduserApi.getContentByDates(DateUtil.parse("2017-10-15T07:00:00Z"), null,
        "5755996320301056|5700735861784576", 10, null, null,
        null, new APIListCallback<List<Content>, List<Error>>() {
          @Override
          public void finished(List<Content> result, String cursor, boolean hasMore) {
            Log.v(TAG, "byDates: " + result);

            ContentDatabaseAdapter databaseAdapter = ContentDatabaseAdapter.getInstance(getApplicationContext());
            for (Content content : result) {
              databaseAdapter.insertOrUpdateContent(content, false, 0);
              Content savedContent = databaseAdapter.getContent(content.getId());
              Log.v(TAG, "Saved Content: " + savedContent);
            }
          }

          @Override
          public void error(List<Error> error) {

          }
        });
  }

  private void searchContent() {
    Filter filter = new Filter.FilterBuilder()
        .addTag("Wörthersee")
        .fromDate(DateUtil.parse("2017-10-18T12:00:00Z"))
        .toDate(DateUtil.parse("2017-10-20T17:00:00Z"))
        .relatedSpotId("5755996320301056|5700735861784576")
        .build();

    mEnduserApi.searchContentsByName("Test", 10, null, null, filter, new APIListCallback<List<Content>, List<Error>>() {
      @Override
      public void finished(List<Content> result, String cursor, boolean hasMore) {
        //Log.v(TAG, "searchContent: " + result.get(0));

        ContentDatabaseAdapter databaseAdapter = ContentDatabaseAdapter.getInstance(getApplicationContext());
        for (Content content : result) {
          databaseAdapter.insertOrUpdateContent(content, false, 0);
          Content savedContent = databaseAdapter.getContent(content.getId());
          Log.v(TAG, "Saved Content: " + savedContent);
        }
      }

      @Override
      public void error(List<Error> error) {

      }
    });
  }

  private void getRecommendations() {
    mEnduserApi.getContentRecommendations(new APIListCallback<List<Content>, List<Error>>() {
      @Override
      public void finished(List<Content> result, String cursor, boolean hasMore) {
        Log.v(TAG, "Recommendations: " + result);
      }

      @Override
      public void error(List<Error> error) {

      }
    });
  }

  public void getSpot() {
    mEnduserApi.getSpot("5755996320301056|5742506566221824", new APICallback<Spot, List<Error>>() {
      @Override
      public void finished(Spot result) {
        Log.v(TAG, "Result: " + result);
        SpotDatabaseAdapter spotDatabaseAdapter = SpotDatabaseAdapter.getInstance(getApplicationContext());
        spotDatabaseAdapter.insertOrUpdateSpot(result);
        Spot savedSpot = spotDatabaseAdapter.getSpot(result.getId());
        Log.v(TAG, "Saved spot: " + savedSpot);
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
        //Log.v(TAG, "getSpotsWithLocation: " + result.get(0));
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
    mEnduserApi.getSpotsByTags(tags, 0, null, EnumSet.of(SpotFlags.INCLUDE_CONTENT, SpotFlags.INCLUDE_MARKERS),
        null, new APIListCallback<List<Spot>, List<Error>>() {
          @Override
          public void finished(List<Spot> result, String cursor, boolean hasMore) {
            if (result.size() != 0) {
              Log.v(TAG, "getSpotsWithTags: " + result.get(0));
              Log.v(TAG, "getSpotsWithTags: " + cursor);
              Log.v(TAG, "getSpotsWithTags: " + hasMore);

              SpotDatabaseAdapter spotDatabaseAdapter = SpotDatabaseAdapter.getInstance(getApplicationContext());
              spotDatabaseAdapter.insertOrUpdateSpot(result.get(0));
              Spot savedSpot = spotDatabaseAdapter.getSpot(result.get(0).getId());
              Log.v(TAG, "Saved spot: " + savedSpot);
            }
          }

          @Override
          public void error(List<Error> error) {
            Log.e(TAG, "Error " + error);
          }
        });
  }

  private void searchSpots() {
    mEnduserApi.searchSpotsByName("do not", 10, null, null, null, new APIListCallback<List<Spot>, List<Error>>() {
      @Override
      public void finished(List<Spot> result, String cursor, boolean hasMore) {
        Log.v(TAG, "searchSpots: " + result);
      }

      @Override
      public void error(List<Error> error) {
        Log.v(TAG, "Error searchSpots: " + error);
      }
    });
  }


  public void getSystem() {
    mEnduserApi.getSystem(new APICallback<com.xamoom.android.xamoomsdk.Resource.System, List<Error>>() {
      @Override
      public void finished(System result) {
        Log.v(TAG, "getSystem: " + result.getId());

        /*
        SystemDatabaseAdapter systemDatabaseAdapter = SystemDatabaseAdapter.getInstance(getApplicationContext());
        systemDatabaseAdapter.insertOrUpdateSystem(result);
        System savedSystem = systemDatabaseAdapter.getSystem(result.getId());
        Log.v(TAG, "Saved system: " + savedSystem);
        */
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

        MenuDatabaseAdapter adapter = MenuDatabaseAdapter.getInstance(getApplicationContext());
        adapter.insertOrUpdate(result, -1);
        com.xamoom.android.xamoomsdk.Resource.Menu savedMenu = adapter.getMenu(result.getId());
        Log.v(TAG, "Saved menu: " + savedMenu);
      }

      @Override
      public void error(List<Error> error) {
        Log.e(TAG, "Error: " + error);
      }
    });
  }

  public void getSystemSetting() {
    mEnduserApi.getSystemSetting("5755996320301056", new APICallback<SystemSetting, List<Error>>() {
      @Override
      public void finished(SystemSetting result) {
        Log.v(TAG, "getRelatedSystemSetting: " + result);
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
        mStyle = result;
      }

      @Override
      public void error(List<Error> error) {

      }
    });
  }

  @Override
  public void clickedContentBlock(Content content) {
    XamoomContentFragment xamoomFragment = XamoomContentFragment.newInstance("AIzaSyBNZUh3-dj4YYY9-csOtQeHG_MpoE8x69Q", "", com.mapbox.mapboxsdk.maps.Style.LIGHT); //create new instance
    xamoomFragment.setEnduserApi(mEnduserApi);
    xamoomFragment.setDisplayAllStoreLinks(true);
    xamoomFragment.setContent(content);
    xamoomFragment.setShowSpotMapContentLinks(true);
    //xamoomFragment.setStyle(mStyle);
    getSupportFragmentManager().beginTransaction()
        .replace(R.id.main_frame, xamoomFragment, "XamoomFragment")
        .addToBackStack(null)
        .commit(); //replace with xamoomFragment
  }

  @Override
  public void clickedSpotMapContentLink(String contentId) {
    Log.v(TAG, "Click Map Content: " + contentId);
    mEnduserApi.getContent(contentId, null, new APIPasswordCallback<Content, List<Error>>() {
      @Override
      public void finished(Content result) {
        clickedContentBlock(result);
      }

      @Override
      public void error(List<Error> error) {

      }

      @Override
      public void passwordRequested() {

      }
    });
  }

  @Override
  public void onRequestPermissionsResult(int requestCode,
                                         String permissions[], int[] grantResults) {
    switch (requestCode) {
      case PERMISSION_REQUEST_COARSE_LOCATION: {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          Log.d(TAG, "coarse location permission granted");
        } else {
          final AlertDialog.Builder builder = new AlertDialog.Builder(this);
          builder.setTitle("Functionality limited");
          builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
          builder.setPositiveButton(android.R.string.ok, null);
          builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
            }
          });
          builder.show();
        }
      }
    }
  }
}
