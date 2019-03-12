package com.xamoom.android.xamoomsdk.Helpers;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import android.content.SharedPreferences;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.xamoom.android.xamoomsdk.APICallback;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.Enums.ContentReason;
import com.xamoom.android.xamoomsdk.PushDevice.PushDeviceUtil;
import com.xamoom.android.xamoomsdk.Resource.Content;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import at.rags.morpheus.Error;

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
    public static final String CONTENTS = "com.xamoom.android.CONTENTS";
    public static final String BEACON_SERVICE_CONNECT_BROADCAST =
            "com.xamoom.android.BEACON_SERVICE_CONNECT_BROADCAST";

    private static final int FAST_INSIDE_SCANNING_SCAN_PERIOD = 2200;
    private static final int FAST_INSIDE_SCANNING_BETWEEN_SCAN_PERDIO = 5000;
    private static final int SCAN_PERIOD = 2200;
    private static final int BETWEEN_SCAN_PERIOD = 40000;

    private static XamoomBeaconService mInstance;

    private Context mContext;
    private BeaconManager mBeaconManager;
    private Region mRegion;
    private RegionBootstrap mRegionBootstrap;
    private EnduserApi api;

    private ArrayList<Beacon> mBeacons = new ArrayList<>();
    private ArrayList<Beacon> immediateBeacons = new ArrayList<>();
    private ArrayList<Beacon> nearBeacons = new ArrayList<>();
    private ArrayList<Beacon> farBeacons = new ArrayList<>();
    private ArrayList<Content> beaconContents = new ArrayList<>();

    public boolean automaticRanging = false;
    public boolean approximateDistanceRanging = false;
    public boolean fastInsideRegionScanning = true;
    private boolean areBeaconsLoading = false;

    /**
     * Method to get the singleton on XamoomBeaconService.
     *
     * @param context A context.
     * @return An instance of XamoomBeaconService.
     */
    public static XamoomBeaconService getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new XamoomBeaconService();
            mInstance.mContext = context;
            mInstance.mBeaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(context);
            mInstance.api = EnduserApi.getSharedInstance(context);
        }

        return mInstance;
    }

    /**
     * Start the XamoomBeaconService with a beacon majorId.
     * This will automatically start (background-)monitoring for xamoom beacons
     * with the used majorId.
     */
    public void startBeaconService() {
        Log.i(TAG, "startBeaconService");

        String majorId = api.getMajorId();

        if (majorId.equalsIgnoreCase("")) {
            throw new IllegalArgumentException("MajorId should not be a number.");
        }
        mRegion = new Region("at.visitklagenfurt.beacons", Identifier.parse("de2b94ae-ed98-11e4-3432-78616d6f6f6d"),
                Identifier.parse(majorId), null);
        mRegionBootstrap = new RegionBootstrap(this, mRegion);

        mBeaconManager.setBackgroundMode(false);
        mBeaconManager.setRegionStatePeristenceEnabled(false);
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        mBeaconManager.setRangeNotifier(this);

        mBeaconManager.bind(this);
    }

    /**
     * Change the backgroundScanningSpeed from the beaconManager.
     * Can make your app use more energy than needed.
     *
     * @param betweenScanPeriod Value in ms to wait between scans.
     * @param scanPeriod Value in ms to scan for beacons. Should not be below 1100 ms.
     */
    public void setBackgroundScanningSpeeds(int betweenScanPeriod, int scanPeriod) {
        mBeaconManager.setBackgroundBetweenScanPeriod(betweenScanPeriod);
        mBeaconManager.setBackgroundScanPeriod(scanPeriod);

        try {
            mBeaconManager.updateScanPeriods();
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to update background scan periods. " + e.getMessage());
        }
    }

    /**
     * Change the foregroundScanningSpeed from the beaconManager.
     * Can make your app use more energy than needed.
     *
     * @param betweenScanPeriod Value in ms to wait between scans.
     * @param scanPeriod Value in ms to scan for beacons. Should not be below 1100 ms.
     */
    public void setForegroundScanningSpeeds(int betweenScanPeriod, int scanPeriod) {
        mBeaconManager.setForegroundBetweenScanPeriod(betweenScanPeriod);
        mBeaconManager.setForegroundScanPeriod(scanPeriod);

        try {
            mBeaconManager.updateScanPeriods();
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to update foreground scan periods. " + e.getMessage());
        }
    }

    /*
     * Sends a broadcast with a broadcastId and beacons.
     *
     * @param broadcastId BroadcastId from XamoomBeaconService.
     * @param beacons Beacons, if there should be beacons in the broadcast.
     */
    private void sendBroadcast(String broadcastId, @Nullable ArrayList<Beacon> beacons, @Nullable ArrayList<Content> contents) {
        Intent intent = new Intent(broadcastId);

        if (beacons != null) {
            intent.putParcelableArrayListExtra(BEACONS, beacons);
        }

        if (contents != null) {
            intent.putParcelableArrayListExtra(CONTENTS, contents);
        }

        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
    }

    /*
     * Will be called when entering the region.
     *
     * Sends a ENTER_REGION_BROADCAST.
     *
     * @param region Monitored region.
     */
    @Override
    public void didEnterRegion(Region region) {
        Log.i(TAG, "didEnterRegion");

        sendBroadcast(ENTER_REGION_BROADCAST, null, null);

        if (fastInsideRegionScanning) {
            setBackgroundScanningSpeeds(FAST_INSIDE_SCANNING_BETWEEN_SCAN_PERDIO,
                    FAST_INSIDE_SCANNING_SCAN_PERIOD);
        }

        if (automaticRanging) {
            this.startRangingBeacons();
        }
    }

    /*
     * Will be called when leaving a region.
     *
     * Sends a EXIT_REGION_BROADCAST.
     *
     * @param region Monitored region.
     */
    @Override
    public void didExitRegion(Region region) {
        Log.i(TAG, "didExitRegion");

        if (automaticRanging) {
            this.stopRangingBeacons();
        }

        if (fastInsideRegionScanning) {
            setBackgroundScanningSpeeds(BETWEEN_SCAN_PERIOD, SCAN_PERIOD);
        }

        sendBroadcast(EXIT_REGION_BROADCAST, null, null);
    }

    /*
     * Will be called, the monitoring state for a region changes.
     */
    @Override
    public void didDetermineStateForRegion(int i, Region region) {
    }

    /*
     * Will be called when ranging beacons.
     *
     * Sends a FOUND_BEACON_BROADCAST.
     *
     * If you have enabled approximateDistanceRanging it will also send
     * IMMEDIATE_BEACON_BROADCAST, NEAR_BEACON_BROADCAST and FAR_BEACON_BROADCAST broadcasts.
     *
     * @param beacons Beacons found in region.
     * @param region Ranged region.
     */
    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
        if (region.getId2() == mRegion.getId2()) {
            Log.i(TAG, "false region");
            return;
        }

        SharedPreferences prefs = mContext.getSharedPreferences(PushDeviceUtil.PREFES_NAME,
                Context.MODE_PRIVATE);
        PushDeviceUtil pUtil = new PushDeviceUtil(prefs);
        api.pushDevice(pUtil);

        mBeacons.clear();
        beaconContents.clear();
        immediateBeacons.clear();
        nearBeacons.clear();
        farBeacons.clear();

        if (areBeaconsLoading || beacons.size() == 0) {
            mBeacons.clear();
            beaconContents.clear();
            sendBroadcast(FOUND_BEACON_BROADCAST, new ArrayList<Beacon>(), new ArrayList<Content>());
            return;
        }


        BeaconDownloadCallback callback = new BeaconDownloadCallback() {
            @Override
            public void finish(ArrayList<Beacon> beacons, ArrayList<Content> contents) {
                if (beacons.size() > 0) {
                    mBeacons.addAll(beacons);
                    beaconContents.addAll(contents);

                    sendBroadcast(FOUND_BEACON_BROADCAST, mBeacons, beaconContents);
                } else {
                    sendBroadcast(NO_BEACON_BROADCAST, null, null);
                }

                if (approximateDistanceRanging) {
                    sendBroadcast(IMMEDIATE_BEACON_BROADCAST, immediateBeacons, null);
                    sendBroadcast(NEAR_BEACON_BROADCAST, nearBeacons, null);
                    sendBroadcast(FAR_BEACON_BROADCAST, farBeacons, null);
                }
                areBeaconsLoading = false;
            }
        };

        try {
            areBeaconsLoading = true;
            downloadBeacons(beacons, callback);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    private void downloadBeacons(final Collection<Beacon> beacons, final BeaconDownloadCallback callback) throws InterruptedException {
        final ArrayList<Beacon> loadedBeacons = new ArrayList<>();
        final ArrayList<Content> loadedContents = new ArrayList<>();

        final CountDownLatch signal = new CountDownLatch(beacons.size());
        for (int i = 0; i < beacons.size(); i++) {
            final Beacon beacon = (Beacon) beacons.toArray()[i];

            api.getContentByBeacon(Integer.getInteger(api.getMajorId()), beacon.getId3().toInt(), null, null, ContentReason.NOTIFICATION, new APICallback<Content, List<Error>>() {
                @Override
                public void finished(Content result) {
                    if (result != null) {
                        loadedBeacons.add(beacon);
                        loadedContents.add(result);

                        if (beacon.getDistance() <= 0.5) {
                            immediateBeacons.add(beacon);
                        } else if (beacon.getDistance() < 3.0 && beacon.getDistance() > 0.5) {
                            nearBeacons.add(beacon);
                        } else if (beacon.getDistance() >= 3.0) {
                            farBeacons.add(beacon);
                        }
                    }

                    signal.countDown();
                }

                @Override
                public void error(List<Error> error) {
                    callback.finish(null, null);
                }
            });
        }

        signal.await();
        callback.finish(loadedBeacons, loadedContents);
    }

    /**
     * Start the ranging for beacons.
     */
    public void startRangingBeacons() {
        Log.i(TAG, "Start ranging beacons");

        try {
            mBeaconManager.startRangingBeaconsInRegion(mRegion);
        } catch (RemoteException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * Stop the ranging for beacons.
     */
    public void stopRangingBeacons() {
        Log.i(TAG, "Stop ranging beacons");

        try {
            mBeaconManager.stopRangingBeaconsInRegion(mRegion);
        } catch (RemoteException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    /**
     * Stop the monitoring for beacons. (Also stops background monitoring)
     */
    public void stopMonitoringRegion() {
        Log.i(TAG, "Stop monitoring beacons");
        mRegionBootstrap.disable();
    }

    /**
     * Start the monitoring for beacons. (Also starts the background monitoring)
     */
    public void startMonitoringRegion() {
        Log.i(TAG, "Start monitoring beacons");
        mRegionBootstrap = new RegionBootstrap(this, mRegion);
    }

    /**
     * Will be called, when the beaconManager is ready to be used.
     *
     * Sends a BEACON_SERVICE_CONNECT_BROADCAST.
     */
    @Override
    public void onBeaconServiceConnect() {
        Log.i(TAG, "onBeaconServiceConnect");
        sendBroadcast(BEACON_SERVICE_CONNECT_BROADCAST, null, null);

        setBackgroundScanningSpeeds(BETWEEN_SCAN_PERIOD, SCAN_PERIOD);
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

    public boolean isBound() {
        return mBeaconManager.isBound(this);
    }

    public ArrayList<Beacon> getBeacons() {
        return mBeacons;
    }
}

interface BeaconDownloadCallback {
    void finish(ArrayList<Beacon> beacons, ArrayList<Content> contents);
}