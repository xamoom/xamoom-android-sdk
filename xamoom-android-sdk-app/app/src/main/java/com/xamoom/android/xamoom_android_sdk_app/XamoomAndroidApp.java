package com.xamoom.android.xamoom_android_sdk_app;

import android.app.Application;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.xamoom.android.XamoomBeaconService;

/**
 * Created by raphaelseher on 08.10.15.
 */
public class XamoomAndroidApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        XamoomBeaconService.getInstance(getApplicationContext()).startBeaconService("5704");
        XamoomBeaconService.getInstance(getApplicationContext()).automaticRanging = true;
        XamoomBeaconService.getInstance(getApplicationContext()).approximateDistanceRanging = true;

        //only place to do things, when scanning in background
        //registerReceiver(mEnterRegionBroadCastReciever, new IntentFilter(XamoomBeaconService.ENTER_REGION_BROADCAST));
        //registerReceiver(mExitRegionBroadCastReciever, new IntentFilter(XamoomBeaconService.EXIT_REGION_BROADCAST));
    }

    private final BroadcastReceiver mEnterRegionBroadCastReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("XamoomBeaconService", "background: enterRegion");

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext());
            notificationBuilder
                    .setContentText("Öffne um etwas zu Entdecken.")
                    .setContentTitle("xamoom in der Nähe")
                    .setContentInfo("Beacon")
                    .setSmallIcon(R.drawable.ic_xamoom_ble)
                    .setNumber(1);
            Notification notification = notificationBuilder.build();

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
            notificationManager.notify(1, notification);
        }
    };

    private final BroadcastReceiver mExitRegionBroadCastReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("XamoomBeaconService", "background: exitRegion");
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
            notificationManager.cancel(1);
        }
    };
}
