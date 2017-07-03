/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomsdk.xamoomcontentblocks.Viewholders;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.util.LruCache;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.VideoView;

import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock2ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock5ViewHolder;
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

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "AndroidManifest.xml")
public class ContentBlock5ViewHolderTest {
  private XamoomContentFragment mXamoomContentFragment;
  private ContentFragmentActivity mActivity;
  private Style mStyle;

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

    mStyle = new Style();
    mStyle.setForegroundFontColor("#ff0000");
  }

  @Test
  public void testConstructor() {
    View itemView = View.inflate(mActivity, R.layout.content_block_5_layout, null);
    ContentBlock5ViewHolder viewHolder = new ContentBlock5ViewHolder(itemView,
        mXamoomContentFragment);

    assertNotNull(viewHolder);
  }

  @Test
  public void testSetupContentBlockOffline() throws IOException {
    View itemView = View.inflate(mActivity, R.layout.content_block_5_layout, null);
    ContentBlock5ViewHolder viewHolder = new ContentBlock5ViewHolder(itemView, mXamoomContentFragment);
    FileManager mockedFileManager = Mockito.mock(FileManager.class);
    viewHolder.setFileManager(mockedFileManager);

    Mockito.stub(mockedFileManager.getFilePath(anyString())).toReturn("test");

    ContentBlock contentBlock = new ContentBlock();
    contentBlock.setFileId("www.xamoom.com/video.mp4");

    viewHolder.setupContentBlock(contentBlock, true);

    LinearLayout mRootLayout = (LinearLayout) itemView.findViewById(R.id.linkBlockLinearLayout);
    mRootLayout.callOnClick();

    Mockito.verify(mockedFileManager).getFile("www.xamoom.com/video.mp4");
  }
}
