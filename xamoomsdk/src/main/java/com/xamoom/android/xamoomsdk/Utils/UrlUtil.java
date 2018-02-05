/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomsdk.Utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.xamoom.android.xamoomsdk.Enums.ContentFlags;
import com.xamoom.android.xamoomsdk.Enums.ContentSortFlags;
import com.xamoom.android.xamoomsdk.Enums.SpotFlags;
import com.xamoom.android.xamoomsdk.Enums.SpotSortFlags;
import com.xamoom.android.xamoomsdk.Filter;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;

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

    if (contentSortFlags.contains(ContentSortFlags.FROM_DATE)) {
      sortParams.add("meta-datetime-from");
    }

    if (contentSortFlags.contains(ContentSortFlags.FROM_DATE_DESC)) {
      sortParams.add("-meta-datetime-from");
    }

    if (contentSortFlags.contains(ContentSortFlags.TO_DATE)) {
      sortParams.add("meta-datetime-to");
    }

    if (contentSortFlags.contains(ContentSortFlags.TO_DATE_DESC)) {
      sortParams.add("-meta-datetime-to");
    }

    params.put("sort", JsonListUtil.joinList(sortParams, ","));
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

    if (spotFlags.contains(SpotFlags.HAS_LOCATION)) {
      params.put("filter[has-location]", "true");
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

    params.put("sort", JsonListUtil.joinList(sortParams, ","));
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


  public static Map<String, String> addConditionsToUrl(@NonNull Map<String, String> params,
                                                       @NonNull Map<String, Object> conditions) {
    if (conditions == null || params == null) {
      return params;
    }

    for (String key : conditions.keySet()) {
      String value = conditionToString(conditions.get(key));
      if (value != null) {
        String paramKey = String.format("condition[%s]", key);
        params.put(paramKey, value);
      }
    }
    return params;
  }

  public static Map<String, String> addTagsFilter(@NonNull Map<String, String> params,
                                                  @NonNull ArrayList<String> tags) {
    params.put("filter[tags]", JsonListUtil.listToJsonArray(tags, ","));
    return params;
  }

  public static Map<String, String> addNameFilter(@NonNull Map<String, String> params,
                                                 @NonNull String name) {
    params.put("filter[name]", name);
    return params;
  }

  public static Map<String, String> addDateFilter(@NonNull Map<String, String> params, @Nullable Date fromDate, @Nullable Date toDate) {
    if (fromDate != null) {
      params.put("filter[meta-datetime-from]", DateUtil.format(fromDate));
    }

    if (toDate != null) {
      params.put("filter[meta-datetime-to]", DateUtil.format(toDate));
    }

    return params;
  }

  public static Map<String, String> addRelatedSpotIdFilter(@NonNull Map<String, String> params,
                                                           @NonNull String spotId) {
    params.put("filter[related-spot]", spotId);
    return params;
  }

  public static Map<String, String> addFilters(@NonNull Map<String, String> params,
                                               @NonNull Filter filter) {
    if (filter.getName() != null) {
      params = addNameFilter(params, filter.getName());
    }

    if (filter.getTags() != null) {
      params = addTagsFilter(params, filter.getTags());
    }

    params = addDateFilter(params, filter.getFromDate(), filter.getToDate());

    if (filter.getRelatedSpotId() != null) {
      params = addRelatedSpotIdFilter(params, filter.getRelatedSpotId());
    }

    return params;
  }

  private static String conditionToString(Object condition) {
    if (condition instanceof String) {
      return (String) condition;
    }

    if (condition instanceof Date) {
      Date date = (Date) condition;
      String dateString = DateUtil.format(date);
      return dateString;
    }

    return String.valueOf(condition);
  }
}
