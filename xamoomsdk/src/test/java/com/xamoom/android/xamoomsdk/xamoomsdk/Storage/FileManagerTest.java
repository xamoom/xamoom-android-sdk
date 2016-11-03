package com.xamoom.android.xamoomsdk.xamoomsdk.Storage;


import android.content.Context;

import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.Storage.FileManager;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static junit.framework.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class FileManagerTest {

  private FileManager mFileManager;

  @Before
  public void setup() {
    mFileManager = new FileManager(RuntimeEnvironment.application);
  }

  @Test
  public void testGetInstance() {
    FileManager test = FileManager.getInstance(RuntimeEnvironment.application);
    Assert.assertNotNull(test);
  }

  @Test
  public void testGetFileName() {
    String urlString = "https://storage.googleapis.com/xamoom-files-dev/mobile/d2fee0d551d9432eaed4596f1300af5d.jpg";

    String fileName = mFileManager.getFileName(urlString);

    Assert.assertEquals(fileName, "e95dea971ec7ec41d4c1be562105021a.jpg");
  }

  @Test
  public void testGetFileNameWithCachingQuery() {
    String urlString = "https://storage.googleapis.com/xamoom-files-dev/mobile/d2fee0d551d9432eaed4596f1300af5d.jpg?v=6cb4b222ae80fd7714949786da84e3cc849ea3bcac2c6db5fe3cbe294ca9161babbcce9806f627dd9ed558a5a7b60c1820a9084c128d0f69019ce6afb314482d";

    String fileName = mFileManager.getFileName(urlString);

    Assert.assertEquals(fileName, "e95dea971ec7ec41d4c1be562105021a.jpg");
  }

  @Test
  public void testSaveFile() {
    Context mockedContext = Mockito.mock(Context.class);
    FileManager fileManager = new FileManager(mockedContext);
    FileOutputStream mockedOutputStream = Mockito.mock(FileOutputStream.class);

    try {
      Mockito.stub(mockedContext.openFileOutput(anyString(), anyInt())).toReturn(mockedOutputStream);
    } catch (FileNotFoundException e) {
      fail();
    }

    String smt = "asfijalksfjlaksfjkla";

    try {
      fileManager.saveFile("test", smt.getBytes());
    } catch (IOException e) {
      fail();
    }

    try {
      Mockito.verify(mockedOutputStream).write(any(byte[].class));
      Mockito.verify(mockedOutputStream).close();
    } catch (IOException e) {
      fail();
    }
  }

  @Test
  public void testGetFile() throws IOException {
    try {
      mFileManager.saveFile("test", "test".getBytes());
    } catch (IOException e) {
      fail();
    }

    File file = mFileManager.getFile("test");

    Assert.assertNotNull(file);
  }

  @Test
  public void testGetFilePath() {
    String urlString = "https://storage.googleapis.com/xamoom-files-dev/mobile/d2fee0d551d9432eaed4596f1300af5d.jpg";
    String path = mFileManager.getFilePath(urlString);

    Assert.assertEquals(path,
        RuntimeEnvironment.application.getFilesDir().getAbsolutePath()+"/e95dea971ec7ec41d4c1be562105021a.jpg");
  }

  @Test
  public void testDeleteFile() throws IOException {
    String urlString = "https://storage.googleapis.com/xamoom-files-dev/mobile/d2fee0d551d9432eaed4596f1300af5d.jpg";
    String path = mFileManager.getFilePath(urlString);

    try {
      mFileManager.saveFile(urlString, "test".getBytes());
    } catch (IOException e) {
      fail();
    }

    boolean deleted = mFileManager.deleteFile(urlString);

    Assert.assertTrue(deleted);
  }

}
