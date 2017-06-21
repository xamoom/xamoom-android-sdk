package com.xamoom.android.pushnotifications;

import android.content.Context;
import android.util.Log;

import com.pushwoosh.fragment.PushFragment;

public class PushManager {
  private static final String TAG = PushManager.class.getSimpleName();
  private TestActivity mTestActivity;

  public PushManager(Context context) {
    Log.v(TAG, "PushManager constructor");
    mTestActivity = new TestActivity();
    PushFragment.init(mTestActivity);
  }
}
