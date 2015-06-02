package com.xamoom.xamoom_android_sdk.xamoom_android_sdk;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.XamoomApiListener;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.XamoomEndUserApi;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentBlocks.ResponseContentBlock;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentById;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentByLocation;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentByLocationIdentifier;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentByLocationItem;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentList;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ResponseSpotMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends ActionBarActivity implements XamoomApiListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create XamoomEndUserApi & set listener
        XamoomEndUserApi.getInstance().setListener(this);

        //test every api call
        XamoomEndUserApi.getInstance().getContentById("a3911e54085c427d95e1243844bd6aa3", false, false, "de");

        XamoomEndUserApi.getInstance().getContentbyIdFull("a3911e54085c427d95e1243844bd6aa3", false, false, "de", true);

        XamoomEndUserApi.getInstance().getContentByLocationIdentifier("0ana0", false, false, "de");

        XamoomEndUserApi.getInstance().getContentByLocation(46.615119, 14.262106, "de");

        XamoomEndUserApi.getInstance().getSpotMap("0", new String[]{"stw"}, "de");

        XamoomEndUserApi.getInstance().getClosestSpots(46.615119, 14.262106, "de", 100, 10);

        XamoomEndUserApi.getInstance().getContentList("de", 7, null, new String[]{"artists"});
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

    //event listeners

    @Override
    public void gotContentByLocationIdentifier(ContentByLocationIdentifier result) {
        Log.v("XamoomEndUserApi","Got ContentByLocationIdentifier: " + result);
    }

    @Override
    public void gotContentById(ContentById result) {
        Log.v("XamoomEndUserApi","Got ContentById: " + result);
    }

    @Override
    public void gotContentByLocation(ContentByLocation result) {
        Log.v("XamoomEndUserApi","Got ContentByLocation: " + result);

        ContentByLocationItem item = result.getItems().get(0);
        XamoomEndUserApi.getInstance().queueGeofenceAnalytics("de", item.getLanguage(), item.getSystemId(), item.getSystemName(), item.getContentId(), item.getContentName(), item.getSpotId(), item.getSpotName());
    }

    @Override
    public void gotSpotMap(ResponseSpotMap result) {
        Log.v("XamoomEndUserApi","Got ResponseSpotMap: " + result);
    }

    @Override
    public void gotClosestSpots(ResponseSpotMap result) {
        Log.v("XamoomEndUserApi","Got ResponseSpotMap: " + result);
    }

    @Override
    public void gotContentList(ContentList result) {
        Log.v("XamoomEndUserApi","Got ContentList: " + result);
    }
}
