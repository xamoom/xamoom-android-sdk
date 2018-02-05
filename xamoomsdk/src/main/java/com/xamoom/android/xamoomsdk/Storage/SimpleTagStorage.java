/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomsdk.Storage;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Sharedpreferences wrapper for saving spots offline.
 */
public class SimpleTagStorage {
  private final static String OFFLINE_TAG_SHARED_PREFERENCES_NAME =
      "com.xamoom.android.sdk.sharedpreferences";
  private final static String OFFLINE_TAG_KEY = "com.xamoom.android.sdk.offlineTags";

  private SharedPreferences mSharedPreferences;

  public SimpleTagStorage(Context context) {
    mSharedPreferences = context.getSharedPreferences(OFFLINE_TAG_SHARED_PREFERENCES_NAME,
        Context.MODE_PRIVATE);
  }

  public void saveTags(ArrayList<String> tags) {
    SharedPreferences.Editor editor = mSharedPreferences.edit();
    Set<String> tagSet = new HashSet<>(tags);
    editor.putStringSet(OFFLINE_TAG_KEY, tagSet);
    editor.apply();
  }

  public ArrayList<String> getTags() {
    Set<String> tagSet = mSharedPreferences.getStringSet(OFFLINE_TAG_KEY, null);

    ArrayList<String> tags = null;
    if (tagSet == null) {
      tags = new ArrayList<>();
    } else {
      tags = new ArrayList<>(tagSet);
    }

    return tags;
  }

  public void setSharedPreferences(SharedPreferences sharedPreferences) {
    mSharedPreferences = sharedPreferences;
  }
}
