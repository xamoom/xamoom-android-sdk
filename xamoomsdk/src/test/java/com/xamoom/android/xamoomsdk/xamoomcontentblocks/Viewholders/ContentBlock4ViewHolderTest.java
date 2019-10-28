/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomsdk.xamoomcontentblocks.Viewholders;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import androidx.collection.LruCache;
import android.view.View;

import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock4ViewHolder;
import com.xamoom.android.xamoomcontentblocks.XamoomContentFragment;
import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.Helper.ContentFragmentActivity;
import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Style;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "AndroidManifest.xml")
public class ContentBlock4ViewHolderTest {

  private XamoomContentFragment mXamoomContentFragment;
  private LruCache<String, Bitmap> mMockBitmapCache;
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

    mMockBitmapCache = new LruCache<>(1024*10);
  }

  @Test
  public void testConstructor() {
    View itemView = View.inflate(mActivity, R.layout.content_block_4_layout, null);
    ContentBlock4ViewHolder viewHolder =
        new ContentBlock4ViewHolder(itemView, mXamoomContentFragment);
    assertNotNull(viewHolder);
  }

  @Test
  public void testSetupForLinkBlockType18() {
    View itemView = View.inflate(mActivity, R.layout.content_block_4_layout, null);
    ContentBlock4ViewHolder viewHolder =
        new ContentBlock4ViewHolder(itemView, mXamoomContentFragment);

    ContentBlock contentBlock = new ContentBlock();
    contentBlock.setPublicStatus(true);
    contentBlock.setTitle("instagram");
    contentBlock.setText("instagram link");
    contentBlock.setLinkUrl("www.instagram.com");
    contentBlock.setLinkType(18);

    viewHolder.setupContentBlock(contentBlock, false);

    assertEquals(contentBlock.getTitle(), viewHolder.getTitleTextView().getText());
    assertEquals(contentBlock.getText(), viewHolder.getContentTextView().getText());
    assertEquals(RuntimeEnvironment.application.getResources().getDrawable(R.drawable.ic_instagram),
        viewHolder.getIcon().getDrawable());
    assertEquals(RuntimeEnvironment.application.getResources().getColor(R.color.linkblock_instagram_background_color),
        getColor(viewHolder.getRootLayout().getBackground()));
    assertEquals(Color.WHITE,
        viewHolder.getTitleTextView().getCurrentTextColor());
    assertEquals(Color.WHITE,
        viewHolder.getContentTextView().getCurrentTextColor());
  }


  private int getColor(Drawable drawable) {
    int color = 0;
    if (drawable instanceof ColorDrawable) {
      color = ((ColorDrawable) drawable).getColor();
    }
    return color;
  }
}
