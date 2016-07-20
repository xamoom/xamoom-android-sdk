package com.xamoom.android.xamoomsdk.xamoomcontentblocks.Viewholders;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.util.LruCache;
import android.view.View;

import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock2ViewHolder;
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
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "AndroidManifest.xml")
public class ContentBlock2ViewHolderTest {

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
    View itemView = View.inflate(mActivity, R.layout.content_block_2_layout, null);
    ContentBlock2ViewHolder viewHolder = new ContentBlock2ViewHolder(itemView,
        mXamoomContentFragment, "youtube_key", mMockBitmapCache);

    assertNotNull(viewHolder);
  }
}
