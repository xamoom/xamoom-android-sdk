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

public class DownloadError {
  public static final int CONNECTION_FAILED_ERROR_CODE = 0;
  public static final String CONNECTION_FAILED_ERROR = "connectionFailedError";
  public static final int CONNECTION_CANCALED_CODE = 1;
  public static final String CONNECTION_CANCELED = "connectionCanceledError";
  public static final int IO_EXCEPTION_ERROR_CODE = 2;
  public static final String IO_EXCEPTION_ERROR = "ioException";

  private int mErrorCode;
  private int mHTTPResponseCode;
  private String mErrorName;
  private String mDescription;
  private Exception mException;

  public DownloadError(int errorCode, String errorName, int HTTPResponseCode, String description,
                       Exception exception) {
    mErrorCode = errorCode;
    mHTTPResponseCode = HTTPResponseCode;
    mErrorName = errorName;
    mDescription = description;
    mException = exception;
  }

  public int getErrorCode() {
    return mErrorCode;
  }

  public void setErrorCode(int errorCode) {
    mErrorCode = errorCode;
  }

  public String getErrorName() {
    return mErrorName;
  }

  public void setErrorName(String errorName) {
    mErrorName = errorName;
  }

  public String getDescription() {
    return mDescription;
  }

  public void setDescription(String description) {
    mDescription = description;
  }

  public Exception getException() {
    return mException;
  }

  public void setException(Exception exception) {
    mException = exception;
  }

  public int getHTTPResponseCode() {
    return mHTTPResponseCode;
  }

  public void setHTTPResponseCode(int HTTPResponseCode) {
    mHTTPResponseCode = HTTPResponseCode;
  }
}
