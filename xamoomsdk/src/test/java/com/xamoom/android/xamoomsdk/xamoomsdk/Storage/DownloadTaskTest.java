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
