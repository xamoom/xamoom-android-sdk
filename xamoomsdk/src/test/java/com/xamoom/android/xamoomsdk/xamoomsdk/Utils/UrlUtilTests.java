/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomsdk.xamoomsdk.Utils;

import android.webkit.URLUtil;

import com.xamoom.android.xamoomsdk.Enums.ContentFlags;
import com.xamoom.android.xamoomsdk.Enums.ContentSortFlags;
import com.xamoom.android.xamoomsdk.Enums.SpotFlags;
import com.xamoom.android.xamoomsdk.Enums.SpotSortFlags;
import com.xamoom.android.xamoomsdk.Filter;
import com.xamoom.android.xamoomsdk.Utils.DateUtil;
import com.xamoom.android.xamoomsdk.Utils.UrlUtil;

import junit.framework.Assert;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static junit.framework.TestCase.assertNotNull;

public class UrlUtilTests {

  @Test
  public void testConstructor() {
    assertNotNull(new UrlUtil());
  }

  @Test
  public void testAddContentParameterWithNull() {
    Map<String, String> checkParams = new HashMap<>();
    checkParams.put("lang", "en");

    Map<String, String> params = UrlUtil.addContentParameter(UrlUtil.getUrlParameter("en"),
        null);

    assertEquals(params, checkParams);
  }

  @Test
  public void testAddContentParameter() throws Exception {
    Map<String, String> checkParams = new HashMap<>();
    checkParams.put("lang", "en");
    checkParams.put("preview", "true");
    checkParams.put("public-only", "true");

    Map<String, String> params = UrlUtil.addContentParameter(UrlUtil.getUrlParameter("en"),
        EnumSet.of(ContentFlags.PREVIEW, ContentFlags.PRIVATE));

    assertEquals(params, checkParams);
  }

  @Test
  public void testAddContentSortingParameter() throws Exception {
    Map<String, String> checkParams = new HashMap<>();
    checkParams.put("lang", "en");
    checkParams.put("sort", "name,-name,meta-datetime-from,-meta-datetime-from,meta-datetime-to,-meta-datetime-to");

    Map<String, String> params = UrlUtil.addContentSortingParameter(UrlUtil.getUrlParameter("en"),
        EnumSet.of(ContentSortFlags.NAME, ContentSortFlags.NAME_DESC, ContentSortFlags.FROM_DATE.FROM_DATE,
            ContentSortFlags.FROM_DATE_DESC, ContentSortFlags.TO_DATE, ContentSortFlags.TO_DATE_DESC));

    assertEquals(params, checkParams);
  }

  @Test
  public void testAddContentSortingParameterWithNull() throws Exception {
    Map<String, String> checkParams = new HashMap<>();
    checkParams.put("lang", "en");

    Map<String, String> params = UrlUtil.addContentSortingParameter(UrlUtil.getUrlParameter("en"),
        null);

    assertEquals(params, checkParams);
  }

  @Test
  public void testAddSpotParameter() throws Exception {
    Map<String, String> checkParams = new HashMap<>();
    checkParams.put("lang", "en");
    checkParams.put("include_content", "true");
    checkParams.put("include_markers", "true");
    checkParams.put("filter[has-location]", "true");

    Map<String, String> params = UrlUtil.addSpotParameter(UrlUtil.getUrlParameter("en"),
        EnumSet.of(SpotFlags.INCLUDE_CONTENT, SpotFlags.INCLUDE_MARKERS, SpotFlags.HAS_LOCATION));

    assertEquals(params, checkParams);
  }

  @Test
  public void testAddSpotParameterWithNull() throws Exception {
    Map<String, String> checkParams = new HashMap<>();
    checkParams.put("lang", "en");

    Map<String, String> params = UrlUtil.addSpotParameter(UrlUtil.getUrlParameter("en"),
        null);

    assertEquals(params, checkParams);
  }

  @Test
  public void testAddSpotSortingParameter() throws Exception {
    Map<String, String> checkParams = new HashMap<>();
    checkParams.put("lang", "en");
    checkParams.put("sort", "name,-name,distance,-distance");

    Map<String, String> params = UrlUtil.addSpotSortingParameter(UrlUtil.getUrlParameter("en"),
        EnumSet.of(SpotSortFlags.NAME, SpotSortFlags.NAME_DESC, SpotSortFlags.DISTANCE, SpotSortFlags.DISTANCE_DESC));

    assertEquals(params, checkParams);
  }

