package com.xamoom.android.xamoomcontentblocks;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import com.xamoom.android.APICallback;
import com.xamoom.android.XamoomEndUserApi;
import com.xamoom.android.mapping.Content;
import com.xamoom.android.mapping.ContentBlocks.ContentBlock;
import com.xamoom.android.mapping.ContentBlocks.ContentBlockType0;
import com.xamoom.android.mapping.ContentBlocks.ContentBlockType3;
import com.xamoom.android.mapping.ContentBlocks.ContentBlockType4;
import com.xamoom.android.mapping.ContentBlocks.ContentBlockType6;
import com.xamoom.android.mapping.ContentById;
import com.xamoom.android.mapping.ContentByLocationIdentifier;
import com.xamoom.android.mapping.Menu;
import com.xamoom.android.mapping.Style;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import retrofit.RetrofitError;


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
    private static final String API_KEY_KEY = "ApiKeyKeyParam";

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
    private String mApiKey;

    private boolean loadFullContent = true;
    private boolean displayAllStoreLinks = false;
    private boolean isAnimated = false;

    private OnXamoomContentFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance.
     * You can set a special linkcolor as hex. (e.g. "00F")
     *
     * @param linkColor LinkColor as hex (e.g. "00F"), will be blue if null
     * @return XamoomContentFragment Returns an Instance of XamoomContentFragment
     */
    public static XamoomContentFragment newInstance(String linkColor, String apiKey) {
        XamoomContentFragment fragment = new XamoomContentFragment();
        Bundle args = new Bundle();

        if(linkColor == null)
            linkColor = "00F";
        args.putString(LINK_COLOR_KEY, linkColor);
        args.putString(API_KEY_KEY, apiKey);
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
            mApiKey = getArguments().getString(API_KEY_KEY);
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

        //check what to to
        if(mContent != null)
            addContentTitleAndImage();
        else if(mContentId != null) {
            loadDataWithContentId(mContentId);
        } else if(mLocationIdentifier != null) {
            loadDateWithLocationIdentifier(mLocationIdentifier, mBeaconId2);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v("pingeborg","XamoomContentBlocks - onStart");

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

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity().getApplicationContext()));

        mContentBlockAdapter = new ContentBlockAdapter(this, mContentBlocks, mLinkColor, mApiKey);
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
     * Load the data from xamoom cloud with a contentId.
     * It will load the full content.
     *
     * If you want to just load "synced" content from xamoom cloud,
     * you have to set {@link #loadFullContent} to true.
     *
     * @param mContentId ContentId from xamoom cloud content
     */
    private void loadDataWithContentId(final String mContentId) {
        mProgressbar.setVisibility(View.VISIBLE);

        XamoomEndUserApi.getInstance(this.getActivity(), mApiKey).getContentbyIdFull(mContentId, false, false, null, loadFullContent, new APICallback<ContentById>() {
            @Override
            public void finished(ContentById result) {
                mContent = result.getContent();
                addContentTitleAndImage();
                setupRecyclerView();
                mProgressbar.setVisibility(View.GONE);
            }

            @Override
            public void error(RetrofitError error) {
            }
        });
    }

    /**
     * Load the data from xamoom cloud with a locationIdentifier.
     * It wil always load the full content.
     *
     * @param mLocationIdentifier LocationIdentifier to load data from xamoom cloud.
     */
    private void loadDateWithLocationIdentifier(String mLocationIdentifier, String beaconId2) {
        mProgressbar.setVisibility(View.VISIBLE);

        XamoomEndUserApi.getInstance(this.getActivity(), mApiKey).getContentByLocationIdentifier(mLocationIdentifier, beaconId2, false, false, null, new APICallback<ContentByLocationIdentifier>() {
            @Override
            public void finished(ContentByLocationIdentifier result) {
                mContent = result.getContent();
                addContentTitleAndImage();
                setupRecyclerView();
                mProgressbar.setVisibility(View.GONE);
            }

            @Override
            public void error(RetrofitError error) {
            }
        });
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

        ContentBlockType0 cb0 = new ContentBlockType0(mContent.getTitle(), true, -1, mContent.getDescriptionOfContent());
        mContentBlocks.add(0, cb0);

        if(mContent.getImagePublicUrl() != null) {
            ContentBlockType3 cb3 = new ContentBlockType3(null, true, 3, mContent.getImagePublicUrl(), 0, getString(R.string.header_picture_alt_text));
            mContentBlocks.add(1, cb3);
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
            if (contentBlock.getContentBlockType() == 4) {
                ContentBlockType4 cb4 = (ContentBlockType4)contentBlock;
                if(cb4.getLinkType() == 15 || cb4.getLinkType() == 17) {
                    cbToRemove.add(contentBlock);
                }
            }
        }

        //remove all found linkBlocks with other Stores
        contentBlocks.removeAll(cbToRemove);

        return contentBlocks;
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

    public void setBeaconId2(String beaconId2) {
        mBeaconId2 = beaconId2;
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
    }

    public void contentBlockClick(Content content) {
        mListener.clickedContentBlock(content);
    }
}
