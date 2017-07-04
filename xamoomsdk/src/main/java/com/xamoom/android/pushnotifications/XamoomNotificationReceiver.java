/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.pushnotifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;

import com.pushwoosh.PushManager;
import com.pushwoosh.internal.PushManagerImpl;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class XamoomNotificationReceiver extends BroadcastReceiver {
  private static final String TAG = XamoomNotificationReceiver.class.getSimpleName();

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

    Intent customNotificationIntent = null;
    try {
      ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),
          PackageManager.GET_META_DATA);
      Bundle bundle = ai.metaData;
      String pushHandler = bundle.getString("XAMOOM_PUSH_HANDLE");

      Class<?> receiverClass = Class.forName(pushHandler);
      customNotificationIntent = new Intent(context, receiverClass);
    } catch (PackageManager.NameNotFoundException e) {
      // ignore
    } catch (ClassNotFoundException e) {
      Log.e(TAG, "Could not find the receiver class.");
    }

    // send PUSH_NOTIFICATION_HANDLER_NAME broadcast if registered
    if (isCustomNotificationHandlerRegistered(context, customNotificationIntent)) {
      customNotificationIntent.putExtras(pushBundle);
      sendCustomHandlerBroadcast(context, customNotificationIntent, dataObject);
      PushManagerImpl.postHandlePush(context, intent);
      return;
    }

    openDefaultActivity(context, intent, dataObject, pushBundle);
    PushManagerImpl.postHandlePush(context, intent);
  }

  /**
   * Checks if there is a broadcastreceiver registered for the
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
    prepareIntentData(intent, dataObject);
    context.sendBroadcast(intent);
  }

  /**
   * Checks if there is a contentid set and adds it to the intent.
   *
   * @param intent Intent with XamoomPushActivity.PUSH_NOTIFICATION_HANDLER_NAME action
   * @param dataObject JsonData from pushwoosh bundle.
   */
  private void prepareIntentData(Intent intent, JSONObject dataObject) {
    String userDataJsonString = null;
    try {
      userDataJsonString = (String) dataObject.get("u");
    } catch (JSONException e) {
      Log.i(TAG, "Push does not contain custom userdata.");
      return;
    }

    JSONObject userData = null;
    try {
      userData = new JSONObject(userDataJsonString);
    } catch (JSONException e) {
      Log.i(TAG, "Userdata is not a JSON String");
      return;
    }

    String contentId = null;
    try {
      contentId = userData.getString(XamoomPushActivity.CONTENT_ID_NAME);
    } catch (JSONException e) {
      Log.i(TAG, "Userdata does not contain content_id.");
      return;
    }

    intent.putExtra(XamoomPushActivity.CONTENT_ID_NAME, contentId);
  }


  private void openDefaultActivity(Context context, Intent intent, JSONObject dataObject, Bundle pushBundle) {
    //Get default launcher intent for clarity
    Intent launchIntent  = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
    launchIntent.addCategory("android.intent.category.LAUNCHER");

    launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

    //Put push notifications payload in Intent
    launchIntent.putExtras(pushBundle);
    launchIntent.putExtra(PushManager.PUSH_RECEIVE_EVENT, dataObject.toString());
    prepareIntentData(launchIntent, dataObject);

    //Start activity!
    context.startActivity(launchIntent);
  }
}
