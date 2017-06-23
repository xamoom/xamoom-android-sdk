package com.xamoom.android.pushnotifications;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;

import com.pushwoosh.PushManager;
import com.pushwoosh.internal.PushManagerImpl;
import com.xamoom.android.xamoomsdk.Resource.Content;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by raphaelseher on 20/06/2017.
 */

public class NotificationReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    if (intent == null) {
      return;
    }

    Bundle pushBundle = PushManagerImpl.preHandlePush(context, intent);
    if(pushBundle == null) {
      return;
    }

    //get push bundle as JSON object
    JSONObject dataObject = PushManagerImpl.bundleToJSON(pushBundle);

    Intent customNotificationIntent = new Intent(XamoomPushActivity.PUSH_NOTIFICATION_HANDLER_NAME);
    if (isCustomNotificationHandlerRegistered(context, customNotificationIntent)) {
      sendCustomHandlerBroadcast(context, customNotificationIntent, dataObject);
      PushManagerImpl.postHandlePush(context, intent);
      return;
    }

    openDefaultActivity(context, intent, dataObject, pushBundle);
    PushManagerImpl.postHandlePush(context, intent);
  }

  /**
   * Checks if there is a broadcastreceiver entered for the
   * XamoomPushActivity.PUSH_NOTIFICATION_HANDLER_NAME.
   *
   * @param context Android context
   * @param intent Intent with XamoomPushActivity.PUSH_NOTIFICATION_HANDLER_NAME action
   * @return true if registered
   */
  private boolean isCustomNotificationHandlerRegistered(Context context, Intent intent) {
    PackageManager pm = context.getPackageManager();
    List<ResolveInfo> listPkgs = pm.queryBroadcastReceivers(intent, 0);
    if (listPkgs != null && listPkgs.size() > 0){
      for (ResolveInfo resInfo : listPkgs) {
        return true;
      }
    }

    return false;
  }

  private void sendCustomHandlerBroadcast(Context context, Intent intent, JSONObject dataObject) {
    // TODO: send custom broadcast
  }

  private void openDefaultActivity(Context context, Intent intent, JSONObject dataObject, Bundle pushBundle) {
    //Get default launcher intent for clarity
    Intent launchIntent  = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
    launchIntent.addCategory("android.intent.category.LAUNCHER");

    launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

    //Put push notifications payload in Intent
    launchIntent.putExtras(pushBundle);
    launchIntent.putExtra(PushManager.PUSH_RECEIVE_EVENT, dataObject.toString());

    //Start activity!
    context.startActivity(launchIntent);
  }
}
