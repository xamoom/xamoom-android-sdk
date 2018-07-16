package com.xamoom.android.xamoomcontentblocks.ViewHolders;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xamoom.android.xamoomcontentblocks.Adapters.AdapterDelegatesManager;
import com.xamoom.android.xamoomcontentblocks.Adapters.ContentBlockAdapter;
import com.xamoom.android.xamoomcontentblocks.ListManager;
import com.xamoom.android.xamoomcontentblocks.XamoomContentFragment;
import com.xamoom.android.xamoomsdk.APIListCallback;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.R;
import com.xamoom.android.xamoomsdk.Resource.Content;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Style;

import java.util.ArrayList;
import java.util.List;

import at.rags.morpheus.Error;

public class ContentBlock11ViewHolder extends RecyclerView.ViewHolder  {
  private TextView mErrorTextView;
  private TextView mTitleTextView;
  private RecyclerView mRecyclerView;
  private Button mLoadMoreButton;
  private ProgressBar mProgressBar;
  private EnduserApi mEnduserApi;
  private ContentBlockAdapter adapter;
  private ListManager mListManager;
  private Style mStyle;
  private ContentBlock mContentBlock;
  private int childrenMargin;

  private String mCursor = null;
  private boolean hasMore = true;
  private boolean[] test = new boolean[1];

  public ContentBlock11ViewHolder(View view, Fragment fragment, EnduserApi enduserApi,
                                  ListManager listManager,
                                  AdapterDelegatesManager adapterDelegatesManager, XamoomContentFragment.OnXamoomContentFragmentInteractionListener
                                      onXamoomContentFragmentInteractionListener) {
    super(view);
    mEnduserApi = enduserApi;
    mListManager = listManager;
    childrenMargin = fragment.getContext().getResources().getDimensionPixelSize(R.dimen.contentblock_children_margin);

    mErrorTextView = (TextView) view.findViewById(R.id.content_list_error_text_view);
    mTitleTextView = (TextView) view.findViewById(R.id.content_list_title_text_view);
    mRecyclerView = (RecyclerView) view.findViewById(R.id.content_list_recycler_view);
    mProgressBar = (ProgressBar) view.findViewById(R.id.content_list_progress_bar);
    mLoadMoreButton = (Button) view.findViewById(R.id.content_list_load_more_button);

    setupButton(fragment.getContext());
    setupRecyclerView(fragment, enduserApi, adapterDelegatesManager,onXamoomContentFragmentInteractionListener);
  }

  private void setupButton(Context context) {
    // set color depending on colorAccent with default color as fallback (darkgray)
    // if style is set, it will use this color as button text color
    int color = getThemeColor(context, R.attr.colorAccent, mLoadMoreButton.getCurrentTextColor());
    if (mStyle != null && mStyle.getHighlightFontColor() != null) {
      color = Color.parseColor(mStyle.getHighlightFontColor());
    }
    mLoadMoreButton.setTextColor(color);

    mLoadMoreButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        showLoading();
        downloadContent(mContentBlock, mContentBlock.getContentListSortAsc());
      }
    });
  }

  private void setupRecyclerView(Fragment fragment, EnduserApi enduserApi,
                                 AdapterDelegatesManager adapterDelegatesManager, XamoomContentFragment.OnXamoomContentFragmentInteractionListener onXamoomContentFragmentInteractionListener) {
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(fragment.getContext());
    layoutManager.setAutoMeasureEnabled(true);
    mRecyclerView.setNestedScrollingEnabled(false);
    mRecyclerView.setLayoutManager(layoutManager);

    XamoomContentFragment contentFragment = (XamoomContentFragment) fragment;

    adapter = new ContentBlockAdapter(fragment, new ArrayList<ContentBlock>(),
        contentFragment.isShowSpotMapContentLinks(), contentFragment.getYoutubeApiKey(), contentFragment);
    adapter.setEnduserApi(enduserApi);
    adapter.setOnXamoomContentFragmentInteractionListener(onXamoomContentFragmentInteractionListener);
    adapter.getDelegatesManager().setAdapterDelegates(adapterDelegatesManager.getAdapterDelegates());
    mRecyclerView.setAdapter(adapter);
  }

  public void setupContentBlock(final ContentBlock contentBlock, boolean offline) {
    mContentBlock = contentBlock;
    mErrorTextView.setVisibility(View.GONE);
    mLoadMoreButton.setVisibility(View.GONE);
    mTitleTextView.setVisibility(View.VISIBLE);
    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mTitleTextView.getLayoutParams();
    params.setMargins(0,0,0, childrenMargin);
    mTitleTextView.setLayoutParams(params);

    adapter.setOffline(offline);

    clearContentBlocks();

    // set title text or remove the layoutParams to have same margins as when using a title
    if (contentBlock.getTitle() != null && !contentBlock.getTitle().equalsIgnoreCase("")) {
      mTitleTextView.setText(contentBlock.getTitle());
    } else {
      mTitleTextView.setVisibility(View.GONE);
      params = (LinearLayout.LayoutParams) mTitleTextView.getLayoutParams();
      params.setMargins(0,0,0,0);
      mTitleTextView.setLayoutParams(params);
    }

    ArrayList<Content> contents = mListManager.getContents(getAdapterPosition());
    if (contents != null) {
      hideLoading();

      if (!mListManager.hasMore(getAdapterPosition())) {
        mLoadMoreButton.setVisibility(View.GONE);
      } else {
        mLoadMoreButton.setVisibility(View.VISIBLE);
      }
      
      displayContents(contents);
    } else {
      showLoading();
      downloadContent(contentBlock, contentBlock.getContentListSortAsc());
    }
  }

  private void downloadContent(ContentBlock contentBlock, boolean sortAsc) {
    mListManager.downloadContent(getAdapterPosition(), contentBlock.getContentListTags(),
        contentBlock.getContentListPageSize(), new APIListCallback<List<Content>, List<Error>>() {
          @Override
          public void finished(List<Content> result, String cursor, boolean hasMore) {
            hideLoading();

            if (!hasMore) {
              mLoadMoreButton.setVisibility(View.GONE);
            }

            displayContents((ArrayList<Content>) result);
          }

          @Override
          public void error(List<Error> error) {
            hideLoading();
            mErrorTextView.setVisibility(View.VISIBLE);
          }
        }, sortAsc);
  }

  private void showLoading() {
    mLoadMoreButton.setVisibility(View.GONE);
    mProgressBar.setVisibility(View.VISIBLE);
  }

  private void hideLoading() {
    mLoadMoreButton.setVisibility(View.VISIBLE);
    mProgressBar.setVisibility(View.GONE);
  }

  private void clearContentBlocks() {
    adapter.setContentBlocks(new ArrayList<ContentBlock>(0));
    adapter.notifyDataSetChanged();
  }

  @ColorInt
  private int getThemeColor(@NonNull final Context context, @AttrRes final int attributeColor, int fallback) {
    final TypedValue value = new TypedValue();
    value.data = fallback;
    context.getTheme().resolveAttribute (attributeColor, value, true);
    return value.data;
  }

  private void displayContents(ArrayList<Content> contents) {
    adapter.getContentBlocks().clear();
    for (Content content : contents) {
      ContentBlock block = new ContentBlock();
      block.setContentId(content.getId());
      block.setBlockType(6);
      adapter.getContentBlocks().add(block);
    }
    adapter.notifyDataSetChanged();
  }

  public void setStyle(Style style) {
    mStyle = style;
    adapter.setStyle(style);
  }
}