  @Test
  public void testAddSpotSortingParameterWithNull() throws Exception {
    Map<String, String> checkParams = new HashMap<>();
    checkParams.put("lang", "en");

    Map<String, String> params = UrlUtil.addSpotSortingParameter(UrlUtil.getUrlParameter("en"),
        null);

    assertEquals(params, checkParams);
  }

  @Test
  public void testAddPagingToUrl() throws Exception {
    Map<String, String> checkParams = new HashMap<>();
    checkParams.put("lang", "en");
    checkParams.put("page[size]", "10");
    checkParams.put("page[cursor]", "1");

    Map<String, String> params = UrlUtil.addPagingToUrl(UrlUtil.getUrlParameter("en"), 10, "1");

    assertEquals(params, checkParams);
  }

  @Test
  public void testAddConditionsToUrl() {
    Map<String, String> checkParams = new HashMap<>(4);
    checkParams.put("condition[string]", "string");
    checkParams.put("condition[number]", "1");
    checkParams.put("condition[float]", "2.0");
    checkParams.put("condition[date]", "2017-07-10T11:18:49Z");

    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault());
    Date date = null;
    try {
      date = df.parse("2017-07-10T13:18:49+02:00");
    } catch (ParseException e) {
      fail();
    }

    Map<String, Object> conditions = new HashMap<>(4);
    conditions.put("string", "string");
    conditions.put("number", 1);
    conditions.put("float", 2.0);
    conditions.put("date", date);

    Map<String, String> params = UrlUtil.addConditionsToUrl(new HashMap<String, String>(), conditions);

    assertEquals(checkParams, params);
  }

  @Test
  public void testAddNameFilter() {
    Map<String, String> checkParams = new HashMap<>(1);
    checkParams.put("filter[name]", "name");

    Map<String, String> params = UrlUtil.addNameFilter(new HashMap<String, String>(), "name");

    assertEquals(checkParams, params);
  }

  @Test
  public void testAddDateFilter() {
    Map<String, String> checkParams = new HashMap<>(2);
    checkParams.put("filter[meta-datetime-from]", "2017-07-10T11:18:49Z");
    checkParams.put("filter[meta-datetime-to]", "2017-07-10T11:18:50Z");

    Map<String, String> params = UrlUtil.addDateFilter(new HashMap<String, String>(),
        DateUtil.parse("2017-07-10T11:18:49Z"), DateUtil.parse("2017-07-10T11:18:50Z"));

    assertEquals(checkParams, params);
  }

  @Test
  public void testAddTagsFilter() {
    Map<String, String> checkParams = new HashMap<>(1);
    checkParams.put("filter[tags]", "['tag1','tag2']");

    ArrayList<String> tags = new ArrayList<>();
    tags.add("tag1");
    tags.add("tag2");

    Map<String, String> params = UrlUtil.addTagsFilter(new HashMap<String, String>(), tags);

    assertEquals(checkParams, params);
  }

  @Test
  public void testAddRelatedSpotIdFilter() {
    Map<String, String> checkParams = new HashMap<>(1);
    checkParams.put("filter[related-spot]", "1234");

    Map<String, String> params = UrlUtil.addRelatedSpotIdFilter(new HashMap<String, String>(), "1234");

    assertEquals(checkParams, params);
  }

  @Test
  public void testAddFilters() {
    Map<String, String> checkParams = new HashMap<>(1);
    checkParams.put("filter[name]", "name");
    checkParams.put("filter[tags]", "['tag1','tag2']");
    checkParams.put("filter[meta-datetime-from]", "2017-07-10T11:18:49Z");
    checkParams.put("filter[meta-datetime-to]", "2017-07-10T11:18:50Z");
    checkParams.put("filter[related-spot]", "1234");

    Filter filter = new Filter.FilterBuilder()
        .name("name")
        .addTag("tag1")
        .addTag("tag2")
        .fromDate(DateUtil.parse("2017-07-10T11:18:49Z"))
        .toDate(DateUtil.parse("2017-07-10T11:18:50Z"))
        .relatedSpotId("1234")
        .build();

    Map<String, String> params = UrlUtil.addFilters(new HashMap<String, String>(), filter);

    assertEquals(checkParams, params);
  }

  @Test
  public void testAddConditionsToUrlWithNull() {
    Map<String, String> params = UrlUtil.addConditionsToUrl(new HashMap<String, String>(), null);

    assertEquals(0, params.size());
  }

  @Test
  public void testAddRecommend() {
    Map<String, String> params = new HashMap<>();

    UrlUtil.addRecommend(params);

    assertEquals(1, params.size());
    assertEquals("true", params.get("recommend"));
  }
}
