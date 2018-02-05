package com.xamoom.android.xamoomcontentblocks;

import com.xamoom.android.xamoomsdk.Resource.ContentBlock;

import java.util.HashMap;
import java.util.List;

/**
 * Created by raphaelseher on 05.01.18.
 */

public class ContentFragmentCache {
  private static ContentFragmentCache sharedInstance;

  private HashMap<String, List<ContentBlock>> savedBlocks = new HashMap<>();
  private HashMap<String, Integer> savedItemCounts = new HashMap<>();

  private ContentFragmentCache() {

  }

  static public ContentFragmentCache getSharedInstance() {
    if (sharedInstance == null) {
      sharedInstance = new ContentFragmentCache();
    }
    return sharedInstance;
  }

  public void save(String key, List<ContentBlock> content) {
    savedBlocks.put(key, content);
    increaseSavedItemsCount(key);
  }

  public List<ContentBlock> get(String key) {
    List<ContentBlock> list = savedBlocks.get(key);
    if (decreaseSavedItemsCount(key) == 0) {
      savedBlocks.remove(key);
      savedItemCounts.remove(key);
    }

    return list;
  }

  private void increaseSavedItemsCount(String key) {
    Integer count = savedItemCounts.get(key);
    if (count == null) {
      count = 1;
    } else {
      count += 1;
    }

    savedItemCounts.put(key, count);
  }

  private Integer decreaseSavedItemsCount(String key) {
    Integer count = savedItemCounts.get(key);

    // if there is no count saved
    if (count == null) {
      return -1;
    }

    count -= 1;
    savedItemCounts.put(key, count);

    return count;
  }
}
