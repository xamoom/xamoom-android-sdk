package com.xamoom.android;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.powersave.BackgroundPowerSaver;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.util.ArrayList;
import java.util.Collection;

/**
 * XamoomBeaconService is a simple to use beaconService on top of android-beacon-library.
 *
 */
public class XamoomBeaconService implements BootstrapNotifier, RangeNotifier, BeaconConsumer {
    private static final String TAG = "XamoomBeaconService";

    public static final String ENTER_REGION_BROADCAST = "com.xamoom.android.ENTER_REGION";
    public static final String EXIT_REGION_BROADCAST = "com.xamoom.android.EXIT_REGION";
    public static final String FOUND_BEACON_BROADCAST = "com.xamoom.android.FOUND_BEACON";
    public static final String NO_BEACON_BROADCAST = "com.xamoom.android.NO_BEACON";
    public static final String IMMEDIATE_BEACON_BROADCAST = "com.xamoom.android.IMMEDIATE_BEACON";
    public static final String NEAR_BEACON_BROADCAST = "com.xamoom.android.NEAR_BEACON";
    public static final String FAR_BEACON_BROADCAST = "com.xamoom.android.FAR_BEACON";
    public static final String BEACONS = "com.xamoom.android.BEACONS";
    public static final String BEACON_SERVICE_CONNECT_BROADCAST = "com.xamoom.android.BEACON_SERVICE_CONNECT_BROADCAST";

    private static XamoomBeaconService mInstance;

    private Context mContext;
    private BeaconManager mBeaconManager;
    private Region mRegion;
    private RegionBootstrap mRegionBootstrap;

    public boolean automaticRanging = false;
    public boolean approximateDistanceRanging = false;

    public static XamoomBeaconService getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new XamoomBeaconService();
            mInstance.mContext = context;
        }

        return mInstance;
    }

    public void startBeaconService() {
        Log.i(TAG, "startBeaconService");
        mBeaconManager = BeaconManager.getInstanceForApplication(mContext);
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        mBeaconManager.setRangeNotifier(this);

        mRegion = new Region("test", null, null, null);

        BackgroundPowerSaver backgroundPowerSaver = new BackgroundPowerSaver(mContext);

        mRegionBootstrap = new RegionBootstrap(this, mRegion);
        mBeaconManager.bind(this);
    }

    private void setBackgroundScanningSpeeds(int betweenScanPeriod, int scanPeriod){
        mBeaconManager.setBackgroundBetweenScanPeriod(betweenScanPeriod);
        mBeaconManager.setBackgroundScanPeriod(scanPeriod);

        try {
            mBeaconManager.updateScanPeriods();
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to update background scan periods. " + e.getMessage());
        }
    }

    private void sendBroadcast(String broadcastId, @Nullable ArrayList<Beacon> beacons) {
        Intent intent = new Intent(broadcastId);

        if (beacons != null) {
            intent.putParcelableArrayListExtra(BEACONS, beacons);
        }

        mContext.sendBroadcast(intent);
    }

    @Override
    public void didEnterRegion(Region region) {
        Log.i(TAG, "didEnterRegion");

        sendBroadcast(ENTER_REGION_BROADCAST, null);

        if (automaticRanging) {
            this.startRangingBeacons();
        }
    }

    @Override
    public void didExitRegion(Region region) {
        Log.i(TAG, "didExitRegion");

        if (automaticRanging) {
            this.stopRangingBeacons();
        }

        sendBroadcast(EXIT_REGION_BROADCAST, null);
    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        Log.i(TAG, "didRangeBeaconsInRegion: " + beacons.size());

        if (beacons.size() > 0) {
            sendBroadcast(FOUND_BEACON_BROADCAST, new ArrayList<Beacon>(beacons));
        } else {
            sendBroadcast(NO_BEACON_BROADCAST, null);
        }

        if (approximateDistanceRanging) {
            ArrayList<Beacon> immediateBeacons = new ArrayList<>();
            ArrayList<Beacon> nearBeacons = new ArrayList<>();
            ArrayList<Beacon> farBeacons = new ArrayList<>();

            for (Beacon beacon : beacons) {
                Log.d(TAG, "Distance: " + beacon.getDistance());

                if (beacon.getDistance() <= 0.5) {
                    immediateBeacons.add(beacon);
                } else if (beacon.getDistance() < 3.0 && beacon.getDistance() > 0.5) {
                    nearBeacons.add(beacon);
                } else if (beacon.getDistance() >= 3.0) {
                    farBeacons.add(beacon);
                }
            }

            if (immediateBeacons.size() > 0) {
                sendBroadcast(IMMEDIATE_BEACON_BROADCAST, immediateBeacons);
            }

            if (nearBeacons.size() > 0) {
                sendBroadcast(NEAR_BEACON_BROADCAST, nearBeacons);
            }

            if (farBeacons.size() > 0) {
                sendBroadcast(FAR_BEACON_BROADCAST, farBeacons);
            }
        }
    }

    public void startRangingBeacons() {
        Log.i(TAG, "Start ranging beacons");

        try {
            mBeaconManager.startRangingBeaconsInRegion(mRegion);
        } catch (RemoteException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void stopRangingBeacons() {
        Log.i(TAG, "Stop ranging beacons");

        try {
            mBeaconManager.stopRangingBeaconsInRegion(mRegion);
        } catch (RemoteException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void stopMonitoringRegion() {
        Log.i(TAG, "Stop monitoring beacons");
        mRegionBootstrap.disable();
    }

    public void startMonitoringRegion() {
        Log.i(TAG, "Start monitoring beacons");
        mRegionBootstrap = new RegionBootstrap(this, mRegion);
    }

    @Override
    public void onBeaconServiceConnect() {
        Log.i(TAG, "onBeaconServiceConnect");
        sendBroadcast(BEACON_SERVICE_CONNECT_BROADCAST, null);
    }

    @Override
    public Context getApplicationContext() {
        return mContext;
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {
        mContext.unbindService(serviceConnection);
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return mContext.bindService(intent, serviceConnection, i);
    }
}
