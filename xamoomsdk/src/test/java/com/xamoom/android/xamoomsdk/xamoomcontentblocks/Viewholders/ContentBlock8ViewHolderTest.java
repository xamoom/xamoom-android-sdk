package com.xamoom.android.xamoomsdk.xamoomcontentblocks.Viewholders;

import android.view.View;
import android.widget.LinearLayout;

import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock5ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock8ViewHolder;
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

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "AndroidManifest.xml")
public class ContentBlock8ViewHolderTest {
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
    View itemView = View.inflate(mActivity, R.layout.content_block_8_layout, null);
    ContentBlock8ViewHolder viewHolder = new ContentBlock8ViewHolder(itemView,
        mXamoomContentFragment);

    assertNotNull(viewHolder);
  }

  @Test
  public void testSetupContentBlockOffline() throws IOException {
    View itemView = View.inflate(mActivity, R.layout.content_block_8_layout, null);
    ContentBlock8ViewHolder viewHolder = new ContentBlock8ViewHolder(itemView, mXamoomContentFragment);
    FileManager mockedFileManager = Mockito.mock(FileManager.class);
    viewHolder.setFileManager(mockedFileManager);

    Mockito.stub(mockedFileManager.getFilePath(anyString())).toReturn("test");

    ContentBlock contentBlock = new ContentBlock();
    contentBlock.setFileId("www.xamoom.com/video.mp4");

    viewHolder.setupContentBlock(contentBlock, true);

    LinearLayout mRootLayout = (LinearLayout) itemView.findViewById(R.id.downloadBlockLinearLayout);
    mRootLayout.callOnClick();

    Mockito.verify(mockedFileManager).getFile("www.xamoom.com/video.mp4");
  }
}
