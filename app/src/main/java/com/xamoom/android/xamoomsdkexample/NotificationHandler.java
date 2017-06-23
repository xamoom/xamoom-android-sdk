package com.xamoom.android.xamoomsdkexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pushwoosh.PushManager;
import com.xamoom.android.pushnotifications.XamoomPushActivity;

/**
 * Created by raphaelseher on 22/06/2017.
 */

public class NotificationHandler extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    Log.v("NotificationHandler", "Got it!");

    context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
  }
}
