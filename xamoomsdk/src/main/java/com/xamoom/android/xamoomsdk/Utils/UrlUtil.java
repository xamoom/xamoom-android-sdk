package com.xamoom.android.xamoomsdk.Utils;

import android.text.TextUtils;

import com.xamoom.android.xamoomsdk.Enums.ContentFlags;
import com.xamoom.android.xamoomsdk.Enums.ContentSortFlags;
import com.xamoom.android.xamoomsdk.Enums.SpotFlags;
import com.xamoom.android.xamoomsdk.Enums.SpotSortFlags;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by raphaelseher on 04/04/16.
 */
public class UrlUtil {
  public static Map<String, String> getUrlParameter(String language) {
    Map<String, String> params = new LinkedHashMap<>();
    if (language != null) {
      params.put("lang", language);
    }
    return params;
  }

  public static Map<String, String> addContentParameter(Map<String, String> params,
                                                 EnumSet<ContentFlags> contentFlags) {
    if (contentFlags == null) {
      return params;
    }

    if (contentFlags.contains(ContentFlags.PREVIEW)) {
      params.put("preview", "true");
    }

    if (contentFlags.contains(ContentFlags.PRIVATE)) {
      params.put("public-only", "true");
    }

    return params;
  }

  public static Map<String, String> addContentSortingParameter(Map<String, String> params,
                                                        EnumSet<ContentSortFlags> contentSortFlags) {
    if (contentSortFlags == null) {
      return params;
    }

    ArrayList<String> sortParams = new ArrayList<String>();

    if (contentSortFlags.contains(ContentSortFlags.NAME)) {
      sortParams.add("name");
    }

    if (contentSortFlags.contains(ContentSortFlags.NAME_DESC)) {
      sortParams.add("-name");
    }

    params.put("sort", TextUtils.join(",", sortParams));
    return params;
  }

  public static Map<String, String> addSpotParameter(Map<String, String> params,
                                              EnumSet<SpotFlags> spotFlags) {
    if (spotFlags == null) {
      return params;
    }

    if (spotFlags.contains(SpotFlags.INCLUDE_CONTENT)) {
      params.put("include_content", "true");
    }

    if (spotFlags.contains(SpotFlags.INCLUDE_MARKERS)) {
      params.put("include_markers", "true");
    }

    return params;
  }

  public static Map<String, String> addSpotSortingParameter(Map<String, String> params,
                                                     EnumSet<SpotSortFlags> spotSortFlags) {
    if (spotSortFlags == null) {
      return params;
    }

    ArrayList<String> sortParams = new ArrayList<String>();

    if (spotSortFlags.contains(SpotSortFlags.NAME)) {
      sortParams.add("name");
    }

    if (spotSortFlags.contains(SpotSortFlags.NAME_DESC)) {
      sortParams.add("-name");
    }

    if (spotSortFlags.contains(SpotSortFlags.DISTANCE)) {
      sortParams.add("distance");
    }

    if (spotSortFlags.contains(SpotSortFlags.DISTANCE_DESC)) {
      sortParams.add("-distance");
    }

    params.put("sort", TextUtils.join(",", sortParams));
    return params;
  }

  public static Map<String, String> addPagingToUrl(Map<String, String> params, int pageSize,
                                            String cursor) {
    if (pageSize != 0) {
      params.put("page[size]", Integer.toString(pageSize));
    }

    if (cursor != null) {
      params.put("page[cursor]", cursor);
    }

    return params;
  }

}
