package com.xamoom.android.xamoomsdk.xamoomsdk.Storage;

import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.Storage.DownloadError;
import com.xamoom.android.xamoomsdk.Storage.DownloadManager;
import com.xamoom.android.xamoomsdk.Storage.DownloadTask;
import com.xamoom.android.xamoomsdk.Storage.FileManager;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.concurrent.Semaphore;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class DownloadManagerTest {

  private HttpURLConnection mMockedURLConnection;
  private InputStream mMockedInputStream;
  private URL mURL;
  private FileManager mMockedFileManager;

  @Before
  public void setup() throws IOException {
    mMockedURLConnection = Mockito.mock(HttpURLConnection.class);
    mMockedInputStream = Mockito.mock(InputStream.class);

    Mockito.stub(mMockedURLConnection.getInputStream()).toReturn(mMockedInputStream);
    URLStreamHandler handler = new URLStreamHandler() {
      @Override
      protected URLConnection openConnection(final URL arg0)
          throws IOException {
        return mMockedURLConnection;
      }
    };
    mURL = new URL("http", "foo.bar", 80, "", handler);

    mMockedFileManager = Mockito.mock(FileManager.class);
  }

  @Test
  public void testSaveFileFromUrlWithQuery() throws IOException, InterruptedException {
    DownloadManager manager = new DownloadManager(mMockedFileManager);

    manager.saveFileFromUrl(mURL, true, new DownloadManager.OnDownloadManagerCompleted() {
      @Override
      public void completed() {
      }

      @Override
      public void failed(DownloadError error) {
        Assert.fail();
      }
    });

    Assert.assertEquals(manager.getDownloadTasks().size(), 1);
  }

  @Test
  public void testSaveFileFromUrl() throws IOException, InterruptedException {
    Mockito.stub(mMockedURLConnection.getResponseCode()).toReturn(HttpURLConnection.HTTP_OK);
    Mockito.stub(mMockedURLConnection.getContentLength()).toReturn(1000);
    Mockito.stub(mMockedInputStream.read(any(byte[].class))).toReturn(1000).toReturn(-1);

    DownloadManager manager = new DownloadManager(mMockedFileManager);

    final Semaphore semaphore = new Semaphore(0);
    manager.saveFileFromUrl(mURL, false, new DownloadManager.OnDownloadManagerCompleted() {
      @Override
      public void completed() {
        semaphore.release();
      }

      @Override
      public void failed(DownloadError error) {
        Assert.fail();
      }
    });
    semaphore.acquire();

    Mockito.verify(mMockedFileManager).saveFile(anyString(), any(byte[].class));
  }

  @Test
  public void testDownloadQueriedTasks() {
    DownloadManager manager = new DownloadManager(mMockedFileManager);

    DownloadTask mockedTask = Mockito.mock(DownloadTask.class);
    manager.getDownloadTasks().add(mockedTask);

    manager.downloadQueriedTasks();

    Mockito.verify(mockedTask).execute();
  }
}
