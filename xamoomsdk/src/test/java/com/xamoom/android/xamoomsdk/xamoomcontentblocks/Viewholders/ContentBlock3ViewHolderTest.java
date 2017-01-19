package com.xamoom.android.xamoomsdk.xamoomcontentblocks.Viewholders;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.util.LruCache;
import android.view.View;
import android.widget.VideoView;

import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock2ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock3ViewHolder;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "AndroidManifest.xml")
public class ContentBlock3ViewHolderTest {

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
    View itemView = View.inflate(mActivity, R.layout.content_block_3_layout, null);
    ContentBlock3ViewHolder viewHolder = new ContentBlock3ViewHolder(itemView,
        mActivity, null);

    assertNotNull(viewHolder);
  }

  @Test
  public void testSetupContentBlock() {
    View itemView = View.inflate(mActivity, R.layout.content_block_3_layout, null);
    ContentBlock3ViewHolder viewHolder = new ContentBlock3ViewHolder(itemView,
        mXamoomContentFragment.getContext(), null);

    ContentBlock block = new ContentBlock();
    block.setTitle("title");
    block.setCopyright("copyright");

    viewHolder.setupContentBlock(block, false);

    assertEquals("title", viewHolder.getTitleTextView().getText().toString());
    assertEquals("copyright", viewHolder.getCopyrightTextView().getText().toString());
  }

  @Test
  public void testSetupContentBlockWhenOffline() {
    View itemView = View.inflate(mActivity, R.layout.content_block_3_layout, null);
    ContentBlock3ViewHolder viewHolder = new ContentBlock3ViewHolder(itemView,
        mXamoomContentFragment.getContext(), null);

    FileManager mockedFileManager = Mockito.mock(FileManager.class);
    viewHolder.setFileManager(mockedFileManager);

    Mockito.stub(mockedFileManager.getFilePath(anyString())).toReturn("test");

    ContentBlock contentBlock = new ContentBlock();
    contentBlock.setFileId("www.xamoom.com/image.jpg");

    viewHolder.setupContentBlock(contentBlock, true);

    Mockito.verify(mockedFileManager).getFilePath("www.xamoom.com/image.jpg");
  }
}
