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

package com.xamoom.android.xamoomsdk.xamoomsdk.Storage;

import com.xamoom.android.xamoomsdk.APICallback;
import com.xamoom.android.xamoomsdk.APIListCallback;
import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.Spot;
import com.xamoom.android.xamoomsdk.Storage.Database.SpotDatabaseAdapter;
import com.xamoom.android.xamoomsdk.Storage.OfflineStorageManager;
import com.xamoom.android.xamoomsdk.Storage.OfflineStorageTagModule;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.Semaphore;

import at.rags.morpheus.Error;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class OfflineStorageTagModuleTest {

  private OfflineStorageTagModule mOfflineStorageTagModule;
  private OfflineStorageManager mMockedManager;
  private EnduserApi mMockedApi;

  @Before
  public void setup() {
    mMockedManager = mock(OfflineStorageManager.class);
    mMockedApi = mock(EnduserApi.class);
    mOfflineStorageTagModule = new OfflineStorageTagModule(mMockedManager, mMockedApi);
  }

  @Test
  public void testConstructor() {
    OfflineStorageTagModule module = new OfflineStorageTagModule(mMockedManager, mMockedApi);

    Assert.assertEquals(mMockedManager, module.getOfflineStorageManager());
    Assert.assertEquals(mMockedApi, module.getEnduserApi());
    Assert.assertNotNull(module.getOfflineTags());
  }

  @Test
  public void testDownloadAndSaveWithTags() throws InterruptedException {
    final boolean[] firstTime = {true};
    ArrayList<String> tags = new ArrayList<>();
    tags.add("tag1");

    final ArrayList<Spot> spots = new ArrayList<>();
    final Content content = new Content();
    content.setId("1");
    Spot spot = new Spot();
    spot.setContent(content);
    spots.add(spot);

    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        APIListCallback<List<Spot>, List<Error>> callback =
            (APIListCallback<List<Spot>, List<Error>>) invocation.getArguments()[5];

        if (firstTime[0]) {
          firstTime[0] = false;
          callback.finished(spots, "1", true);
        } else {
          callback.finished(spots, "2", false);
        }
        return null;
      }
    }).when(mMockedApi).getSpotsByTags(anyList(), anyInt(), anyString(), any(EnumSet.class),
        any(EnumSet.class), (APIListCallback<List<Spot>, List<Error>>) any());

    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        APICallback<Content, List<Error>> callback =
            (APICallback<Content, List<Error>>) invocation.getArguments()[1];
        callback.finished(content);
        return null;
      }
    }).when(mMockedApi).getContent(anyString(), (APICallback<Content, List<Error>>) any());

    final Semaphore semaphore = new Semaphore(0);
    mOfflineStorageTagModule.downloadAndSaveWithTags(tags, false,
        new APIListCallback<List<Spot>, List<Error>>() {
      @Override
      public void finished(List<Spot> result, String cursor, boolean hasMore) {
        semaphore.release();
      }

      @Override
      public void error(List<Error> error) {

      }
    }, null);
    semaphore.acquire();

    Assert.assertEquals(1, mOfflineStorageTagModule.getOfflineTags().size());
    Mockito.verify(mMockedApi, times(2)).getSpotsByTags(eq(tags), eq(100), anyString(),
        any(EnumSet.class), any(EnumSet.class), (APIListCallback<List<Spot>, List<Error>>) any());
    Mockito.verify(mMockedApi, times(2)).getContent(eq(content.getId()),
        (APICallback<Content, List<Error>>) any());
  }

  @Test
  public void testDeleteWithTags() {
    final ArrayList<Spot> spots = new ArrayList<>();
    final ArrayList<Content> contents = new ArrayList<>();

    ArrayList<String> tags1 = new ArrayList<>();
    tags1.add("tag1");

    ArrayList<String> tags12 = new ArrayList<>();
    tags12.add("tag1");
    tags12.add("tag2");

    Content content1 = new Content();
    content1.setId("1");
    content1.setTags(tags1);
    contents.add(content1);

    Content content12 = new Content();
    content12.setId("2");
    content12.setTags(tags12);
    contents.add(content12);

    Spot spot1 = new Spot();
    spot1.setId("3");
    spot1.setTags(tags1);
    spot1.setContent(content1);
    spots.add(spot1);

    Spot spot12 = new Spot();
    spot12.setId("4");
    spot12.setTags(tags12);
    spot12.setContent(content12);
    spots.add(spot12);

    mOfflineStorageTagModule.getOfflineTags().addAll(tags12);

    SpotDatabaseAdapter mockedSpotDatabaseAdapter = Mockito.mock(SpotDatabaseAdapter.class);
    Mockito.stub(mockedSpotDatabaseAdapter.getAllSpots()).toReturn(spots);
    Mockito.stub(mMockedManager.getSpotDatabaseAdapter()).toReturn(mockedSpotDatabaseAdapter);
    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        APIListCallback<List<Spot>, List<Error>> callback =
            (APIListCallback<List<Spot>, List<Error>>) invocation.getArguments()[4];
        callback.finished(spots, null, false);
        return null;
      }
    }).when(mMockedManager).getSpotsByTags(any(ArrayList.class), anyInt(), anyString(),
        any(EnumSet.class), (APIListCallback<List<Spot>, List<Error>>) any());

    mOfflineStorageTagModule.deleteWithTags(tags1);

    Mockito.verify(mMockedManager, times(1)).deleteSpot(eq("3"));
    Mockito.verify(mMockedManager, times(0)).deleteSpot(eq("4"));
    Mockito.verify(mMockedManager, times(1)).deleteContent(eq("1"));
    Mockito.verify(mMockedManager, times(0)).deleteContent(eq("2"));
  }

  @Test
  public void testDeleteWithTagsAll() throws IOException {
    final ArrayList<Spot> spots = new ArrayList<>();
    final ArrayList<Content> contents = new ArrayList<>();

    ArrayList<String> tags1 = new ArrayList<>();
    tags1.add("tag1");

    ArrayList<String> tags12 = new ArrayList<>();
    tags12.add("tag1");
    tags12.add("tag2");

    Content content1 = new Content();
    content1.setId("1");
    content1.setTags(tags1);
    contents.add(content1);

    Content content12 = new Content();
    content12.setId("2");
    content12.setTags(tags12);
    contents.add(content12);

    Spot spot1 = new Spot();
    spot1.setId("3");
    spot1.setTags(tags1);
    spot1.setContent(content1);
    spot1.setPublicImageUrl("www.xamoom.com/file.jpg");
    spots.add(spot1);

    Spot spot12 = new Spot();
    spot12.setId("4");
    spot12.setTags(tags12);
    spot12.setContent(content12);
    spots.add(spot12);

    mOfflineStorageTagModule.getOfflineTags().addAll(tags12);

    SpotDatabaseAdapter mockedSpotDatabaseAdapter = Mockito.mock(SpotDatabaseAdapter.class);
    Mockito.stub(mockedSpotDatabaseAdapter.getAllSpots()).toReturn(spots);
    Mockito.stub(mMockedManager.getSpotDatabaseAdapter()).toReturn(mockedSpotDatabaseAdapter);

    doAnswer(new Answer() {
      @Override
      public Object answer(InvocationOnMock invocation) throws Throwable {
        APIListCallback<List<Spot>, List<Error>> callback =
            (APIListCallback<List<Spot>, List<Error>>) invocation.getArguments()[4];
        callback.finished(spots, null, false);
        return null;
      }
    }).when(mMockedManager).getSpotsByTags(any(ArrayList.class), anyInt(), anyString(),
        any(EnumSet.class), (APIListCallback<List<Spot>, List<Error>>) any());

    mOfflineStorageTagModule.deleteWithTags(tags12);

    Mockito.verify(mMockedManager, times(1)).deleteSpot(eq("3"));
    Mockito.verify(mMockedManager, times(1)).deleteSpot(eq("4"));
    Mockito.verify(mMockedManager, times(1)).deleteContent(eq("1"));
    Mockito.verify(mMockedManager, times(1)).deleteContent(eq("2"));
    Mockito.verify(mMockedManager, times(1)).deleteFile(eq(spot1.getPublicImageUrl()), eq(true));
  }
}
