package com.xamoom.android.pushnotifications;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.pushwoosh.fragment.PushEventListener;
import com.pushwoosh.fragment.PushFragment;

public class TestActivity extends AppCompatActivity implements PushEventListener {
  private static final String TAG = TestActivity.class.getSimpleName();


  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    PushFragment.init(this);
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
