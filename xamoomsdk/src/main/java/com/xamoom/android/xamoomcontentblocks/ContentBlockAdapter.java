package com.xamoom.android.xamoomcontentblocks;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xamoom.android.xamoomcontentblocks.Helper.BestLocationProvider;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock0ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock1ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock2ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock3ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock4ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock5ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock6ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock7ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock8ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock9ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentHeaderViewHolder;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.R;
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
  private XamoomContentFragment.OnXamoomContentFragmentInteractionListener mListener;
  private Fragment mFragment;
  private List<ContentBlock> mContentBlocks;
  private Style mStyle;
  private String mYoutubeApiKey;
  private EnduserApi mEnduserApi;
  private BestLocationProvider mBestLocationProvider;
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
   * @param listener FragmentListener for click events.
   */
  public ContentBlockAdapter(Fragment fragment, List<ContentBlock> contentBlocks,
                             Style style, EnduserApi enduserApi, boolean showSpotMapContentLinks,
                             String youtubeApiKey, XamoomContentFragment.OnXamoomContentFragmentInteractionListener listener) {
    mListener = listener;
    mFragment = fragment;
    mContentBlocks = contentBlocks;
    mStyle = style;
    mEnduserApi = enduserApi;
    showContentLinks = showSpotMapContentLinks;
    mYoutubeApiKey = youtubeApiKey;

    if (fragment != null && fragment.getContext() != null) {
      mBestLocationProvider = new BestLocationProvider(fragment.getContext(), false, true,
          1000, 1000, 5, 10);
    }

    if (mStyle != null) {
      configureColors();
    }
  }

  /**
   * Will set mLinkColor, mBackgroundColor and mFontColor with
   * the values from mStyle.
   */
  private void configureColors() {
    if (mStyle.getHighlightFontColor() != null) {
      mLinkColor = mStyle.getHighlightFontColor().substring(1);
    }

    if (mStyle.getBackgroundColor() != null) {
      mBackgroundColor = mStyle.getBackgroundColor().substring(1);
    }

    if (mStyle.getForegroundFontColor() != null) {
      mFontColor = mStyle.getForegroundFontColor().substring(1);
    }
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
        return new ContentBlock3ViewHolder(view3, mFragment.getContext());
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
            mContentCache, mListener);
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
        return new ContentBlock9ViewHolder(view9, mFragment, mEnduserApi, mBestLocationProvider);
      default:
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_layout, parent, false);
        return new ViewHolder(v);
    }
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
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
  }

  @Override
  public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
    mFragment = null;
    mContentBlocks = null;
    mEnduserApi = null;
    mBitmapCache = null;
    if (mBestLocationProvider != null) {
      mBestLocationProvider.destroy();
      mBestLocationProvider = null;
    }
    super.onDetachedFromRecyclerView(recyclerView);
  }

  public String getFontColor() {
    return mFontColor;
  }

  public String getBackgroundColor() {
    return mBackgroundColor;
  }

  public String getLinkColor() {
    return mLinkColor;
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
}

/**
 * Empty ViewHolder.
 */
class ViewHolder extends RecyclerView.ViewHolder {
  public ViewHolder(View v) {
    super(v);
  }
}
