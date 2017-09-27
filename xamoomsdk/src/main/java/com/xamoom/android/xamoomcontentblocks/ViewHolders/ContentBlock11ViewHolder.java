package com.xamoom.android.xamoomcontentblocks.ViewHolders;

import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.Resource.ContentBlock;
import com.xamoom.android.xamoomsdk.Resource.Style;

public class ContentBlock11ViewHolder extends RecyclerView.ViewHolder  {

  private Style mStyle;

  public ContentBlock11ViewHolder(View view, Fragment fragment, EnduserApi enduserApi, LruCache contentCache) {
    super(view);
  }

  public void setupContentBlock(ContentBlock contentBlock, boolean offline) {

  }

  public void setStyle(Style style) {
    mStyle = style;
  }
}
