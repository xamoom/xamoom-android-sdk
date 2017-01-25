/*
* Copyright 2017 by xamoom GmbH <apps@xamoom.com>
*
* This file is part of some open source application.
*
* Some open source application is free software: you can redistribute
* it and/or modify it under the terms of the GNU General Public
* License as published by the Free Software Foundation, either
* version 2 of the License, or (at your option) any later version.
*
* Some open source application is distributed in the hope that it will
* be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
* of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with xamoom-android-sdk. If not, see <http://www.gnu.org/licenses/>.
*
* author: Raphael Seher <raphael@xamoom.com>
*/

package com.xamoom.android.xamoomsdk.Storage;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Array;
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
