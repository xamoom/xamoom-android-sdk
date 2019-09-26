/*
 * Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the root of this project.
 */

package com.xamoom.android.xamoomcontentblocks;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.xamoom.android.xamoomcontentblocks.Adapters.ContentBlockAdapter;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock12ViewHolderInterface;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock1ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock2ViewHolder;
import com.xamoom.android.xamoomcontentblocks.ViewHolders.ContentBlock3ViewHolder;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Style;

import java.util.ArrayList;
import java.util.Arrays;
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
 * Display data by setting the Content. Content must be completely downloaded to appear completely.
 *
 * Set the EnduserApi for ContentBlocks, that need to download additional data from the xamoom cms.
 *
 * @author Raphael Seher
 *
 */
public class XamoomContentFragment extends Fragment implements ContentBlock3ViewHolder.OnContentBlock3ViewHolderInteractionListener, ContentBlock12ViewHolderInterface {
  private static final String YOUTUBE_API_KEY = "YoutubeAPIKey";
  private static final String LIST_STATE = "LayoutManagerState";
  private static final String CONTENT_ID = "ContentID";
  private static final String ENDUSER_API_KEY = "EnduserApiKey";
  private static final String OFFLINE = "Offline";
  private static final String SHOW_SPOT_MAP_CONTENT_LINKS = "ContentLinksSpotMaps";
  private static final String STYLE = "Style";
  private static final String BACKGROUND_COLOR = "BackgroundColor";
  private static final String URL_SCHEME = "UrlScheme";
  private static final String BEACON_MAJOR = "beaconMajor";
  private static final String MAPBOX_STYLE = "mapbox_style";
  private static final String NAVIGATION_TINT = "navigation_tint";
  private static final String CONTENT_BUTTON_TEXT = "content_text_color";
  private static final String NAVIGATION_MODE = "navigation_mode";

  private static final int WRITE_STORAGE_PERMISSION = 0;

  private View mRootView;
  private EnduserApi mEnduserApi;
  private RecyclerView mRecyclerView;
  private ContentBlockAdapter mContentBlockAdapter;
  private Content mContent;
  private String mContentID;
  private ArrayList<ContentBlock> mContentBlocks = new ArrayList<>();
  private String mYoutubeApiKey;
  private Style style;
  private int mBackgroundColor = Color.WHITE;
  private LinearLayoutManager.SavedState mListState;

  private boolean offline = false;
  private boolean showAllBlocksWhenOffline = false;
  private boolean displayAllStoreLinks = false;
  private boolean showSpotMapContentLinks = false;
  private boolean isAnimated = false;
  private ArrayList<String> contentBlockUrlScheme;
  private String majorId;
  private String mapboxStyleString;
  private String navigationButtonTintColorString;
  private String contentButtonTextColorString;
  private String navigationMode;

  private Integer[] validBlockTypes = {-2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 11, 12};

  public static XamoomContentFragment newInstance(@NonNull String youtubeApiKey) {
    return newInstance(youtubeApiKey, null, null, null, null, null, null);
  }

  public static XamoomContentFragment newInstance(@NonNull String youtubeApiKey, @Nullable ArrayList<String> url) {
    return newInstance(youtubeApiKey, url, null, null, null, null, null);
  }

  public static XamoomContentFragment newInstance(@NonNull String youtubeApiKey, @Nullable ArrayList<String> url, @Nullable String beaconMajor) {
    return newInstance(youtubeApiKey, url, beaconMajor, null, null, null, null);
  }

  public static XamoomContentFragment newInstance(@NonNull String youtubeApiKey, @Nullable String beaconMajorId, @Nullable String mapboxStyleString) {
    return newInstance(youtubeApiKey, null, beaconMajorId, mapboxStyleString, null, null, null);
  }

  /**
   * Use this factory method to create a new instance.
   * You can set a special linkcolor as hex. (e.g. "00F")
   *
   * @return XamoomContentFragment Returns an Instance of XamoomContentFragment
   */
  public static XamoomContentFragment newInstance(@NonNull String youtubeApiKey, ArrayList<String> url, @Nullable String beaconMajorId, @Nullable String mapboxStyleString,
                                                  @Nullable String navigationButtonTintColorString, @Nullable String contentButtonTextColorString, @Nullable String navigationMode) {
    XamoomContentFragment fragment = new XamoomContentFragment();
    Bundle args = new Bundle();

    args.putString(YOUTUBE_API_KEY, youtubeApiKey);
    args.putStringArrayList(URL_SCHEME, url);
    args.putString(BEACON_MAJOR, beaconMajorId);
    args.putString(MAPBOX_STYLE, mapboxStyleString);
    args.putString(NAVIGATION_TINT, navigationButtonTintColorString);
    args.putString(CONTENT_BUTTON_TEXT, contentButtonTextColorString);
    args.putString(NAVIGATION_MODE, navigationMode);

    fragment.setArguments(args);

    return fragment;
  }

