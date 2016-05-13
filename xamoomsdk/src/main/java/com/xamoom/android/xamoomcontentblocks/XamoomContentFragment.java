package com.xamoom.android.xamoomcontentblocks;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.xamoom.android.xamoomcontentblocks.Adapters.ContentBlockAdapter;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock3ViewHolder;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Style;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * XamoomContentBlock is a helper for everyone to display the different contentBlocks delivered
 * from the xamoom cloud.
 *
 * ATM there are nine different contentBlocks displaying different contents from the customer.
 * In your app you can display the contentBlocks like you want, we decided to display them
 * like on our website (https://www.xm.gl).
 *
 * To get started with the XamoomContentFragment create a new Instance via {@link #newInstance(String)}.
 * You also have to adapt {@link com.xamoom.android.xamoomcontentblocks.XamoomContentFragment.OnXamoomContentFragmentInteractionListener}.
 *
 * There are two ways to use this class.
 * You can set an identifier (contentId or locationIdentifier), this fragment will download it and display it, when added to an activity.
 * Or you can set the content and the fragment will display that.
 *
 * @author Raphael Seher
 *
 * @version 0.1
 *
 */
public class XamoomContentFragment extends Fragment implements ContentBlock3ViewHolder.OnContentBlock3ViewHolderInteractionListener {
  private static final String YOUTUBE_API_KEY = "0000";
  private static final int WRITE_STORAGE_PERMISSION = 0;

  private RecyclerView mRecyclerView;
  private ContentBlockAdapter mContentBlockAdapter;
  private Content mContent;
  private List<ContentBlock> mContentBlocks = new LinkedList<>();
  private Style mStyle;
  private String mYoutubeApiKey;
  private EnduserApi mEnduserApi;

  private boolean displayAllStoreLinks = false;
  private boolean showSpotMapContentLinks = false;
  private boolean isAnimated = false;

  private OnXamoomContentFragmentInteractionListener mListener;

  /**
   * Use this factory method to create a new instance.
   * You can set a special linkcolor as hex. (e.g. "00F")
   *
   * @return XamoomContentFragment Returns an Instance of XamoomContentFragment
   */
  public static XamoomContentFragment newInstance(@NonNull String youtubeApiKey) {
    XamoomContentFragment fragment = new XamoomContentFragment();
    Bundle args = new Bundle();

    args.putString(YOUTUBE_API_KEY, youtubeApiKey);
    fragment.setArguments(args);
    return fragment;
  }

  public XamoomContentFragment() {
    mContentBlockAdapter = new ContentBlockAdapter(this, mContentBlocks, mStyle, mEnduserApi,
        showSpotMapContentLinks, mYoutubeApiKey, mListener, this);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mYoutubeApiKey = getArguments().getString(YOUTUBE_API_KEY);
      mContentBlockAdapter.setYoutubeApiKey(mYoutubeApiKey);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_xamoom_content, container, false);
    mRecyclerView = (RecyclerView) view.findViewById(R.id.contentblock_recycler_view);

    setupRecyclerView();

    return view;
  }

  @Override
  public void onStart() {
    super.onStart();

    if(!isAnimated) {
      if (mContentBlockAdapter != null) {
        mContentBlockAdapter.notifyDataSetChanged();
      }
    }
  }

  @Override
  public void onDestroy() {
    mRecyclerView.setAdapter(null);
    mRecyclerView.setLayoutManager(null);
    mContentBlockAdapter = null;
    mRecyclerView = null;
    mContent = null;
    mContentBlocks = null;
    mEnduserApi = null;

    super.onDestroy();
  }

  /**
   * Setup the recyclerview.
   */
  private void setupRecyclerView() {
    if(mContent == null) {
      return;
    }

    mRecyclerView.setLayoutManager(
        new LinearLayoutManager(this.getActivity().getApplicationContext()));
    mRecyclerView.setAdapter(mContentBlockAdapter);
  }

  /**
   * If you animate this fragment, it will show the data when the animation is finished.
   */
  @Override
  public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
    if (nextAnim != 0) {
      isAnimated = true;
      Animation anim = AnimationUtils.loadAnimation(getActivity(), nextAnim);
      anim.setAnimationListener(animationListener);
    }

    return super.onCreateAnimation(transit, enter, nextAnim);
  }

  public Animation.AnimationListener animationListener = new Animation.AnimationListener() {
    @Override
    public void onAnimationStart(Animation animation) {}

    @Override
    public void onAnimationEnd(Animation animation) {
      if (mContentBlockAdapter != null) {
        mContentBlockAdapter.notifyDataSetChanged();
      }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {}
  };

  /**
   * There should always be the contents text, description and image
   * on top of the other contentBlocks.
   *
   * Here it creates a text and an image contentBlock and adds them
   * to achieve this.
   *
   * When you want to have all link ContentBlocks displayed for all
   * stores (to promote your app), you have to set {@link #displayAllStoreLinks} to true.
   */
  private void addContentTitleAndImage() {
    ContentBlock contentBlock0 = new ContentBlock();
    contentBlock0.setTitle(mContent.getTitle());
    contentBlock0.setBlockType(-1);
    contentBlock0.setPublicStatus(true);
    contentBlock0.setText(mContent.getDescription());
    mContentBlocks.add(0, contentBlock0);

    if(mContent.getPublicImageUrl() != null) {
      ContentBlock contentBlock3 = new ContentBlock();
      contentBlock3.setTitle(null);
      contentBlock3.setBlockType(3);
      contentBlock3.setPublicStatus(true);
      contentBlock3.setFileId(mContent.getPublicImageUrl());
      contentBlock3.setScaleX(0);
      mContentBlocks.add(1, contentBlock3);
    }
  }

  /**
   * The default behaviour is that there are only Google Play Store link ContentBlocks
   * are shown.
   *
   * All others will get removed here. Except you set {@link #displayAllStoreLinks} to true.
   *
   * @param contentBlocks ContentBlocks List to manipulate
   * @return Manipulated contetnBlocks List
   */
  private List<ContentBlock> removeStoreLinks(List<ContentBlock> contentBlocks) {
    ArrayList<ContentBlock> cbToRemove = new ArrayList<>();

    for (ContentBlock contentBlock : contentBlocks) {
      if (contentBlock.getBlockType() == 4) {
        if(contentBlock.getLinkType() == 15 || contentBlock.getLinkType() == 17) {
          cbToRemove.add(contentBlock);
        }
      }
    }

    contentBlocks.removeAll(cbToRemove);
    return contentBlocks;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);

    Activity activity;
    if (context instanceof Activity){
      activity= (Activity)context;

      try {
        mListener = (OnXamoomContentFragmentInteractionListener) activity;
      } catch (ClassCastException e) {
        throw new ClassCastException(activity.toString()
            + " must implement OnXamoomContentFragmentInteractionListener");
      }
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  @Override
  public void needExternalStoragePermission() {
   requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_STORAGE_PERMISSION);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }

  /**
   * Implement OnXamoomContentFragmentInteractionListener and override
   * <code>clickedContentBlock(String)</code>.
   *
   * <code>clickedContentBlock(String)</code> gets called, when somebody clicks
   * a {@link com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock6ViewHolder} and you have to handle it. Normally you would open
   * a new activity or update the XamoomContentFragment with the passed contentId.
   */
  public interface OnXamoomContentFragmentInteractionListener {
    void clickedContentBlock(Content content);
    void clickedSpotMapContentLink(String contentId);
  }

  public void contentBlockClick(Content content) {
    mListener.clickedContentBlock(content);
  }

  // getters


  public ContentBlockAdapter getContentBlockAdapter() {
    return mContentBlockAdapter;
  }

  public boolean isShowSpotMapContentLinks() {
    return showSpotMapContentLinks;
  }

  public boolean isDisplayAllStoreLinks() {
    return displayAllStoreLinks;
  }

  public RecyclerView getRecyclerView() {
    return mRecyclerView;
  }

  public List<ContentBlock> getContentBlocks() {
    return mContentBlocks;
  }

  public Style getStyle() {
    return mStyle;
  }

  // setters
  public void setContent(Content content) {
    setContent(content, true);
  }

  public void setContent(Content content, boolean addHeader) {
    this.mContent = content;
    if (mContent.getContentBlocks() != null) {
      mContentBlocks.addAll(mContent.getContentBlocks());
    }

    if (addHeader) {
      addContentTitleAndImage();
    }

    if(!displayAllStoreLinks) {
      mContentBlocks = removeStoreLinks(mContentBlocks);
    }
  }

  public void setStyle(Style mStyle) {
    this.mStyle = mStyle;
  }

  public void setDisplayAllStoreLinks(boolean displayAllStoreLinks) {
    this.displayAllStoreLinks = displayAllStoreLinks;
  }

  public void setShowSpotMapContentLinks(boolean showSpotMapContentLinks) {
    this.showSpotMapContentLinks = showSpotMapContentLinks;
  }

  public void setEnduserApi(EnduserApi enduserApi) {
    mEnduserApi = enduserApi;
  }

}
