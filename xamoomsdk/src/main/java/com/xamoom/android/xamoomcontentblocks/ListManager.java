package com.xamoom.android.xamoomcontentblocks;

import android.util.SparseArray;

import com.xamoom.android.xamoomsdk.APIListCallback;
import com.xamoom.android.xamoomsdk.EnduserApi;
import com.xamoom.android.xamoomsdk.Enums.ContentFlags;
import com.xamoom.android.xamoomsdk.Enums.ContentSortFlags;
import com.xamoom.android.xamoomsdk.Enums.SpotFlags;
import com.xamoom.android.xamoomsdk.Resource.Content;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import at.rags.morpheus.Error;

public class ListManager {
  private SparseArray<ContentListItem> mContentListItemHashMap;
  private SparseArray<APIListCallback<List<Content>, List<Error>>> mCallbackHashMap;
  private EnduserApi mEnduserApi;

  public ListManager(EnduserApi enduserApi) {
    mEnduserApi = enduserApi;
    mContentListItemHashMap = new SparseArray<>();
    mCallbackHashMap = new SparseArray<>();
  }

  public void downloadContent(final int adapterPosition, final List<String> tags, int pageSize,
                              final APIListCallback<List<Content>, List<Error>> callback, boolean sortAsc) {
    mCallbackHashMap.put(adapterPosition, callback);
    ContentListItem listItem = mContentListItemHashMap.get(adapterPosition);
    if (listItem == null) {
      listItem = new ContentListItem(pageSize, null, true);
      mContentListItemHashMap.put(adapterPosition, listItem);
    }

    EnumSet<ContentSortFlags> flags = EnumSet.noneOf(ContentSortFlags.class);
    if (sortAsc) {
      flags.add(ContentSortFlags.NAME);
    } else {
      flags.add(ContentSortFlags.NAME_DESC);
    }

    mEnduserApi.downloadContentsByTags(tags, listItem.getPageSize(), listItem.getCursor(),
            flags, new APIListCallback<List<Content>, List<Error>>() {
          @Override
          public void finished(List<Content> result, String cursor, boolean hasMore) {
            mContentListItemHashMap.get(adapterPosition).setCursor(cursor);
            mContentListItemHashMap.get(adapterPosition).setHasMore(hasMore);
            mContentListItemHashMap.get(adapterPosition).getContents().addAll(result);

            if (callback != null) {
              callback.finished(mContentListItemHashMap.get(adapterPosition).getContents(), cursor, hasMore);
            }
          }

          @Override
          public void error(List<Error> error) {
            if (callback != null) {
              callback.error(error);
            }
          }
        });
  }


  public boolean hasMore(int position) {
    ContentListItem contentListItem = mContentListItemHashMap.get(position);
    if (contentListItem != null) {
      return contentListItem.getHasMore();
    }
    return false;
  }

  public ArrayList<Content> getContents(int position) {
    ContentListItem contentListItem = mContentListItemHashMap.get(position);
    if (contentListItem != null) {
      return contentListItem.getContents();
    }
    return null;
  }
}
