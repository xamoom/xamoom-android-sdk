package com.xamoom.android.xamoomsdkexample;

import android.app.Notification;
import android.support.v4.app.NotificationCompat;

import com.pushwoosh.notification.PushData;
import com.xamoom.android.pushnotifications.XamoomNotificationFactory;


public class CustomNotification extends XamoomNotificationFactory {
  @Override
  public Notification onGenerateNotification(PushData pushData) {
    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getContext());
    notificationBuilder.setContentTitle("Custom " + getContentFromHtml(pushData.getHeader()));
    notificationBuilder.setContentText(getContentFromHtml(pushData.getMessage()));
    notificationBuilder.setSmallIcon(pushData.getSmallIcon());
    notificationBuilder.setTicker(getContentFromHtml(pushData.getTicker()));
    notificationBuilder.setWhen(System.currentTimeMillis());
    Notification notification = notificationBuilder.build();

    // needed to make a notification
    addSound(notification, pushData.getSound());
    addVibration(notification, pushData.getVibration());
    addCancel(notification);

    return notification;
  }
}