package com.xamoom.android.xamoomcontentblocks.ViewHolders;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
public class ContentBlock6ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
  private XamoomContentFragment.OnXamoomContentFragmentInteractionListener mListener;
  private Context mContext;
  private TextView mTitleTextView;
  private TextView mDescriptionTextView;
  private ImageView mContentThumbnailImageView;
  private ProgressBar mProgressBar;
  private EnduserApi mEnduserApi;
  private LruCache<String, Content> mContentCache;
  private Content mContent;

  public ContentBlock6ViewHolder(View itemView, Context context, EnduserApi enduserApi, LruCache<String, Content> contentCache, XamoomContentFragment.OnXamoomContentFragmentInteractionListener listener) {
    super(itemView);
    mListener = listener;
    mContext = context;
    mTitleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
    mDescriptionTextView = (TextView) itemView.findViewById(R.id.descriptionTextView);
    mContentThumbnailImageView = (ImageView) itemView.findViewById(R.id.contentThumbnailImageView);
    mProgressBar = (ProgressBar) itemView.findViewById(R.id.contentProgressBar);
    mEnduserApi = enduserApi;
    mContentCache = contentCache;

    itemView.setOnClickListener(this);
  }

  public void setupContentBlock(ContentBlock contentBlock) {
    mContentThumbnailImageView.setImageDrawable(null);
    mTitleTextView.setText(null);
    mDescriptionTextView.setText(null);

    mProgressBar.setVisibility(View.VISIBLE);

    mContent = mContentCache.get(contentBlock.getContentId());

    if (mContent != null) {
      displayContent(mContent);
      mProgressBar.setVisibility(View.GONE);
    } else {
      loadContent(contentBlock.getContentId());
    }
  }

  private void loadContent(final String contentId) {
    mEnduserApi.getContent(contentId, new APICallback<Content, List<at.rags.morpheus.Error>>() {
      @Override
      public void finished(Content result) {
        mProgressBar.setVisibility(View.GONE);
        mContent = result;
        mContentCache.put(contentId, result);
        displayContent(result);
      }

      @Override
      public void error(List<Error> error) {
        mProgressBar.setVisibility(View.GONE);
        if (error != null && error.get(0) != null) {
          Log.e("XamoomContentBlocks", error.get(0).getCode() + " Error Title" + error.get(0).getTitle() + " Detail: " + error.get(0).getDetail());
        }
      }
    });
  }

  private void displayContent(Content content) {
    mTitleTextView.setText(content.getTitle());
    mDescriptionTextView.setText(content.getDescription());

    Glide.with(mContext)
        .load(content.getPublicImageUrl())
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .crossFade()
        .centerCrop()
        .into(mContentThumbnailImageView);
  }

  @Override
  public void onClick(View v) {
    if (mContent != null) {
      mListener.clickedContentBlock(mContent);
    }
  }
}