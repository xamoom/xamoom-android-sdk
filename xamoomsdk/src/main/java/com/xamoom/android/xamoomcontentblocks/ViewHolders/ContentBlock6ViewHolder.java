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
import com.xamoom.android.xamoomcontentblocks.XamoomContentFragment;
import com.xamoom.android.xamoomsdk.APICallback;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;

import java.util.HashMap;
import java.util.List;

import at.rags.morpheus.Error;


/**
 * ContentBlock
 */
public class ContentBlock6ViewHolder extends RecyclerView.ViewHolder {
  private Fragment mFragment;
  private TextView mTitleTextView;
  private TextView mDescriptionTextView;
  private LinearLayout mRootLayout;
  private ImageView mContentThumbnailImageView;
  private ProgressBar mProgressBar;
  private EnduserApi mEnduserApi;

  private static HashMap<String,Content> mSavedContentContentBlock = new HashMap<>();

  public ContentBlock6ViewHolder(View itemView, Fragment fragment, EnduserApi enduserApi) {
    super(itemView);
    mFragment = fragment;
    mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
    mDescriptionTextView = (TextView) itemView.findViewById(R.id.descriptionTextView);
    mRootLayout = (LinearLayout) itemView.findViewById(R.id.contentBlockLinearLayout);
    mContentThumbnailImageView = (ImageView) itemView.findViewById(R.id.contentThumbnailImageView);
    mProgressBar = (ProgressBar) itemView.findViewById(R.id.contentProgressBar);
    mEnduserApi = enduserApi;
  }

  public void setupContentBlock(ContentBlock contentBlock) {
    mContentThumbnailImageView.setImageDrawable(null);
    mTitleTextView.setText(null);
    mDescriptionTextView.setText(null);

    mProgressBar.setVisibility(View.VISIBLE);

    if(mSavedContentContentBlock.containsKey(contentBlock.getContentId())) {
      final Content result = mSavedContentContentBlock.get(contentBlock.getContentId());
      mTitleTextView.setText(result.getTitle());
      mDescriptionTextView.setText(result.getDescription());

      if (mFragment.isAdded()) {
        Glide.with(mFragment)
            .load(result.getPublicImageUrl())
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
          xamoomContentFragment.contentBlockClick(result);
        }
      });
    } else {
      mEnduserApi.getContent(contentBlock.getContentId(), new APICallback<Content, List<at.rags.morpheus.Error>>() {
        @Override
        public void finished(final Content result) {
          //save result
          mSavedContentContentBlock.put(result.getId(), result);

          mTitleTextView.setText(result.getTitle());
          mDescriptionTextView.setText(result.getDescription());

          if (mFragment.isAdded()) {
            Glide.with(mFragment)
                .load(result.getPublicImageUrl())
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
              xamoomContentFragment.contentBlockClick(result);
            }
          });
        }

        @Override
        public void error(List<Error> error) {

        }
      });
    }
  }
}