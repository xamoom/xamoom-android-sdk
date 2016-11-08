package com.xamoom.android.xamoomsdk.xamoomsdk.Offline;

import com.xamoom.android.xamoomsdk.APICallback;
import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.Offline.OfflineEnduserApi;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Storage.OfflineStorageManager;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;

import at.rags.morpheus.Error;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.contains;
import static org.mockito.Matchers.eq;

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
}
