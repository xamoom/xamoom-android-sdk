package com.xamoom.android.xamoom_android_sdk_app;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.xamoom.android.*;
import com.xamoom.android.XamoomEndUserApi;
import com.xamoom.android.mapping.Content;
import com.xamoom.android.mapping.ContentById;
import com.xamoom.android.mapping.ContentByLocation;
import com.xamoom.android.mapping.ContentByLocationIdentifier;
import com.xamoom.android.mapping.ContentList;
import com.xamoom.android.mapping.SpotMap;
import com.xamoom.android.xamoomcontentblocks.XamoomContentFragment;

import retrofit.RetrofitError;

public class MainActivity extends AppCompatActivity implements XamoomContentFragment.OnXamoomContentFragmentInteractionListener {

    private static String TESTING_CONTENT_ID = "8f51819db5c6403d8455593322437c07"; //pingeb.org about page (productive)
    private static String TESTING_MARKER_ID = "5k9kv"; //Gründerzenturm Build QR Marker
    private static String[] TESTING_BEACON = new String[]{"de2b94ae-ed98-11e4-3432-78616d6f6f6d","5704","1209"};
    private TextView outputTextView;
    private ProgressDialog mProgressDialog;
    private Switch mMenuSwitch;
    private Switch mStyleSwitch;
    private boolean mMenuSwitchStatus;
    private boolean mStyleSwitchStatus;
    private String mApiKey;

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

        mApiKey = getResources().getString(R.string.apiKey);
    }

    public void getByIdFullButtonOnClick (View v) {
        mProgressDialog.show();
        XamoomEndUserApi.getInstance(this.getApplicationContext(), mApiKey).getContentbyIdFull(TESTING_CONTENT_ID, mStyleSwitchStatus, mMenuSwitchStatus, "de", true, new APICallback<ContentById>() {
            @Override
            public void finished(ContentById result) {
                mProgressDialog.dismiss();
                outputTextView.setText(result.toString());
            }

            @Override
            public void error(RetrofitError error) {

            }
        });
    }

    public void getByLocationIdentifierButtonOnClick (View v) {
        mProgressDialog.show();
        XamoomEndUserApi.getInstance(this.getApplicationContext(), mApiKey).getContentByLocationIdentifier(TESTING_MARKER_ID, null, mStyleSwitchStatus, mMenuSwitchStatus, "de", new APICallback<ContentByLocationIdentifier>() {
            @Override
            public void finished(ContentByLocationIdentifier result) {
                mProgressDialog.dismiss();
                outputTextView.setText(result.toString());
            }

            @Override
            public void error(RetrofitError error) {

            }
        });
    }

    public void getByLocationIdentifierBeaconButtonOnClick (View v) {
        mProgressDialog.show();
        XamoomEndUserApi.getInstance(this.getApplicationContext(), mApiKey).getContentByLocationIdentifier(TESTING_BEACON[2], TESTING_BEACON[1], mStyleSwitchStatus, mMenuSwitchStatus, "de", new APICallback<ContentByLocationIdentifier>() {
            @Override
            public void finished(ContentByLocationIdentifier result) {
                mProgressDialog.dismiss();
                outputTextView.setText(result.toString());
            }

            @Override
            public void error(RetrofitError error) {

            }
        });
    }

    public void getByLocationFullButtonOnClick (View v) {
        mProgressDialog.show();
        XamoomEndUserApi.getInstance(this.getApplicationContext(), mApiKey).getContentByLocation(46.615119, 14.262106, "de", new APICallback<ContentByLocation>() {
            @Override
            public void finished(ContentByLocation result) {
                mProgressDialog.dismiss();
                outputTextView.setText(result.toString());
            }

            @Override
            public void error(RetrofitError error) {

            }
        });
    }

    public void getSpotMapButtonOnClick (View v) {
        mProgressDialog.show();
        XamoomEndUserApi.getInstance(this.getApplicationContext(), mApiKey).getSpotMap("0", new String[]{"stw"}, "de", new APICallback<SpotMap>() {
            @Override
            public void finished(SpotMap result) {
                mProgressDialog.dismiss();
                outputTextView.setText(result.toString());
            }

            @Override
            public void error(RetrofitError error) {

            }
        });
    }

    public void getClosestSpotsButtonOnClick (View v) {
        mProgressDialog.show();
        XamoomEndUserApi.getInstance(this.getApplicationContext(), mApiKey).getClosestSpots(46.615119, 14.262106, "de", 100, 10, new APICallback<SpotMap>() {
            @Override
            public void finished(SpotMap result) {
                mProgressDialog.dismiss();
                outputTextView.setText(result.toString());
            }

            @Override
            public void error(RetrofitError error) {

            }
        });
    }

    public void getContentListButtonOnClick (View v) {
        mProgressDialog.show();
        XamoomEndUserApi.getInstance(this.getApplicationContext(), mApiKey).getContentList("de", 7, null, new String[]{"artists"}, new APICallback<ContentList>() {
            @Override
            public void finished(ContentList result) {
                mProgressDialog.dismiss();
                outputTextView.setText(result.toString());
            }

            @Override
            public void error(RetrofitError error) {

            }
        });
    }

    public void xamoomcontentBlocksClick (View v) {
        mProgressDialog.show();
        XamoomEndUserApi.getInstance(this.getApplicationContext(), mApiKey).getContentbyIdFull(TESTING_CONTENT_ID, mStyleSwitchStatus, mMenuSwitchStatus, "de", true, new APICallback<ContentById>() {
            @Override
            public void finished(ContentById result) {
                mProgressDialog.dismiss();
                XamoomContentFragment fragment = XamoomContentFragment.newInstance(null, mApiKey);
                fragment.setContent(result.getContent());
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.xamoomContentBlocksFrameLayout, fragment)
                        .commit();
            }

            @Override
            public void error(RetrofitError error) {

            }
        });
    }

    @Override
    public void clickedContentBlock(Content content) {
        XamoomContentFragment fragment = XamoomContentFragment.newInstance(null, mApiKey);
        fragment.setContent(content);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.xamoomContentBlocksFrameLayout, fragment)
                .addToBackStack(null)
                .commit();
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
