package com.xamoom.android.xamoomsdk.Storage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * DownloadManager is used to download and save files from Urls.
 */
public class DownloadManager {
  private FileManager mFileManager;
  private ArrayList<DownloadTask> mDownloadTasks = new ArrayList<>();

  public DownloadManager(FileManager fileManager) {
    mFileManager = fileManager;
  }

  /**
   * Will download file via a {@link DownloadTask} and save it to internal storage.
   *
   * @param url Url from remote file.
   * @param queryTasks Boolean to queue files.
   * @param completedInterface Callback when finished or failed.
   * @throws MalformedURLException
   */
  public void saveFileFromUrl(final URL url, boolean queryTasks,
                              final OnDownloadManagerCompleted completedInterface) throws MalformedURLException {
    final String urlString = url.toString();

    DownloadTask downloadTask = new DownloadTask(url, new DownloadTask.OnDownloadTaskCompleted() {
      @Override
      public void completed(ByteArrayOutputStream byteArrayOutputStream) {
        try {
          mFileManager.saveFile(urlString, byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
          if (completedInterface != null) {
            completedInterface.failed(urlString, new DownloadError(DownloadError.IO_EXCEPTION_ERROR_CODE,
                DownloadError.IO_EXCEPTION_ERROR, -1, null, e));
          }
        }

        if (completedInterface != null) {
          completedInterface.completed(urlString);
        }
      }

      @Override
      public void failed(DownloadError downloadError) {
        if (completedInterface != null) {
          completedInterface.failed(urlString, downloadError);
        }
      }
    });

    if (!queryTasks) {
      downloadTask.execute();
    } else {
      mDownloadTasks.add(downloadTask);
    }
  }

  /**
   * Will start to download all queried tasks.
   * After starting mDownloadTasks will get cleared.
   */
  public void downloadQueriedTasks() {
    for (DownloadTask task : mDownloadTasks) {
      task.execute();
    }

    mDownloadTasks.clear();
  }

  public interface OnDownloadManagerCompleted {
    void completed(String urlString);
    void failed(String urlString, DownloadError downloadError);
  }

  public ArrayList<DownloadTask> getDownloadTasks() {
    return mDownloadTasks;
  }

  public void setFileManager(FileManager fileManager) {
    mFileManager = fileManager;
  }
}
