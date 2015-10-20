package com.xamoom.android.xamoom_android_sdk_app;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
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

import org.altbeacon.beacon.Beacon;

import java.util.ArrayList;

import retrofit.RetrofitError;

public class MainActivity extends AppCompatActivity implements XamoomContentFragment.OnXamoomContentFragmentInteractionListener {

    private static final String TAG = "XamoomAndroidSdkApp";
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private static final String TESTING_CONTENT_ID = "8f51819db5c6403d8455593322437c07";
    private static final String TESTING_MARKER_ID = "5k9kv";
    private static final String[] TESTING_BEACON = new String[]{"de2b94ae-ed98-11e4-3432-78616d6f6f6d","5704","1209"};
    private TextView outputTextView;
    private ProgressDialog mProgressDialog;
    private Switch mMenuSwitch;
    private Switch mStyleSwitch;
    private boolean mMenuSwitchStatus;
    private boolean mStyleSwitchStatus;
    private String mApiKey;

    private final BroadcastReceiver mEnterRegionBroadCastReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "enterRegion");
        }
    };

    private final BroadcastReceiver mExitRegionBroadCastReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "exitRegion");
        }
    };

    private final BroadcastReceiver mFoundBeaconsBroadCastReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Beacon> beacons = intent.getParcelableArrayListExtra(XamoomBeaconService.BEACONS);
            Log.d(TAG, "found beacon : " + beacons);
        }
    };

    private final BroadcastReceiver mNoBeaconsBroadCastReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "no beacons");
        }
    };

    private final BroadcastReceiver mImmediateBeaconsBroadCastReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Beacon> beacons = intent.getParcelableArrayListExtra(XamoomBeaconService.BEACONS);
            Log.d(TAG, "immediate beacons");
        }
    };

    private final BroadcastReceiver mNearBeaconsBroadCastReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Beacon> beacons = intent.getParcelableArrayListExtra(XamoomBeaconService.BEACONS);
            Log.d(TAG, "near beacons");
        }
    };

    private final BroadcastReceiver mFarBeaconsBroadCastReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<Beacon> beacons = intent.getParcelableArrayListExtra(XamoomBeaconService.BEACONS);
            Log.d(TAG, "far beacons");
        }
    };

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

        mApiKey = getResources().getString(R.string.prod_apiKey);

        checkPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mEnterRegionBroadCastReciever, new IntentFilter(XamoomBeaconService.ENTER_REGION_BROADCAST));
        registerReceiver(mExitRegionBroadCastReciever, new IntentFilter(XamoomBeaconService.EXIT_REGION_BROADCAST));
        registerReceiver(mFoundBeaconsBroadCastReciever, new IntentFilter(XamoomBeaconService.FOUND_BEACON_BROADCAST));
        registerReceiver(mNoBeaconsBroadCastReciever, new IntentFilter(XamoomBeaconService.NO_BEACON_BROADCAST));
        registerReceiver(mImmediateBeaconsBroadCastReciever, new IntentFilter(XamoomBeaconService.IMMEDIATE_BEACON_BROADCAST));
        registerReceiver(mNearBeaconsBroadCastReciever, new IntentFilter(XamoomBeaconService.NEAR_BEACON_BROADCAST));
        registerReceiver(mFarBeaconsBroadCastReciever, new IntentFilter(XamoomBeaconService.FAR_BEACON_BROADCAST));
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
                return;
            }
        }
    }
}