  public XamoomContentFragment() {
    mContentBlockAdapter = new ContentBlockAdapter(this, mContentBlocks,
            showSpotMapContentLinks, mYoutubeApiKey, this, contentBlockUrlScheme, mapboxStyleString, navigationButtonTintColorString, contentButtonTextColorString, navigationMode, this, mContent);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      contentBlockUrlScheme = getArguments().getStringArrayList(URL_SCHEME);
      mapboxStyleString = getArguments().getString(MAPBOX_STYLE);
      navigationButtonTintColorString = getArguments().getString(NAVIGATION_TINT);
      contentButtonTextColorString = getArguments().getString(CONTENT_BUTTON_TEXT);
      navigationMode = getArguments().getString(NAVIGATION_MODE);
      mContentBlockAdapter.setContentBlockUrlScheme(contentBlockUrlScheme);
      mContentBlockAdapter.setMapboxStyleString(mapboxStyleString);
      mContentBlockAdapter.setShowContentLinks(showSpotMapContentLinks);
      mContentBlockAdapter.setNavigationButtonTintColorString(navigationButtonTintColorString);
      mContentBlockAdapter.setContentButtonTextColorString(contentButtonTextColorString);
      mContentBlockAdapter.setNavigationMode(navigationMode);
      mYoutubeApiKey = getArguments().getString(YOUTUBE_API_KEY);
      mContentBlockAdapter.setYoutubeApiKey(mYoutubeApiKey);
      mContentBlockAdapter.setContentBlocks(mContentBlocks);
      this.majorId = getArguments().getString(BEACON_MAJOR);
    }

    if (savedInstanceState != null) {
      mYoutubeApiKey = savedInstanceState.getString(YOUTUBE_API_KEY);
      showSpotMapContentLinks = savedInstanceState.getBoolean(SHOW_SPOT_MAP_CONTENT_LINKS);
      mEnduserApi = new EnduserApi(savedInstanceState.getString(ENDUSER_API_KEY), getContext(), this.majorId, 5000);
      mContentID = savedInstanceState.getString(CONTENT_ID);
      mListState = savedInstanceState.getParcelable(LIST_STATE);
      offline = savedInstanceState.getBoolean(OFFLINE);
      style = savedInstanceState.getParcelable(STYLE);
      mBackgroundColor = savedInstanceState.getInt(BACKGROUND_COLOR);

      mContentBlocks = (ArrayList<ContentBlock>)
              ContentFragmentCache.getSharedInstance().get(mContentID);

      mContentBlockAdapter.setYoutubeApiKey(mYoutubeApiKey);
      mContentBlockAdapter.setContentBlocks(mContentBlocks);
      mContentBlockAdapter.setEnduserApi(mEnduserApi);
      mContentBlockAdapter.setOffline(offline);
      mContentBlockAdapter.setStyle(style);
      mContentBlockAdapter.setShowContentLinks(showSpotMapContentLinks);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    mRootView = inflater.inflate(R.layout.fragment_xamoom_content, container, false);
    mRootView.setBackgroundColor(mBackgroundColor);

    mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.contentblock_recycler_view);
    DefaultItemAnimator animator = new DefaultItemAnimator();
    animator.setMoveDuration(1050);
    mRecyclerView.setItemAnimator(animator);
    setupRecyclerView();

