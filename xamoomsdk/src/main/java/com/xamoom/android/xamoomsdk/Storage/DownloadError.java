/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomsdk.Storage;

/**
 * Errors for DownloadTask.
 */
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
