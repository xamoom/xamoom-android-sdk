package com.xamoom.android.xamoomsdk.Resource.Meta;

import com.google.gson.annotations.SerializedName;

/**
 * Created by raphaelseher on 03/03/16.
 */
public class PagingMeta {
  @SerializedName("cursor")
  private String cursor;
  @SerializedName("has-more")
  private boolean hasMore;

  public String getCursor() {
    return cursor;
  }

  public boolean hasMore() {
    return hasMore;
  }
}