    return mRootView;
  }

  @Override
  public void onStart() {
    super.onStart();

    if(!isAnimated) {
      if (mContentBlockAdapter != null) {
        Log.v("test", "onStart");
        mContentBlockAdapter.notifyDataSetChanged();
      }
    }
  }

  @Override
  public void onResume() {
    super.onResume();

    mRecyclerView.scrollToPosition(0);
    mContentBlockAdapter.notifyDataSetChanged();
  }

  @Override
  public void onStop() {
    if (mContentBlockAdapter != null) {
      mContentBlockAdapter.onStop();
    }
    super.onStop();

  }

  @Override
  public void onPause() {
    Intent intent = new Intent(ContentBlock2ViewHolder.RESET_YOUTUBE);
    LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
    Intent intent2 = new Intent(ContentBlock1ViewHolder.PAUSE_INTENT_ACTION);
    LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent2);

    if (mContentBlockAdapter != null) {
      mContentBlockAdapter.onPause();
    }

    super.onPause();
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putString(YOUTUBE_API_KEY, mYoutubeApiKey);
    outState.putBoolean(SHOW_SPOT_MAP_CONTENT_LINKS, showSpotMapContentLinks);
    outState.putString(ENDUSER_API_KEY, mEnduserApi.getApiKey());
    outState.putString(CONTENT_ID, mContentID);
    outState.putParcelable(LIST_STATE, mRecyclerView.getLayoutManager().onSaveInstanceState());
    outState.putBoolean(OFFLINE, offline);
    outState.putParcelable(STYLE, style);
    outState.putInt(BACKGROUND_COLOR, mBackgroundColor);

    ContentFragmentCache.getSharedInstance().save(mContentID, mContentBlocks);
    mContentBlockAdapter.onSaveInstanceState(outState);
  }

  @Override
  public void onDestroy() {
    if (mRecyclerView != null) {
      mRecyclerView.setAdapter(null);
      mRecyclerView.setLayoutManager(null);
    }
    mContentBlockAdapter = null;
    mRecyclerView = null;
    mContent = null;
    mContentBlocks = null;

    super.onDestroy();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    if (mContentBlockAdapter != null) {
      mContentBlockAdapter.onDestroy();
    }
  }

  @Override
  public void onLowMemory() {
    super.onLowMemory();
    if (mContentBlockAdapter != null) {
      mContentBlockAdapter.onLowMemory();
    }
  }

  /**
   * Setup the recyclerview.
   */
  private void setupRecyclerView() {
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity().getApplicationContext());
    layoutManager.setAutoMeasureEnabled(true);

    if (mListState != null) {
      layoutManager.onRestoreInstanceState(mListState);
    }

    mRecyclerView.setLayoutManager(layoutManager);
    mRecyclerView.setAdapter(mContentBlockAdapter);
    mRecyclerView.setItemAnimator(new MyAnimator());
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

  private void addEventBlock() { if (mContent.getRelatedSpot() != null && mContent.getRelatedSpot().getId() != null && mContent.getFromDate() != null && mContent.getToDate() != null) {
      ContentBlock contentBlockEvent = new ContentBlock();
      contentBlockEvent.setBlockType(-2);
      contentBlockEvent.setPublicStatus(true);
      mContentBlocks.add(contentBlockEvent);
    }
  }

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
    if (mContent.getTitle() != null && !mContent.getTitle().equalsIgnoreCase("") ||
            mContent.getDescription() != null && !mContent.getDescription().equalsIgnoreCase("")) {
      ContentBlock contentBlock0 = new ContentBlock();
      contentBlock0.setTitle(mContent.getTitle());
      contentBlock0.setBlockType(-1);
      contentBlock0.setPublicStatus(true);
      contentBlock0.setText(mContent.getDescription());
      mContentBlocks.add(0, contentBlock0);
    }

    if(mContent.getPublicImageUrl() != null) {
      ContentBlock contentBlock3 = new ContentBlock();
      contentBlock3.setTitle(null);
      contentBlock3.setBlockType(3);
      contentBlock3.setPublicStatus(true);
      contentBlock3.setFileId(mContent.getPublicImageUrl());
      contentBlock3.setScaleX(0);
      contentBlock3.setCopyright(mContent.getCoverImageCopyRight());
      mContentBlocks.add(1, contentBlock3);
    }
  }

  public void removeOfflineBlocks() {
    ArrayList<ContentBlock> removeBlocks = new ArrayList<>();
    for (ContentBlock block : mContentBlocks) {
      if (block.getBlockType() == 9 ||
              block.getBlockType() == 7) {
        removeBlocks.add(block);
      }

      if (block.getBlockType() == 2) {
        if (block.getVideoUrl().contains("youtu") ||
                block.getVideoUrl().contains("vimeo")) {
          removeBlocks.add(block);
        }
      }
    }

    mContentBlocks.removeAll(removeBlocks);
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
  private ArrayList<ContentBlock> removeStoreLinks(ArrayList<ContentBlock> contentBlocks) {
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
        OnXamoomContentFragmentInteractionListener listener = (OnXamoomContentFragmentInteractionListener) activity;
        mContentBlockAdapter.setOnXamoomContentFragmentInteractionListener(listener);
      } catch (ClassCastException e) {
        throw new ClassCastException(activity.toString()
                + " must implement OnXamoomContentFragmentInteractionListener");
      }
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
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

  // setters


  public void setRecyclerView(RecyclerView recyclerView) {
    mRecyclerView = recyclerView;
  }

  public void setContent(Content content) {
    setContent(content, true, false);
  }

  /**
   * Sets the content to display when fragment gets loaded.
   *
   * @param content Content to display.
   * @param addHeader Adds header (content title and content image).
   * @param offline Will notify contentBlocks to use offline functioniality and remove
   *                offline unavailable contentBlocks.
   */
  public void setContent(Content content, boolean addHeader, boolean offline) {
    if (content == null) {
      return;
    }

    this.mContent = content;
    mContentBlockAdapter.setContent(content);
    this.mContentID = content.getId();
    if (mContent.getContentBlocks() != null) {
      mContentBlocks.addAll(mContent.getContentBlocks());
    }

    // add header if needed choosen
    if (addHeader) {
      addContentTitleAndImage();
    } else if (mContent.getContentBlocks().get(0) != null) {
      mContent.getContentBlocks().get(0).setCoverImageCopyRight(mContent.getCoverImageCopyRight());
    }

    addEventBlock();

    // set offline modus inside adapter & remove blocks that cannot be displayed offline (like maps)
    if (offline && !showAllBlocksWhenOffline) {
      this.offline = offline;
      mContentBlockAdapter.setOffline(offline);

      removeOfflineBlocks();
    }

    if(!displayAllStoreLinks) {
      mContentBlocks = removeStoreLinks(mContentBlocks);
    }

    List<Integer> validTypes = Arrays.asList(validBlockTypes);

    ArrayList<ContentBlock> validContentBlocks = new ArrayList<>();

    for (ContentBlock block: mContentBlocks) {
      if (validTypes.contains(block.getBlockType())) {
        validContentBlocks.add(block);
      } else {
        String logString = String.format("Block Type %d not supported. ContentBlock at position %d will not be shown", block.getBlockType(), mContentBlocks.indexOf(block));
        Log.e("XamoomContentFragment", logString);
      }
    }

    mContentBlocks = validContentBlocks;
  }

  public void setBackgroundColor(int backgroundColor) {
    mBackgroundColor = backgroundColor;

    if (mRootView != null) {
      mRootView.setBackgroundColor(backgroundColor);
    }
  }

  public void setStyle(Style style) {
    mContentBlockAdapter.setStyle(style);

    if (style != null && style.getBackgroundColor() != null) {
      mBackgroundColor = Color.parseColor(style.getBackgroundColor());
    }
  }

  public void setDisplayAllStoreLinks(boolean displayAllStoreLinks) {
    this.displayAllStoreLinks = displayAllStoreLinks;
  }

  public void setShowSpotMapContentLinks(boolean showSpotMapContentLinks) {
    this.showSpotMapContentLinks = showSpotMapContentLinks;
  }

  public void setEnduserApi(EnduserApi enduserApi) {
    mEnduserApi = enduserApi;
    mContentBlockAdapter.setEnduserApi(enduserApi);
  }

  public EnduserApi getEnduserApi() {
    return mEnduserApi;
  }

  public String getYoutubeApiKey() {
    return mYoutubeApiKey;
  }

  public boolean isShowAllBlocksWhenOffline() {
    return showAllBlocksWhenOffline;
  }

  public void setShowAllBlocksWhenOffline(boolean showAllBlocksWhenOffline) {
    this.showAllBlocksWhenOffline = showAllBlocksWhenOffline;
  }


  @Override
  public void didScroll(int position) {
    getRecyclerView().getAdapter().notifyItemRangeChanged(position, getRecyclerView().getAdapter().getItemCount() - position - 1);
  }
}

class MyAnimator extends DefaultItemAnimator {
  @Override
  public boolean animateAdd(RecyclerView.ViewHolder holder) {
    Log.d("foo", "animateAdd");
    return super.animateAdd(holder);
  }

  @Override
  public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromX, int fromY, int toX, int toY) {
    Log.d("foo", "animateChange");
    return super.animateChange(oldHolder, newHolder, fromX, fromY, toX, toY);
  }

  @Override
  public boolean animateMove(RecyclerView.ViewHolder holder, int fromX, int fromY, int toX, int toY) {
    Log.d("foo", "animateMove");
    return super.animateMove(holder, fromX, fromY, toX, toY);
  }

  @Override
  public boolean animateRemove(RecyclerView.ViewHolder holder) {
    Log.d("foo", "animateRemove");
    return super.animateRemove(holder);
  }
}
