package com.xamoom.xamoom_android_sdk.xamoom_android_sdk;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.XamoomApiListener;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.XamoomEndUserApi;
import com.xamoom.xamoom_android_sdk.xamoom_android_sdk.api.mapping.ContentByLocationIdentifier;


public class MainActivity extends ActionBarActivity implements XamoomApiListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create XamoomEndUserApi & set listener
        XamoomEndUserApi api = new XamoomEndUserApi();
        api.setListener(this);

        //test every api call
        api.getContentByLocationIdentifier("0ana0", true, true, "de");
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
        Log.v("XamoomEndUserApi","Got something: " + result);
    }
}
