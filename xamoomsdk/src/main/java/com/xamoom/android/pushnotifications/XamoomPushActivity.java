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
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.pushwoosh.PushManager;
import com.pushwoosh.fragment.PushEventListener;
import com.pushwoosh.fragment.PushFragment;

public class XamoomPushActivity extends AppCompatActivity implements PushEventListener {
  public static final String PUSH_NOTIFICATION_HANDLER_NAME = "XAMOOM_PUSH_HANDLER";
  public static final String CONTENT_ID_NAME = "content_id";
  private static final String TAG = XamoomPushActivity.class.getSimpleName();


  public void registerNotificationFactory(XamoomNotificationFactory factory) {
    PushManager.getInstance(getApplicationContext()).setNotificationFactory(factory);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    PushFragment.init(this);
    registerNotificationFactory(new XamoomNotificationFactory());
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    PushFragment.onNewIntent(this, intent);
  }

  @Override
  public void doOnUnregisteredError(String s) {
    Log.v(TAG, "doOnUnregisteredError: " + s);
  }

  @Override
  public void doOnRegisteredError(String s) {
    Log.v(TAG, "doOnRegisteredError: " + s);
  }

  @Override
  public void doOnRegistered(String s) {
    Log.v(TAG, "doOnRegistered: " + s);
  }

  @Override
  public void doOnMessageReceive(String s) {
    Log.v(TAG, "doOnMessageReceive: " + s);
  }

  @Override
  public void doOnUnregistered(String s) {
    Log.v(TAG, "doOnUnregistered: " + s);
  }
}
