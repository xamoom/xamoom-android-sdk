package com.xamoom.android.xamoomsdk.xamoomcontentblocks.Viewholders;

import android.support.v4.util.LruCache;
import android.view.View;
import android.widget.LinearLayout;

import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock5ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock6ViewHolder;
import com.xamoom.android.xamoomcontentblocks.XamoomContentFragment;
import com.xamoom.android.xamoomsdk.APICallback;
import com.xamoom.android.xamoomsdk.APIListCallback;
import com.xamoom.android.xamoomsdk.BuildConfig;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.Helper.ContentFragmentActivity;
import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Style;
import com.xamoom.android.xamoomsdk.Storage.FileManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;

import at.rags.morpheus.Error;
import okhttp3.Response;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "AndroidManifest.xml")
public class ContentBlock6ViewHolderTest {
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
    View itemView = View.inflate(mActivity, R.layout.content_block_6_layout, null);
    ContentBlock6ViewHolder viewHolder = new ContentBlock6ViewHolder(itemView,
        mXamoomContentFragment.getContext(), null, null, null);

    assertNotNull(viewHolder);
  }

  @Test
  public void testSetupContentBlockOffline() throws IOException {
    LruCache<String, Content> mockContentCache = (LruCache<String, Content>) Mockito.mock(LruCache.class);
    Mockito.stub(mockContentCache.get(anyString())).toReturn(null);
    EnduserApi mockedApi = Mockito.mock(EnduserApi.class);
    Mockito.doAnswer(
        new Answer<Object>() {
          public Object answer(InvocationOnMock invocation) {
            APICallback<Content, Error> callback =
                (APICallback<Content, Error>) invocation.getArguments()[2];
            Content content = new Content();
            content.setPublicImageUrl("www.xamoom.com/video.mp4");
            callback.finished(content);
            return null;
          }
        }).when(mockedApi).getContent(anyString(), any(EnumSet.class),
        (APICallback<Content, List<Error>>) any());

    View itemView = View.inflate(mActivity, R.layout.content_block_6_layout, null);
    ContentBlock6ViewHolder viewHolder = new ContentBlock6ViewHolder(itemView,
        mXamoomContentFragment.getContext(), mockedApi, mockContentCache, null);
    FileManager mockedFileManager = Mockito.mock(FileManager.class);
    viewHolder.setFileManager(mockedFileManager);

    Mockito.stub(mockedFileManager.getFilePath(anyString())).toReturn("test");

    ContentBlock contentBlock = new ContentBlock();
    contentBlock.setFileId("www.xamoom.com/video.mp4");
    contentBlock.setId("1");

    viewHolder.setupContentBlock(contentBlock, true);

    Mockito.verify(mockedFileManager).getFilePath("www.xamoom.com/video.mp4");
  }
}
