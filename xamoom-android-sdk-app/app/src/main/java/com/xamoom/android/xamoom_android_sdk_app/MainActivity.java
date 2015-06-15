package com.xamoom.android.xamoom_android_sdk_app;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.xamoom.android.APICallback;
import com.xamoom.android.XamoomEndUserApi;
import com.xamoom.android.mapping.ContentById;
import com.xamoom.android.mapping.ContentByLocation;
import com.xamoom.android.mapping.ContentByLocationIdentifier;
import com.xamoom.android.mapping.ContentList;
import com.xamoom.android.mapping.SpotMap;

public class MainActivity extends ActionBarActivity  {

    private static String TESTING_CONTENT_ID = "bc79d8a22a584604b6c9e8d04e4b0834";
    private static String TESTING_MARKER_ID = "dkriw";
    private TextView outputTextView;
    private ProgressDialog mProgressDialog;
    private Switch mMenuSwitch;
    private Switch mStyleSwitch;
    private boolean mMenuSwitchStatus;
    private boolean mStyleSwitchStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        outputTextView = (TextView)findViewById(R.id.outputTextView);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        mMenuSwitch = (Switch)findViewById(R.id.menuSwitch);
        mMenuSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mMenuSwitchStatus = isChecked;
            }
        });

        mStyleSwitch = (Switch)findViewById(R.id.styleSwitch);
        mStyleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mStyleSwitchStatus = isChecked;
            }
        });
    }

    public void getByIdButtonOnClick (View v) {
        mProgressDialog.show();
        XamoomEndUserApi.getInstance().getContentById(TESTING_CONTENT_ID, mStyleSwitchStatus, mMenuSwitchStatus, "de", new APICallback<ContentById>() {
            @Override
            public void finished(ContentById result) {
                mProgressDialog.dismiss();
                outputTextView.setText(result.toString());
            }
        });
    }

    public void getByIdFullButtonOnClick (View v) {
        mProgressDialog.show();
        XamoomEndUserApi.getInstance().getContentbyIdFull(TESTING_CONTENT_ID, mStyleSwitchStatus, mMenuSwitchStatus, "de", true, new APICallback<ContentById>() {
            @Override
            public void finished(ContentById result) {
                mProgressDialog.dismiss();
                outputTextView.setText(result.toString());
            }
        });
    }

    public void getByLocationIdentifierButtonOnClick (View v) {
        mProgressDialog.show();
        XamoomEndUserApi.getInstance().getContentByLocationIdentifier(TESTING_MARKER_ID, mStyleSwitchStatus, mMenuSwitchStatus, "de", new APICallback<ContentByLocationIdentifier>() {
            @Override
            public void finished(ContentByLocationIdentifier result) {
                mProgressDialog.dismiss();
                outputTextView.setText(result.toString());
            }
        });
    }

    public void getByLocationFullButtonOnClick (View v) {
        mProgressDialog.show();
        XamoomEndUserApi.getInstance().getContentByLocation(46.615119, 14.262106, "de", new APICallback<ContentByLocation>() {
            @Override
            public void finished(ContentByLocation result) {
                mProgressDialog.dismiss();
                outputTextView.setText(result.toString());
            }
        });
    }

    public void getSpotMapButtonOnClick (View v) {
        mProgressDialog.show();
        XamoomEndUserApi.getInstance().getSpotMap("0", new String[]{"stw"}, "de", new APICallback<SpotMap>() {
            @Override
            public void finished(SpotMap result) {
                mProgressDialog.dismiss();
                outputTextView.setText(result.toString());
            }
        });
    }

    public void getClosestSpotsButtonOnClick (View v) {
        mProgressDialog.show();
        XamoomEndUserApi.getInstance().getClosestSpots(46.615119, 14.262106, "de", 100, 10, new APICallback<SpotMap>() {
            @Override
            public void finished(SpotMap result) {
                mProgressDialog.dismiss();
                outputTextView.setText(result.toString());
            }
        });
    }

    public void getContentListButtonOnClick (View v) {
        mProgressDialog.show();
        XamoomEndUserApi.getInstance().getContentList("de", 7, null, new String[]{"artists"}, new APICallback<ContentList>() {
            @Override
            public void finished(ContentList result) {
                mProgressDialog.dismiss();
                outputTextView.setText(result.toString());
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