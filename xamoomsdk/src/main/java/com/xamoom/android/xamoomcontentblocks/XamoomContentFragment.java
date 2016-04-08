package com.xamoom.android.xamoomcontentblocks;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Menu;
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
 * To get started with the XamoomContentFragment create a new Instance via {@link #newInstance(String, String)}.
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
public class XamoomContentFragment extends Fragment {
  public static final String XAMOOM_CONTENT_ID = "xamoomContentId";
  public static final String XAMOOM_LOCATION_IDENTIFIER = "xamoomLocationIdentifier";

  private static final String LINK_COLOR_KEY = "LinkColorKeyParam";
  private static final String YOUTUBE_API_KEY = "YoutubeApiKeyParam";


  private RecyclerView mRecyclerView;
  private ProgressBar mProgressbar;
  private ContentBlockAdapter mContentBlockAdapter;

  private String mContentId;
  private String mLocationIdentifier;
  private String mBeaconId2;
  private Content mContent;

  private List<ContentBlock> mContentBlocks;
  private Style mStyle;
  private Menu mMenu;
  private String mLinkColor;
  private String mYoutubeApiKey;
  private EnduserApi mEnduserApi;

  private boolean loadFullContent = true;
  private boolean displayAllStoreLinks = false;
  private boolean showSpotMapContentLinks = false;
  private boolean isAnimated = false;

  private OnXamoomContentFragmentInteractionListener mListener;

  /**
   * Use this factory method to create a new instance.
   * You can set a special linkcolor as hex. (e.g. "00F")
   *
   * @param linkColor LinkColor as hex (e.g. "00F"), will be blue if null
   * @return XamoomContentFragment Returns an Instance of XamoomContentFragment
   */
  public static XamoomContentFragment newInstance(@Nullable String linkColor,
                                                 @NonNull String youtubeApiKey) {
    XamoomContentFragment fragment = new XamoomContentFragment();
    Bundle args = new Bundle();

    if(linkColor == null) {
      linkColor = "00F";
    }

    args.putString(LINK_COLOR_KEY, linkColor);
    args.putString(YOUTUBE_API_KEY, youtubeApiKey);
    fragment.setArguments(args);
    return fragment;
  }

  public XamoomContentFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mLinkColor = getArguments().getString(LINK_COLOR_KEY);
      mYoutubeApiKey = getArguments().getString(YOUTUBE_API_KEY);
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_xamoom_content, container, false);
    mRecyclerView = (RecyclerView) view.findViewById(R.id.contentBlocksRecycler);
    mProgressbar = (ProgressBar) view.findViewById(R.id.progressBar);

    if (mContent != null) {
      addContentTitleAndImage();
    }

    return view;
  }

  @Override
  public void onStart() {
    super.onStart();

    //if there is no animation, the recyclerview will be setup here
    if(!isAnimated) {
      setupRecyclerView();
    }
  }

  /**
   * Setup the recyclerview.
   */
  private void setupRecyclerView() {
    if(mContent == null || mContentBlockAdapter != null)
      return;

    mRecyclerView.setLayoutManager(
        new LinearLayoutManager(this.getActivity().getApplicationContext()));

    mContentBlockAdapter = new ContentBlockAdapter(this, mContentBlocks, mLinkColor, mEnduserApi,
        showSpotMapContentLinks, mYoutubeApiKey);
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
      anim.setAnimationListener(new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
          //if there is an animation, the recyclerview will be setup here
          setupRecyclerView();
        }
      });

      return anim;
    }

    return super.onCreateAnimation(transit, enter, nextAnim);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
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
    mContentBlocks = new LinkedList<>();
    mContentBlocks.addAll(mContent.getContentBlocks());

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

    if(!displayAllStoreLinks)
      mContentBlocks = removeStoreLinks(mContentBlocks);
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
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      mListener = (OnXamoomContentFragmentInteractionListener) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException(activity.toString()
          + " must implement OnXamoomContentFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
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

  public void spotMapContentLinkClick(String contentId) {
    mListener.clickedSpotMapContentLink(contentId);
  }

  //setters
  public void setContent(Content content) {
    this.mContent = content;
  }

  public void setMenu(Menu mMenu) {
    this.mMenu = mMenu;
  }

  public void setLoadFullContent(boolean loadFullContent) {
    this.loadFullContent = loadFullContent;
  }

  public void setStyle(Style mStyle) {
    this.mStyle = mStyle;
  }

  public void setIsStoreLinksActivated(boolean isStoreLinksActivated) {
    this.displayAllStoreLinks = isStoreLinksActivated;
  }

  public void setContentId(String mContentId) {
    this.mContentId = mContentId;
  }

  public void setLocationIdentifier(String mLocationIdentifier) {
    this.mLocationIdentifier = mLocationIdentifier;
  }

  public void setShowSpotMapContentLinks(boolean showSpotMapContentLinks) {
    this.showSpotMapContentLinks = showSpotMapContentLinks;
  }

  public void setBeaconId2(String beaconId2) {
    mBeaconId2 = beaconId2;
  }

  public void setEnduserApi(EnduserApi enduserApi) {
    mEnduserApi = enduserApi;
  }
}
