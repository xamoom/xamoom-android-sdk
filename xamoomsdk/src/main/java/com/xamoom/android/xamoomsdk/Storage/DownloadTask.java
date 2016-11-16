package com.xamoom.android.xamoomsdk.Storage;

import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class DownloadTask extends AsyncTask<Void, Integer, ByteArrayOutputStream> {
  private OnDownloadTaskCompleted mListener;
  private URL mURL;

  public DownloadTask(URL url, OnDownloadTaskCompleted listener) {
    mURL = url;
    mListener = listener;
  }

  @Override
  protected ByteArrayOutputStream doInBackground(Void... voids) {
    InputStream input = null;
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    HttpURLConnection connection = null;

    try {
      connection = (HttpURLConnection) mURL.openConnection();
      connection.connect();

      if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
        if (mListener != null) {
          mListener.failed(new DownloadError(DownloadError.CONNECTION_FAILED_ERROR_CODE,
              DownloadError.CONNECTION_FAILED_ERROR, connection.getResponseCode(), null, null));
        }
      }

      int fileLength = connection.getContentLength();

      input = connection.getInputStream();

      byte data[] = new byte[4096];
      long total = 0;
      int count;

      while ((count = input.read(data)) != -1) {
        // allow canceling with back button
        if (isCancelled()) {
          input.close();
          if (mListener != null) {
            mListener.failed(new DownloadError(DownloadError.CONNECTION_CANCALED_CODE,
                DownloadError.CONNECTION_CANCELED, -1, null, null));
          }
          return null;
        }

        total += count;
        // publishing the progress....
        if (fileLength > 0) // only if total length is known
          publishProgress((int) (total * 100 / fileLength));

        buffer.write(data, 0, count);
      }
    } catch (Exception e) {
      if (mListener != null) {
        mListener.failed(new DownloadError(DownloadError.IO_EXCEPTION_ERROR_CODE,
            DownloadError.IO_EXCEPTION_ERROR, -1, null, e));
      }
    } finally {
      if (connection == null) {
        mListener.failed(null);
        return null;
      }

      if (mListener != null) {
        mListener.completed(buffer);
        return buffer;
      }

      if (connection != null) {
        connection.disconnect();
      }
    }
    return null;
  }

  @Override
  protected void onPostExecute(ByteArrayOutputStream byteArrayOutputStream) {
    super.onPostExecute(byteArrayOutputStream);
  }

  public interface OnDownloadTaskCompleted {
    void completed(ByteArrayOutputStream byteArrayOutputStream);
    void failed(DownloadError downloadError);
  }
}
