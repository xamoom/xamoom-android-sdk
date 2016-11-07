package com.xamoom.android.xamoomsdk.xamoomsdk.Storage;

import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.Storage.DownloadError;
import com.xamoom.android.xamoomsdk.Storage.DownloadTask;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.concurrent.Semaphore;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class DownloadTaskTest {
  private HttpURLConnection mMockedURLConnection;
  private InputStream mMockedInputStream;
  private URL mURL;

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

    mURL = new URL("http://foo.bar", "foo.bar", 80, "", handler);
  }

  @Test
  public void testDownloadTask() throws InterruptedException, IOException {
    Mockito.stub(mMockedURLConnection.getResponseCode()).toReturn(HttpURLConnection.HTTP_OK);
    Mockito.stub(mMockedURLConnection.getContentLength()).toReturn(1000);
    Mockito.stub(mMockedInputStream.read(any(byte[].class))).toReturn(1000).toReturn(-1);

    final Semaphore semaphore = new Semaphore(0);
    DownloadTask task = new DownloadTask(mURL, new DownloadTask.OnDownloadTaskCompleted() {
      @Override
      public void completed(ByteArrayOutputStream byteArrayOutputStream) {
        Assert.assertEquals(byteArrayOutputStream.size(), 1000);
        semaphore.release();
      }

      @Override
      public void failed(DownloadError downloadError) {
        Assert.fail();
        semaphore.release();
      }
    });

    task.execute();
    semaphore.acquire();

    Mockito.verify(mMockedInputStream, times(2)).read(any(byte[].class));
    Mockito.verify(mMockedURLConnection).getResponseCode();
    Mockito.verify(mMockedURLConnection).getInputStream();
  }
}
