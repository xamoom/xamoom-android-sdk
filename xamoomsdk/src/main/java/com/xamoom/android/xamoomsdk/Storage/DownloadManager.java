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
