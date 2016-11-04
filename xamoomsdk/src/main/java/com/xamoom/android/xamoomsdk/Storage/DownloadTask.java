package com.xamoom.android.xamoomsdk.Storage;

import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class DownloadTask extends AsyncTask<URL, Integer, ByteArrayOutputStream> {
  private OnDownloadTaskCompleted mListener;

  public DownloadTask(OnDownloadTaskCompleted listener) {
    mListener = listener;
  }

  @Override
  protected ByteArrayOutputStream doInBackground(URL... sUrl) {
    InputStream input = null;
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    HttpURLConnection connection = null;

    try {
      URL url = sUrl[0];
      connection = (HttpURLConnection) url.openConnection();
      connection.connect();

      // expect HTTP 200 OK, so we don't mistakenly save error report
      // instead of the file
      if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
        return null;
      }

      // this will be useful to display download percentage
      // might be -1: server did not report the length
      int fileLength = connection.getContentLength();

      // download the file
      input = connection.getInputStream();
      //output = new FileOutputStream("/sdcard/file_name.extension");

      byte data[] = new byte[4096];
      long total = 0;
      int count;

      while ((count = input.read(data)) != -1) {
        // allow canceling with back button
        if (isCancelled()) {
          input.close();
          mListener.failed();
          return null;
        }

        total += count;
        // publishing the progress....
        if (fileLength > 0) // only if total length is known
          publishProgress((int) (total * 100 / fileLength));

        buffer.write(data, 0, count);
      }
    } catch (Exception e) {
      mListener.failed();
    } finally {
      mListener.completed(buffer);
      if (connection != null)
        connection.disconnect();
    }
    return null;
  }

  @Override
  protected void onPostExecute(ByteArrayOutputStream byteArrayOutputStream) {
    super.onPostExecute(byteArrayOutputStream);
  }

  public interface OnDownloadTaskCompleted {
    void completed(ByteArrayOutputStream byteArrayOutputStream);
    void failed();
  }
}
