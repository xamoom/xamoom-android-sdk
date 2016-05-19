package com.xamoom.android.xamoomcontentblocks.Adapters;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock3ViewHolder;
import com.xamoom.android.xamoomcontentblocks.XamoomContentFragment;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Style;

import java.util.List;

/**
 * ContentBlockAdapter will display all the contentBlocks you get from the xamoom cloud.
 *
 * @author Raphael Seher
 */
public class ContentBlockAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private XamoomContentFragment.OnXamoomContentFragmentInteractionListener mOnXamoomContentFragmentInteractionListener;
  private ContentBlock3ViewHolder.OnContentBlock3ViewHolderInteractionListener mOnContentBlock3ViewHolderInteractionListener;
  private Fragment mFragment;
  private AdapterDelegatesManager mDelegatesManager = new AdapterDelegatesManager();
  private List<ContentBlock> mContentBlocks;
  private Style mStyle;
  private String mYoutubeApiKey;
  private EnduserApi mEnduserApi;
  private boolean showContentLinks;

  private String mLinkColor = "00F";
  private String mBackgroundColor = "000";
  private String mFontColor = "FFF";
  private LruCache<String, Bitmap> mBitmapCache = new LruCache<>(8*1024*1024);
  private LruCache<String, Content> mContentCache = new LruCache<>(2*1024*1024);

  /**
   * Constructor for the Adapter.
   * @param fragment Fragment with the recyclerView in it.
   * @param contentBlocks ContentBlocks to display.
   * @param style The style from your xamoom system.
   * @param youtubeApiKey Youtube api key from Google Developer Console.
   */
  public ContentBlockAdapter(Fragment fragment, List<ContentBlock> contentBlocks,
                             Style style, boolean showSpotMapContentLinks,
                             String youtubeApiKey,
                             ContentBlock3ViewHolder.OnContentBlock3ViewHolderInteractionListener contentBlock3ViewHolderInteractionListener) {
    mOnContentBlock3ViewHolderInteractionListener = contentBlock3ViewHolderInteractionListener;
    mFragment = fragment;
    mContentBlocks = contentBlocks;
    mStyle = style;
    showContentLinks = showSpotMapContentLinks;
    mYoutubeApiKey = youtubeApiKey;

    setupAdapters();
  }

  private void setupAdapters() {
    mDelegatesManager.addDelegate(0, new ContentBlock0Adapter());
    mDelegatesManager.addDelegate(1, new ContentBlock1Adapter());
    mDelegatesManager.addDelegate(2, new ContentBlock2Adapter());
    mDelegatesManager.addDelegate(3, new ContentBlock3Adapter());
    mDelegatesManager.addDelegate(4, new ContentBlock4Adapter());
    mDelegatesManager.addDelegate(5, new ContentBlock5Adapter());
    mDelegatesManager.addDelegate(6, new ContentBlock6Adapter());
    mDelegatesManager.addDelegate(7, new ContentBlock7Adapter());
    mDelegatesManager.addDelegate(8, new ContentBlock8Adapter());
    mDelegatesManager.addDelegate(9, new ContentBlock9Adapter());
  }

  @Override
  public int getItemViewType(int position) {
    return mContentBlocks.get(position).getBlockType();
  }

  @Override
  public int getItemCount() {
    return mContentBlocks.size();
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return mDelegatesManager.onCreateViewHolder(parent, viewType, mFragment, mEnduserApi, mYoutubeApiKey,
        mBitmapCache, mContentCache, showContentLinks, mOnContentBlock3ViewHolderInteractionListener,
        mOnXamoomContentFragmentInteractionListener);
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    mDelegatesManager.onBindViewHolder(mContentBlocks, position, holder, mStyle);
  }

  @Override
  public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
    mFragment = null;
    mContentBlocks = null;
    mDelegatesManager = null;
    mEnduserApi = null;
    mBitmapCache = null;
    mOnContentBlock3ViewHolderInteractionListener = null;
    mOnXamoomContentFragmentInteractionListener = null;
    super.onDetachedFromRecyclerView(recyclerView);
  }

  public AdapterDelegatesManager getDelegatesManager() {
    return mDelegatesManager;
  }

  public Fragment getFragment() {
    return mFragment;
  }

  public EnduserApi getEnduserApi() {
    return mEnduserApi;
  }

  public List<ContentBlock> getContentBlocks() {
    return mContentBlocks;
  }

  public void setContentBlocks(List<ContentBlock> contentBlocks) {
    mContentBlocks = contentBlocks;
  }

  public void setYoutubeApiKey(String youtubeApiKey) {
    mYoutubeApiKey = youtubeApiKey;
  }

  public void setOnXamoomContentFragmentInteractionListener(XamoomContentFragment.OnXamoomContentFragmentInteractionListener onXamoomContentFragmentInteractionListener) {
    mOnXamoomContentFragmentInteractionListener = onXamoomContentFragmentInteractionListener;
  }

  public void setEnduserApi(EnduserApi enduserApi) {
    mEnduserApi = enduserApi;
  }
}
