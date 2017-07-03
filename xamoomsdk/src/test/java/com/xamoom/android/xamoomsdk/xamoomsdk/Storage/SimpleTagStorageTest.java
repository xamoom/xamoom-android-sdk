/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomsdk.xamoomsdk.Storage;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.Storage.SimpleTagStorage;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class SimpleTagStorageTest {
  private SimpleTagStorage mSimpleTagStorage;
  private SharedPreferences mMockedPreferences;
  private SharedPreferences.Editor mMockedEditor;

  @SuppressLint("CommitPrefEdits")
  @Before
  public void setup() {
    mMockedPreferences = mock(SharedPreferences.class);
    mMockedEditor = mock(SharedPreferences.Editor.class);
    Mockito.stub(mMockedPreferences.edit()).toReturn(mMockedEditor);

    mSimpleTagStorage =
        new SimpleTagStorage(RuntimeEnvironment.application.getApplicationContext());
    mSimpleTagStorage.setSharedPreferences(mMockedPreferences);
  }

  @Test
  public void testSaveTags() {
    ArrayList<String> tags = new ArrayList<>();
    tags.add("tag1");
    tags.add("tag2");

    mSimpleTagStorage.saveTags(tags);

    Mockito.verify(mMockedEditor).putStringSet(eq("com.xamoom.android.sdk.offlineTags"),
        eq(new HashSet<String>(tags)));

  }

  @Test
  public void testGetTags() {
    ArrayList<String> tags = new ArrayList<>();
    tags.add("tag1");
    tags.add("tag2");

    Mockito.stub(mMockedPreferences.getStringSet(eq("com.xamoom.android.sdk.offlineTags"), (Set<String>) any()))
        .toReturn(new HashSet<String>(tags));

    ArrayList<String> savedTags = mSimpleTagStorage.getTags();

    Assert.assertEquals(tags, savedTags);
  }

}
