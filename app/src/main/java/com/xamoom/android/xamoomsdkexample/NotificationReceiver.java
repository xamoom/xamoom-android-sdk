package com.xamoom.android.xamoomsdkexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.pushwoosh.PushManager;
import com.pushwoosh.internal.PushManagerImpl;

import org.json.JSONObject;

/**
 * Created by raphaelseher on 20/06/2017.
 */

public class NotificationReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    if (intent == null)
      return;

    //Let Pushwoosh SDK to pre-handling push (Pushwoosh track stats, opens rich pages, etc.).
    //It will return Bundle with a push notification data
    Bundle pushBundle = PushManagerImpl.preHandlePush(context, intent);
    if(pushBundle == null)
      return;

    //get push bundle as JSON object
    JSONObject dataObject = PushManagerImpl.bundleToJSON(pushBundle);

    //Get default launcher intent for clarity
    Intent launchIntent  = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
    launchIntent.addCategory("android.intent.category.LAUNCHER");

    launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

    //Put push notifications payload in Intent
    launchIntent.putExtras(pushBundle);
    launchIntent.putExtra(PushManager.PUSH_RECEIVE_EVENT, dataObject.toString());

    //Start activity!
    context.startActivity(launchIntent);

    //Let Pushwoosh SDK post-handle push (track stats, etc.)
    PushManagerImpl.postHandlePush(context, intent);
  }
}
