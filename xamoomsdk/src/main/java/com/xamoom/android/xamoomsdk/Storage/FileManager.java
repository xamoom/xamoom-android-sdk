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

  public void saveFile(String urlString, byte[] bytes) throws IOException {
    FileOutputStream outputStream;
    String fileName = getFileName(urlString);

    outputStream = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
    outputStream.write(bytes);
    outputStream.close();
  }

  public boolean deleteFile(String urlString) throws IOException {
    File file = getFile(urlString);

    boolean deleted = file.delete();
    return deleted;
  }

  public File getFile(String urlString) throws IOException {
    File file = new File(mContext.getFilesDir(), getFileName(urlString));
    return file;
  }

  public String getFileName(String urlString) {
    Uri url = Uri.parse(urlString);
    String urlStringWithoutCaching = urlString.replace("?" + url.getQuery(), "");
    String fileName = url.getLastPathSegment();
    String[] fileNameSegments = fileName.split("\\.");
    String extension = "";
    if (fileNameSegments != null && fileNameSegments.length > 1) {
      extension = fileNameSegments[1];
    }

    String newFileName = String.format("%s.%s", md5(urlStringWithoutCaching), extension);
    return newFileName;
  }

  public String getFilePath(String urlString) {
    return String.format("%s/%s", mContext.getFilesDir().getAbsolutePath(), getFileName(urlString));
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
