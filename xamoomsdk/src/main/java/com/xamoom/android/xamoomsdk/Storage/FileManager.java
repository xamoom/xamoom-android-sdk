/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomsdk.Storage;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * FileManager to save and retriev data from disk.
 */
public class FileManager {
  private static FileManager mInstance;
  private Context mContext;

  public static FileManager getInstance(Context context) {
    if (mInstance == null) {
      mInstance = new FileManager(context);
    }

    return mInstance;
  }

  public FileManager(Context context) {
    mContext = context;
  }

  /**
   * Saves file to internal storage.
   *
   * @param urlString Url as string to create fileName.
   * @param bytes Bytes to save.
   * @throws IOException if not able to open file.
   */
  public void saveFile(String urlString, byte[] bytes) throws IOException {
    FileOutputStream outputStream;
    String fileName = getFileName(urlString);

    outputStream = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
    outputStream.write(bytes);
    outputStream.close();
  }

  /**
   * Deletes file with urlString.
   *
   * @param urlString Url as string to create fileName.
   * @return True if deleted.
   * @throws IOException if not able to open file.
   */
  public boolean deleteFile(String urlString) throws IOException {
    File file = getFile(urlString);

    boolean deleted = file.delete();
    return deleted;
  }

  /**
   * Returns file from internal storage.
   *
   * @param urlString Url as string to create fileName.
   * @return Saved file.
   * @throws IOException if not able to open file.
   */
  public File getFile(String urlString) throws IOException {
    File file = new File(mContext.getFilesDir(), getFileName(urlString));
    return file;
  }

  /**
   * Returns fileName of saved file on internal storage.
   *
   * @param urlString Url as string to create fileName.
   * @return fileName of saved file.
   */
  public String getFileName(String urlString) {
    if (urlString == null) {
      return null;
    }

    String urlStringWithoutCaching = FileManager.removeQuery(urlString);
    Uri url = Uri.parse(urlStringWithoutCaching);
    String fileName = url.getLastPathSegment();
    String[] fileNameSegments = fileName.split("\\.");
    String extension = "";
    if (fileNameSegments != null && fileNameSegments.length > 1) {
      extension = fileNameSegments[1];
    }

    String newFileName = String.format("%s.%s", md5(urlStringWithoutCaching), extension);
    return newFileName;
  }

  /**
   * Returns filePath of saved file on internal storage.
   *
   * @param urlString Url as string to create fileName.
   * @return filePath as String.
   */
  public String getFilePath(String urlString) {
    if (urlString == null) {
      return null;
    }

    return String.format("%s/%s", mContext.getFilesDir().getAbsolutePath(), getFileName(urlString));
  }

  /**
   * Remove query from url.
   *
   * @param urlString Url of file as string.
   * @return Url without query parameters.
   */
  public static String removeQuery(String urlString) {
    if (urlString == null) {
      return null;
    }

    Uri url = Uri.parse(urlString);
    return urlString.replace("?" + url.getQuery(), "");
  }

  private static String md5(String s) {
    MessageDigest digest;
    try {
      digest = MessageDigest.getInstance("MD5");
      digest.update(s.getBytes(Charset.forName("US-ASCII")), 0, s.length());
      byte[] magnitude = digest.digest();
      BigInteger bi = new BigInteger(1, magnitude);
      String hash = String.format("%0" + (magnitude.length << 1) + "x", bi);
      return hash;
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return "";
  }

  // getter & setter

  public void setContext(Context context) {
    mContext = context;
  }
}
