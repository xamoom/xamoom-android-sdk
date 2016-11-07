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
          completedInterface.failed(new DownloadError(DownloadError.IO_EXCEPTION_ERROR_CODE,
              DownloadError.IO_EXCEPTION_ERROR, -1, null, e));
        }
        completedInterface.completed();
      }

      @Override
      public void failed(DownloadError downloadError) {
        completedInterface.failed(downloadError);
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
    void completed();
    void failed(DownloadError downloadError);
  }

  public ArrayList<DownloadTask> getDownloadTasks() {
    return mDownloadTasks;
  }

  public void setFileManager(FileManager fileManager) {
    mFileManager = fileManager;
  }
}
