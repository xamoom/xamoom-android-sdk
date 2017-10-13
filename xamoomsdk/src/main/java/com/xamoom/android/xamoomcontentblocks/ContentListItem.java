package com.xamoom.android.xamoomcontentblocks;

import com.xamoom.android.xamoomsdk.Resource.Content;

import java.util.ArrayList;

public class ContentListItem {
  private ArrayList<Content> mContents;
  private String mCursor = null;
  private int pageSize = 10;
  private Boolean hasMore = false;

  public ContentListItem(int pageSize, String cursor, Boolean hasMore) {
    mContents = new ArrayList<>();
    this.pageSize = pageSize;
    this.mCursor = cursor;
    this.hasMore = hasMore;
  }

  public ArrayList<Content> getContents() {
    return mContents;
  }

  public void setContents(ArrayList<Content> contents) {
    mContents = contents;
  }

  public String getCursor() {
    return mCursor;
  }

  public void setCursor(String cursor) {
    mCursor = cursor;
  }

  public Boolean getHasMore() {
    return hasMore;
  }

  public void setHasMore(Boolean hasMore) {
    this.hasMore = hasMore;
  }

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }
}
