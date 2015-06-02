package com.xamoom.xamoom_android_sdk.xamoom_android_sdk;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.APICallback;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.XamoomEndUserApi;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentById;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentByLocation;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentByLocationIdentifier;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentList;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.SpotMap;


public class MainActivity extends ActionBarActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //test every api call
        XamoomEndUserApi.getInstance().getContentById("a3911e54085c427d95e1243844bd6aa3", false, false, "de", new APICallback<ContentById>() {
            @Override
            public void finished(ContentById result) {
                Log.v("XamoomEndUserApi", "Worked! " + result);
            }
        });

        XamoomEndUserApi.getInstance().getContentbyIdFull("a3911e54085c427d95e1243844bd6aa3", false, false, "de", true, new APICallback<ContentById>() {
            @Override
            public void finished(ContentById result) {
                Log.v("XamoomEndUserApi", "Worked! " + result);
            }
        });

        XamoomEndUserApi.getInstance().getContentByLocationIdentifier("0ana0", false, false, "de", new APICallback<ContentByLocationIdentifier>() {
            @Override
            public void finished(ContentByLocationIdentifier result) {
                Log.v("XamoomEndUserApi", "Worked! " + result);
            }
        });

        XamoomEndUserApi.getInstance().getContentByLocation(46.615119, 14.262106, "de", new APICallback<ContentByLocation>() {
            @Override
            public void finished(ContentByLocation result) {
                Log.v("XamoomEndUserApi", "Worked! " + result);
            }
        });

        XamoomEndUserApi.getInstance().getSpotMap("0", new String[]{"stw"}, "de", new APICallback<SpotMap>() {
            @Override
            public void finished(SpotMap result) {
                Log.v("XamoomEndUserApi", "Worked! " + result);
            }
        });

        XamoomEndUserApi.getInstance().getClosestSpots(46.615119, 14.262106, "de", 100, 10, new APICallback<SpotMap>() {
            @Override
            public void finished(SpotMap result) {
                Log.v("XamoomEndUserApi", "Worked! " + result);
            }
        });

        XamoomEndUserApi.getInstance().getContentList("de", 7, null, new String[]{"artists"}, new APICallback<ContentList>() {
            @Override
            public void finished(ContentList result) {
                Log.v("XamoomEndUserApi", "Worked! " + result);
            }
        });

        XamoomEndUserApi.getInstance().getContentList("de", 7, null, new String[]{"artists"}, new APICallback<ContentList>() {
            @Override
            public void finished(ContentList result) {
                Log.v("XamoomEndUserApi", "Worked! " + result);
            }
        });
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
}
