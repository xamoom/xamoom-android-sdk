/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomsdk.xamoomsdk.Offline;

import android.annotation.SuppressLint;
import android.location.Location;

import com.xamoom.android.xamoomsdk.APICallback;
import com.xamoom.android.xamoomsdk.APIListCallback;
import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.Offline.OfflineEnduserApi;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.Menu;
import com.xamoom.android.xamoomsdk.Resource.Spot;
import com.xamoom.android.xamoomsdk.Resource.System;
import com.xamoom.android.xamoomsdk.Resource.SystemSetting;
import com.xamoom.android.xamoomsdk.Storage.OfflineStorageManager;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.Semaphore;

import at.rags.morpheus.Error;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.notNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class OfflineEnduserApiTest {

  private OfflineEnduserApi mOfflineEnduserApi;
  private OfflineStorageManager mMockedOfflineStorageManager;

  @Before
  public void setup() {
    mOfflineEnduserApi = new OfflineEnduserApi(RuntimeEnvironment.application);
    mMockedOfflineStorageManager = Mockito.mock(OfflineStorageManager.class);

    mOfflineEnduserApi.setOfflineStorageManager(mMockedOfflineStorageManager);
  }

  @Test
  public void testGetContent() {
    final Content savedContent = new Content();
    savedContent.setId("1");

    Mockito.stub(mMockedOfflineStorageManager.getContent(eq("1234"))).toReturn(savedContent);

    mOfflineEnduserApi.getContent("1234", new APICallback<Content, List<Error>>() {
      @Override
      public void finished(Content result) {
        Assert.assertEquals(result, savedContent);
      }

      @Override
      public void error(List<Error> error) {

      }
    });

    Mockito.verify(mMockedOfflineStorageManager).getContent(eq("1234"));
  }

  @Test
  public void testGetContentByLocationIdentifier() {
    final Content savedContent = new Content();
    savedContent.setId("1");

    Mockito.stub(mMockedOfflineStorageManager.getContentWithLocationIdentifier(anyString()))
        .toReturn(savedContent);

    mOfflineEnduserApi.getContentByLocationIdentifier("locId", new APICallback<Content, List<Error>>() {
      @Override
      public void finished(Content result) {
        Assert.assertEquals(savedContent, result);
      }

      @Override
      public void error(List<Error> error) {
        Assert.fail();
      }
    });
  }

  @SuppressLint("DefaultLocale")
  @Test
  public void testGetContentByLocationIdentifierBeacon() {
    final Content savedContent = new Content();
    savedContent.setId("1");

    Mockito.stub(mMockedOfflineStorageManager.getContentWithLocationIdentifier(anyString()))
        .toReturn(savedContent);

    mOfflineEnduserApi.getContentByBeacon(1, 2, new APICallback<Content, List<Error>>() {
      @Override
      public void finished(Content result) {
        Assert.assertEquals(savedContent, result);
      }

      @Override
      public void error(List<Error> error) {
        Assert.fail();
      }
    });

    Mockito.verify(mMockedOfflineStorageManager).getContentWithLocationIdentifier(eq(String.format("%d|%d", 1, 2)));
  }

  @Test
  public void testGetContentsByLocation() {
    mOfflineEnduserApi.getContentsByLocation(null, 1, null, null, new APIListCallback<List<Content>, List<Error>>() {
      @Override
      public void finished(List<Content> result, String cursor, boolean hasMore) {
        Assert.assertNotNull(result);
      }

      @Override
      public void error(List<Error> error) {

      }
    });

    Mockito.verify(mMockedOfflineStorageManager).getContentsByLocation(any(Location.class),
        anyInt(), anyString(), any(EnumSet.class), (APIListCallback<List<Content>, List<Error>>) any(APICallback.class));
  }

  @Test
  public void testGetContentsByTags() {
    mOfflineEnduserApi.getContentsByTags(null, 1, null, null, new APIListCallback<List<Content>, List<Error>>() {
      @Override
      public void finished(List<Content> result, String cursor, boolean hasMore) {
        Assert.assertNotNull(result);
      }

      @Override
      public void error(List<Error> error) {

      }
    });

    Mockito.verify(mMockedOfflineStorageManager).getContentByTags(any(List.class), anyInt(),
        anyString(), any(EnumSet.class), any (APIListCallback.class));
  }

  @Test
  public void testSearchContentsByName() {
    mOfflineEnduserApi.searchContentsByName("test", 1, null, null, new APIListCallback<List<Content>, List<Error>>() {
      @Override
      public void finished(List<Content> result, String cursor, boolean hasMore) {
        Assert.assertNotNull(result);
      }

      @Override
      public void error(List<Error> error) {

      }
    });

    Mockito.verify(mMockedOfflineStorageManager).searchContentsByName(eq("test"), anyInt(), anyString(),
        any(EnumSet.class), (APIListCallback<List<Content>, List<Error>>) any(APICallback.class));
  }

  @Test
  public void testGetSpot() throws InterruptedException {
    Mockito.stub(mMockedOfflineStorageManager.getSpot(anyString())).toReturn(new Spot());

    final Semaphore semaphore = new Semaphore(0);
    mOfflineEnduserApi.getSpot("1", new APICallback<Spot, List<Error>>() {
      @Override
      public void finished(Spot result) {
        semaphore.release();
      }

      @Override
      public void error(List<Error> error) {

      }
    });
    semaphore.acquire();

    Mockito.verify(mMockedOfflineStorageManager).getSpot(eq("1"));
  }

  @Test
  public void testGetSpotsByLocationWithPaging() throws InterruptedException {
    mOfflineEnduserApi.getSpotsByLocation(null, 1, 10, null, null, null, null);

    Mockito.verify(mMockedOfflineStorageManager).getSpotsByLocation(any(Location.class), anyInt(),
        anyInt(), anyString(), any(EnumSet.class),
        (APIListCallback<List<Spot>, List<Error>>) any(APICallback.class));
  }

  @Test
  public void getGetSpotsByLocationWithWithoutPaging() throws InterruptedException {
    mOfflineEnduserApi.getSpotsByLocation(null, 1, null, null, null);

    Mockito.verify(mMockedOfflineStorageManager).getSpotsByLocation(any(Location.class), anyInt(),
        anyInt(), anyString(), any(EnumSet.class),
        (APIListCallback<List<Spot>, List<Error>>) any(APICallback.class));
  }

  @Test
  public void testGetSpotsByTags() throws InterruptedException {
    mOfflineEnduserApi.getSpotsByTags(null, null, null, null);

    Mockito.verify(mMockedOfflineStorageManager).getSpotsByTags(anyList(), anyInt(), anyString(),
        any(EnumSet.class),
        (APIListCallback<List<Spot>, List<Error>>) any(APICallback.class));
  }

  @Test
  public void testGetSpotsByTagsWithPaging() throws InterruptedException {
    mOfflineEnduserApi.getSpotsByTags(null, 10, null, null, null, null);

    Mockito.verify(mMockedOfflineStorageManager).getSpotsByTags(anyList(), anyInt(), anyString(),
        any(EnumSet.class),
        (APIListCallback<List<Spot>, List<Error>>) any(APICallback.class));
  }

  @Test
  public void testSearchSpotsByName() {
    mOfflineEnduserApi.searchSpotsByName("1", 10, null, null, null, null);

    Mockito.verify(mMockedOfflineStorageManager).searchSpotsByName(anyString(), anyInt(),
        anyString(), any(EnumSet.class),
        (APIListCallback<List<Spot>, List<Error>>) any(APICallback.class));
  }

  @Test
  public void testGetSystem() throws InterruptedException {
    Mockito.stub(mMockedOfflineStorageManager.getSystem()).toReturn(new System());

    final Semaphore semaphore = new Semaphore(0);
    mOfflineEnduserApi.getSystem(new APICallback<System, List<Error>>() {
      @Override
      public void finished(System result) {
        semaphore.release();
      }

      @Override
      public void error(List<Error> error) {

      }
    });
    semaphore.acquire();

    Mockito.verify(mMockedOfflineStorageManager).getSystem();
  }

  @Test
  public void testGetMenu() throws InterruptedException {
    Mockito.stub(mMockedOfflineStorageManager.getMenu(anyString())).toReturn(new Menu());

    final Semaphore semaphore = new Semaphore(0);
    mOfflineEnduserApi.getMenu("1", new APICallback<Menu, List<Error>>() {
      @Override
      public void finished(Menu result) {
        semaphore.release();
      }

      @Override
      public void error(List<Error> error) {

      }
    });
    semaphore.acquire();

    Mockito.verify(mMockedOfflineStorageManager).getMenu(eq("1"));
  }

  @Test
  public void testGetSetting() throws InterruptedException {
    Mockito.stub(mMockedOfflineStorageManager.getSystemSetting(anyString())).toReturn(new SystemSetting());

    final Semaphore semaphore = new Semaphore(0);
    mOfflineEnduserApi.getSystemSetting("1", new APICallback<SystemSetting, List<Error>>() {
      @Override
      public void finished(SystemSetting result) {
        semaphore.release();
      }

      @Override
      public void error(List<Error> error) {

      }
    });
    semaphore.acquire();

    Mockito.verify(mMockedOfflineStorageManager).getSystemSetting(eq("1"));
  }
}
