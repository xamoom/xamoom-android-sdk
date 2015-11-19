package com.xamoom.android.xamoomcontentblocks;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xamoom.android.mapping.ContentBlocks.ContentBlock;
import com.xamoom.android.mapping.ContentBlocks.ContentBlockType0;
import com.xamoom.android.mapping.ContentBlocks.ContentBlockType1;
import com.xamoom.android.mapping.ContentBlocks.ContentBlockType2;
import com.xamoom.android.mapping.ContentBlocks.ContentBlockType3;
import com.xamoom.android.mapping.ContentBlocks.ContentBlockType4;
import com.xamoom.android.mapping.ContentBlocks.ContentBlockType5;
import com.xamoom.android.mapping.ContentBlocks.ContentBlockType6;
import com.xamoom.android.mapping.ContentBlocks.ContentBlockType7;
import com.xamoom.android.mapping.ContentBlocks.ContentBlockType8;
import com.xamoom.android.mapping.ContentBlocks.ContentBlockType9;
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

import java.util.List;

/**
 * ContentBlockAdapter will display all the contentBlocks you get from the xamoom cloud.
 *
 * @author Raphael Seher
 */
public class ContentBlockAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Fragment mFragment;
    private List<ContentBlock> mContentBlocks;
    private String mLinkColor;
    private String mApiKey;
    private boolean showContentLinks;

    /**
     * Constructor for the Adapter.
     *
     * @param fragment Fragment with the recyclerView in it.
     * @param contentBlocks ContentBlocks to display.
     * @param linkColor LinkColor as hex (e.g. "00F"), will be blue if null
     */
    public ContentBlockAdapter(Fragment fragment, List<ContentBlock> contentBlocks,
                               String linkColor, String apiKey, boolean showSpotMapContentLinks) {
        mFragment = fragment;
        mContentBlocks = contentBlocks;
        mLinkColor = linkColor;
        mApiKey = apiKey;
        showContentLinks = showSpotMapContentLinks;
    }

    @Override
    public int getItemViewType(int position) {
        return mContentBlocks.get(position).getContentBlockType();
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
                return new ContentBlock2ViewHolder(view2, mFragment);
            case 3:
                View view3 = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.content_block_3_layout, parent, false);
                return new ContentBlock3ViewHolder(view3, mFragment);
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
                return new ContentBlock6ViewHolder(view6, mFragment, mApiKey);
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
                return new ContentBlock9ViewHolder(view9, mFragment, mApiKey);
            default:
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_layout, parent, false);
                return new ViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ContentBlock cb = mContentBlocks.get(position);

        switch (cb.getContentBlockType()) {
            case -1:
                ContentBlockType0 cb00 = (ContentBlockType0)cb;
                ContentHeaderViewHolder newHeaderHolder = (ContentHeaderViewHolder) holder;
                newHeaderHolder.setLinkColor(mLinkColor);
                newHeaderHolder.setupContentBlock(cb00);
                break;
            case 0:
                ContentBlockType0 cb0 = (ContentBlockType0)cb;
                ContentBlock0ViewHolder newHolder = (ContentBlock0ViewHolder) holder;
                newHolder.setLinkColor(mLinkColor);
                newHolder.setupContentBlock(cb0);
                break;
            case 1:
                ContentBlockType1 cb1 = (ContentBlockType1)cb;
                ContentBlock1ViewHolder newHolder1 = (ContentBlock1ViewHolder) holder;
                newHolder1.setupContentBlock(cb1);
                break;
            case 2:
                ContentBlockType2 cb2 = (ContentBlockType2)cb;
                ContentBlock2ViewHolder newHolder2 = (ContentBlock2ViewHolder) holder;
                newHolder2.setupContentBlock(cb2);
                break;
            case 3:
                ContentBlockType3 cb3 = (ContentBlockType3)cb;
                ContentBlock3ViewHolder newHolder3 = (ContentBlock3ViewHolder) holder;
                newHolder3.setupContentBlock(cb3);
                break;
            case 4:
                ContentBlockType4 cb4 = (ContentBlockType4) cb;
                ContentBlock4ViewHolder newHolder4 = (ContentBlock4ViewHolder) holder;
                newHolder4.setupContentBlock(cb4);
                break;
            case 5:
                ContentBlockType5 cb5 = (ContentBlockType5) cb;
                ContentBlock5ViewHolder newHolder5 = (ContentBlock5ViewHolder) holder;
                newHolder5.setupContentBlock(cb5);
                break;
            case 6:
                ContentBlockType6 cb6 = (ContentBlockType6) cb;
                ContentBlock6ViewHolder newHolder6 = (ContentBlock6ViewHolder) holder;
                newHolder6.setupContentBlock(cb6);
                break;
            case 7:
                ContentBlockType7 cb7 = (ContentBlockType7) cb;
                ContentBlock7ViewHolder newHolder7 = (ContentBlock7ViewHolder) holder;
                newHolder7.setupContentBlock(cb7);
                break;
            case 8:
                ContentBlockType8 cb8 = (ContentBlockType8) cb;
                ContentBlock8ViewHolder newHolder8 = (ContentBlock8ViewHolder) holder;
                newHolder8.setupContentBlock(cb8);
                break;
            case 9:
                ContentBlockType9 cb9 = (ContentBlockType9) cb;
                ContentBlock9ViewHolder newHolder9 = (ContentBlock9ViewHolder) holder;
                newHolder9.setupContentBlock(cb9);
                newHolder9.showContentLinks = showContentLinks;
                break;
        }
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
