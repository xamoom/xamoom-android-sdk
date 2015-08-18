package com.xamoom.android.xamoomcontentblocks.ViewHolders;


import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xamoom.android.APICallback;
import com.xamoom.android.XamoomEndUserApi;
import com.xamoom.android.mapping.ContentBlocks.ContentBlockType6;
import com.xamoom.android.mapping.ContentById;
import com.xamoom.android.xamoomcontentblocks.R;
import com.xamoom.android.xamoomcontentblocks.XamoomContentFragment;

import java.util.HashMap;

import retrofit.RetrofitError;

/**
 * ContentBlock
 */
public class ContentBlock6ViewHolder extends RecyclerView.ViewHolder {
    private Fragment mFragment;
    private String mApiKey;
    private TextView mTitleTextView;
    private TextView mDescriptionTextView;
    private LinearLayout mRootLayout;
    private ImageView mContentThumbnailImageView;
    private ProgressBar mProgressBar;

    private static HashMap<String,ContentById> mSavedContentContentBlock = new HashMap<>();


    public ContentBlock6ViewHolder(View itemView, Fragment fragment, String apiKey) {
        super(itemView);
        mFragment = fragment;
        mApiKey = apiKey;
        mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
        mDescriptionTextView = (TextView) itemView.findViewById(R.id.descriptionTextView);
        mRootLayout = (LinearLayout) itemView.findViewById(R.id.contentBlockLinearLayout);
        mContentThumbnailImageView = (ImageView) itemView.findViewById(R.id.contentThumbnailImageView);
        mProgressBar = (ProgressBar) itemView.findViewById(R.id.contentProgressBar);
    }

    public void setupContentBlock(final ContentBlockType6 cb6) {
        mContentThumbnailImageView.setImageDrawable(null);
        mTitleTextView.setText(null);
        mDescriptionTextView.setText(null);

        mProgressBar.setVisibility(View.VISIBLE);

        if(mSavedContentContentBlock.containsKey(cb6.getContentId())) {
            final ContentById result = mSavedContentContentBlock.get(cb6.getContentId());
            mTitleTextView.setText(result.getContent().getTitle());
            mDescriptionTextView.setText(result.getContent().getDescriptionOfContent());

            if (mFragment.isAdded()) {
                Glide.with(mFragment)
                        .load(result.getContent().getImagePublicUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .crossFade()
                        .centerCrop()
                        .into(mContentThumbnailImageView);
            }

            mProgressBar.setVisibility(View.GONE);

            mRootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    XamoomContentFragment xamoomContentFragment = (XamoomContentFragment) mFragment;
                    xamoomContentFragment.contentBlockClick(result.getContent());
                }
            });
        } else {
            XamoomEndUserApi.getInstance(mFragment.getActivity().getApplicationContext(), mApiKey).getContentbyIdFull(cb6.getContentId(), false, false, null, false, new APICallback<ContentById>() {
                @Override
                public void finished(final ContentById result) {
                    //save result
                    mSavedContentContentBlock.put(cb6.getContentId(), result);

                    mTitleTextView.setText(result.getContent().getTitle());
                    mDescriptionTextView.setText(result.getContent().getDescriptionOfContent());

                    if (mFragment.isAdded()) {
                        Glide.with(mFragment)
                                .load(result.getContent().getImagePublicUrl())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .crossFade()
                                .centerCrop()
                                .into(mContentThumbnailImageView);
                    }

                    mProgressBar.setVisibility(View.GONE);

                    mRootLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            XamoomContentFragment xamoomContentFragment = (XamoomContentFragment) mFragment;
                            xamoomContentFragment.contentBlockClick(result.getContent());
                        }
                    });
                }

                @Override
                public void error(RetrofitError error) {
                }
            });
        }
    }
}