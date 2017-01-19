/*
* Copyright 2017 by xamoom GmbH <apps@xamoom.com>
*
* This file is part of some open source application.
*
* Some open source application is free software: you can redistribute
* it and/or modify it under the terms of the GNU General Public
* License as published by the Free Software Foundation, either
* version 2 of the License, or (at your option) any later version.
*
* Some open source application is distributed in the hope that it will
* be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
* of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with xamoom-android-sdk. If not, see <http://www.gnu.org/licenses/>.
*
* author: Raphael Seher <raphael@xamoom.com>
*/

package com.xamoom.android.xamoomsdk.xamoomsdk.Utils;

import com.xamoom.android.xamoomsdk.Enums.ContentFlags;
import com.xamoom.android.xamoomsdk.Enums.ContentSortFlags;
import com.xamoom.android.xamoomsdk.Enums.SpotFlags;
import com.xamoom.android.xamoomsdk.Enums.SpotSortFlags;
import com.xamoom.android.xamoomsdk.Utils.UrlUtil;

import org.junit.Test;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
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
    checkParams.put("sort", "name,-name");

    Map<String, String> params = UrlUtil.addContentSortingParameter(UrlUtil.getUrlParameter("en"),
        EnumSet.of(ContentSortFlags.NAME, ContentSortFlags.NAME_DESC));

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

}
