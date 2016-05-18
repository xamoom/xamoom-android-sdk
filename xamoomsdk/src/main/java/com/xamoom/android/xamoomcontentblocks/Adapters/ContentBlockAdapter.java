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
                             Style style, EnduserApi enduserApi, boolean showSpotMapContentLinks,
                             String youtubeApiKey,
                             ContentBlock3ViewHolder.OnContentBlock3ViewHolderInteractionListener contentBlock3ViewHolderInteractionListener) {
    mOnContentBlock3ViewHolderInteractionListener = contentBlock3ViewHolderInteractionListener;
    mFragment = fragment;
    mContentBlocks = contentBlocks;
    mStyle = style;
    mEnduserApi = enduserApi;
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
    /*
    switch (viewType) {
      case -1:
        View view0 = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.content_header_layout, parent, false);
        return new ContentHeaderViewHolder(view0);
      case 0:
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.content_block_0_layout, parent, false);
        return new ContentBlock0ViewHolder(view);
      case 1:
        View view1 = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.content_block_1_layout, parent, false);
        return new ContentBlock1ViewHolder(view1, mFragment);
      case 2:
        View view2 = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.content_block_2_layout, parent, false);
        return new ContentBlock2ViewHolder(view2, mFragment.getContext(), mYoutubeApiKey, mBitmapCache);
      case 3:
        View view3 = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.content_block_3_layout, parent, false);
        return new ContentBlock3ViewHolder(view3, mFragment.getContext(), mOnContentBlock3ViewHolderInteractionListener);
      case 4:
        View view4 = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.content_block_4_layout, parent, false);
        return new ContentBlock4ViewHolder(view4, mFragment);
      case 5:
        View view5 = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.content_block_5_layout, parent, false);
        return new ContentBlock5ViewHolder(view5, mFragment);
      case 6:
        View view6 = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.content_block_6_layout, parent, false);
        return new ContentBlock6ViewHolder(view6, mFragment.getContext(), mEnduserApi,
            mContentCache, mOnXamoomContentFragmentInteractionListener);
      case 7:
        View view7 = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.content_block_7_layout, parent, false);
        return new ContentBlock7ViewHolder(view7);
      case 8:
        View view8 = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.content_block_8_layout, parent, false);
        return new ContentBlock8ViewHolder(view8, mFragment);
      case 9:
        View view9 = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.content_block_9_layout, parent, false);
        return new ContentBlock9ViewHolder(view9, mFragment, mEnduserApi);
      default:
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_layout, parent, false);
        return new ViewHolder(v);
    }*/
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    mDelegatesManager.onBindViewHolder(mContentBlocks, position, holder, mStyle);
    /*
    ContentBlock cb = mContentBlocks.get(position);

    switch (cb.getBlockType()) {
      case -1:
        ContentHeaderViewHolder newHeaderHolder = (ContentHeaderViewHolder) holder;
        newHeaderHolder.setLinkColor(mLinkColor);
        newHeaderHolder.setupContentBlock(cb);
        break;
      case 0:
        ContentBlock0ViewHolder newHolder = (ContentBlock0ViewHolder) holder;
        newHolder.setLinkColor(mLinkColor);
        newHolder.setupContentBlock(cb);
        break;
      case 1:
        ContentBlock1ViewHolder newHolder1 = (ContentBlock1ViewHolder) holder;
        newHolder1.setupContentBlock(cb);
        break;

      case 2:
        ContentBlock2ViewHolder newHolder2 = (ContentBlock2ViewHolder) holder;
        newHolder2.setupContentBlock(cb);
        break;
      case 3:
        ContentBlock3ViewHolder newHolder3 = (ContentBlock3ViewHolder) holder;
        newHolder3.setupContentBlock(cb);
        break;
      case 4:
        ContentBlock4ViewHolder newHolder4 = (ContentBlock4ViewHolder) holder;
        newHolder4.setupContentBlock(cb);
        break;
      case 5:
        ContentBlock5ViewHolder newHolder5 = (ContentBlock5ViewHolder) holder;
        newHolder5.setupContentBlock(cb);
        break;
      case 6:
        ContentBlock6ViewHolder newHolder6 = (ContentBlock6ViewHolder) holder;
        newHolder6.setupContentBlock(cb);
        break;
      case 7:
        ContentBlock7ViewHolder newHolder7 = (ContentBlock7ViewHolder) holder;
        newHolder7.setupContentBlock(cb);
        break;
      case 8:
        ContentBlock8ViewHolder newHolder8 = (ContentBlock8ViewHolder) holder;
        newHolder8.setupContentBlock(cb);
        break;
      case 9:
        ContentBlock9ViewHolder newHolder9 = (ContentBlock9ViewHolder) holder;
        newHolder9.showContentLinks = showContentLinks;
        newHolder9.setupContentBlock(cb);
        break;
    }
    */
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
}

/**
 * Empty ViewHolder.
 */
class ViewHolder extends RecyclerView.ViewHolder {
  public ViewHolder(View v) {
    super(v);
  }
}
