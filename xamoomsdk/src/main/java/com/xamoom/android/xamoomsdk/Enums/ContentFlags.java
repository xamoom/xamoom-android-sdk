package com.xamoom.android.xamoomsdk.Enums;

import com.xamoom.android.xamoomsdk.APICallback;
import com.xamoom.android.xamoomsdk.EnduserApi;

import java.util.EnumSet;

/**
 * ContentFlags are options you can use when getting content.
 *
 * @see EnduserApi#getContent(String, EnumSet, APICallback)
 */
public enum ContentFlags {
  /**
   * Use preview when you don't want to have statistics recorded.
   */
  PREVIEW,
  /**
   * Use private if you only want to receive {@link com.xamoom.android.xamoomsdk.Resource.ContentBlock}
   * with an *unchecked* "mobile exclusive" in the xamoom-backend.
   */
  PRIVATE
}
