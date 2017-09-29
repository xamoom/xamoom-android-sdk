package com.xamoom.android.xamoomcontentblocks.ViewHolders;

import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xamoom.android.xamoomcontentblocks.Adapters.ContentBlockAdapter;
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
  private TextView mTitleTextView;
  private RecyclerView mRecyclerView;
  private Button mLoadMoreButton;
  private EnduserApi mEnduserApi;
  private ContentBlockAdapter adapter;
  private Style mStyle;
  private ArrayList<Content> mContents = new ArrayList<>();

  private String cursor = null;
  private boolean hasMore = true;

  public ContentBlock11ViewHolder(View view, Fragment fragment, EnduserApi enduserApi,
                                  LruCache contentCache,
                                  XamoomContentFragment.OnXamoomContentFragmentInteractionListener
                                      onXamoomContentFragmentInteractionListener) {
    super(view);
    mEnduserApi = enduserApi;

    mTitleTextView = (TextView) view.findViewById(R.id.content_list_title_text_view);

    mRecyclerView = (RecyclerView) view.findViewById(R.id.content_list_recycler_view);

    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(fragment.getContext());
    layoutManager.setAutoMeasureEnabled(true);
    mRecyclerView.setLayoutManager(layoutManager);

    adapter = new ContentBlockAdapter(fragment, new ArrayList<ContentBlock>(),
        false, "", null);
    adapter.setEnduserApi(enduserApi);
    adapter.setOnXamoomContentFragmentInteractionListener(onXamoomContentFragmentInteractionListener);
    mRecyclerView.setAdapter(adapter);

    mLoadMoreButton = (Button) view.findViewById(R.id.content_list_load_more_button);
    mLoadMoreButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Log.v("Test", "Click");
      }
    });
  }

  public void setupContentBlock(final ContentBlock contentBlock, boolean offline) {
    // Load data
    // Show Data
    mEnduserApi.getContentsByTags(contentBlock.getContentListTags(), contentBlock.getContentListPageSize(), cursor,
        null, new APIListCallback<List<Content>, List<Error>>() {
          @Override
          public void finished(List<Content> result, String cursor, boolean hasMore) {
            mContents.addAll(result);

            ArrayList<ContentBlock> blocks = new ArrayList<>();
            for (Content content : mContents) {
              ContentBlock contentBlock6 = new ContentBlock();
              contentBlock6.setBlockType(6);
              contentBlock6.setContentId(content.getId());
              blocks.add(contentBlock6);
            }

            adapter.setContentBlocks(blocks);
            adapter.notifyDataSetChanged();
          }

          @Override
          public void error(List<Error> error) {

          }
        });

    /*
    ContentBlock contentBlock6 = new ContentBlock();
    contentBlock6.setBlockType(6);
    contentBlock6.setContentId("7cf2c58e6d374ce3888c32eb80be53b5");
    contentBlock6.setText("Content Title");
    ArrayList<ContentBlock> blocks = new ArrayList<>();
    blocks.add(contentBlock6);
    blocks.add(contentBlock6);
    blocks.add(contentBlock6);

    adapter.setContentBlocks(blocks);
    adapter.notifyDataSetChanged();
    */
  }

  public void setStyle(Style style) {
    mStyle = style;
  }
}
