/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.pushnotifications;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.pushwoosh.notification.AbsNotificationFactory;
import com.pushwoosh.notification.PushData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class XamoomNotificationFactory extends AbsNotificationFactory {
  private static final String TAG = XamoomNotificationFactory.class.getSimpleName();

  @Override
  public Notification onGenerateNotification(PushData pushData) {
    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getContext());
    notificationBuilder.setContentTitle(getContentFromHtml(pushData.getHeader()));
    notificationBuilder.setContentText(getContentFromHtml(pushData.getMessage()));
    notificationBuilder.setSmallIcon(pushData.getSmallIcon());
    notificationBuilder.setTicker(getContentFromHtml(pushData.getTicker()));
    notificationBuilder.setWhen(System.currentTimeMillis());
    addRemoteActions(notificationBuilder, pushData);

    if (pushData.getBigPicture() != null) {
      notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(pushData.getBigPicture()).setSummaryText(getContentFromHtml(pushData.getMessage())));
    } else {
      notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(getContentFromHtml(pushData.getMessage())));
    }

    if (pushData.getIconBackgroundColor() != null) {
      notificationBuilder.setColor(pushData.getIconBackgroundColor());
    }

    if (null != pushData.getLargeIcon()) {
      notificationBuilder.setLargeIcon(pushData.getLargeIcon());
    }

    Notification notification = notificationBuilder.build();

    addSound(notification, pushData.getSound());
    addVibration(notification, pushData.getVibration());
    addCancel(notification);

    return notification;
  }

  private void addRemoteActions(NotificationCompat.Builder notificationBuilder, PushData pushData) {
    String actions = pushData.getExtras().getString("my_actions");
    if (actions != null) {
      try {
        JSONArray jsonArray = new JSONArray(actions);
        for (int i = 0; i < jsonArray.length(); ++i) {
          JSONObject json = jsonArray.getJSONObject(i);
          String title = json.getString("title");
          String url = json.getString("url");
          Intent actionIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
          notificationBuilder.addAction(new NotificationCompat.Action(0, title,
              PendingIntent.getActivity(getContext(), 0, actionIntent,
                  PendingIntent.FLAG_UPDATE_CURRENT)));
        }
      } catch (JSONException e) {
        Log.v(TAG, "my_actions is not an json array.");
      }
    }
  }

  @Override
  public void onPushReceived(PushData pushData) {
  }

  @Override
  public void onPushHandle(Activity activity) {
  }
}