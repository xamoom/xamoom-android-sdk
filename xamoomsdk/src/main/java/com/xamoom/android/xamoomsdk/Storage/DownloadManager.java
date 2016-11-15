package com.xamoom.android.xamoomsdk.Storage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class DownloadManager {
  private FileManager mFileManager;
  private ArrayList<DownloadTask> mDownloadTasks = new ArrayList<>();

  public DownloadManager(FileManager fileManager) {
    mFileManager = fileManager;
  }

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

  public void downloadQueriedTasks() {
    for (DownloadTask task : mDownloadTasks) {
      task.execute();
    }
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
