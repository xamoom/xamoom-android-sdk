/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomsdk.xamoomcontentblocks.Viewholders;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v4.util.LruCache;
import android.view.View;

import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock1ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock2ViewHolder;
import com.xamoom.android.xamoomcontentblocks.XamoomContentFragment;
import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.Helper.ContentFragmentActivity;
import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Style;
import com.xamoom.android.xamoomsdk.Storage.FileManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Matchers.anyString;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "AndroidManifest.xml")
public class ContentBlock1ViewHolderTest {

  private XamoomContentFragment mXamoomContentFragment;
  private ContentFragmentActivity mActivity;
  private MediaPlayer mMockedMediaPlayer;

  @Before
  public void setup() {
    mActivity = Robolectric.buildActivity( ContentFragmentActivity.class )
        .create()
        .start()
        .resume()
        .get();

    mXamoomContentFragment = XamoomContentFragment.newInstance("");
    mActivity.getSupportFragmentManager()
        .beginTransaction()
        .add(mXamoomContentFragment, null)
        .commit();

    mMockedMediaPlayer = Mockito.mock(MediaPlayer.class);
  }

  @Test
  public void testSetupMusicPlayerOffline() {
    String fileId = "www.xamoom.com/file.mp4";
    View itemView = View.inflate(mActivity, R.layout.content_block_1_layout, null);
    ContentBlock1ViewHolder viewHolder = new ContentBlock1ViewHolder(itemView,
        mXamoomContentFragment);
    viewHolder.setMediaPlayer(mMockedMediaPlayer);
    FileManager mockedFileManager = Mockito.mock(FileManager.class);
    viewHolder.setFileManager(mockedFileManager);
    Mockito.stub(mockedFileManager.getFilePath(anyString())).toReturn(fileId);

    ContentBlock contentBlock = new ContentBlock();
    contentBlock.setFileId(fileId);

    viewHolder.setupContentBlock(contentBlock, true);

    Mockito.verify(mockedFileManager).getFilePath(fileId);
  }
}
