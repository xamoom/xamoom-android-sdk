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

import com.xamoom.android.xamoomsdk.Utils.JsonListUtil;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class JsonListUtilTests {

  @Test
  public void testConstructor() {
    JsonListUtil jsonListUtil = new JsonListUtil();
    assertNotNull(jsonListUtil);
  }

  @Test
  public void testListUtilWithArray() throws Exception {
    List<String> stringList = new ArrayList<>();
    stringList.add("String1");
    stringList.add("String2");

    String output = JsonListUtil.listToJsonArray(stringList, ",");

    assertEquals(output, "['String1','String2']");
  }

  @Test
  public void testListUtilWithOneItem() throws Exception {
    List<String> stringList = new ArrayList<>();
    stringList.add("String1");

    String output = JsonListUtil.listToJsonArray(stringList, ",");

    assertEquals(output, "['String1']");
  }

  @Test
  public void testListUtilWithNullList() throws Exception {
    String output = JsonListUtil.listToJsonArray(null, ",");

    assertEquals(output, "");
  }

  @Test
  public void testJoinList() {
    List<String> stringList = new ArrayList<>();
    stringList.add("String1");
    stringList.add("String2");

    String output = JsonListUtil.joinList(stringList, ",");

    assertEquals(output, "String1,String2");
  }

  @Test
  public void testJoinListOneElement() {
    List<String> stringList = new ArrayList<>();
    stringList.add("String1");

    String output = JsonListUtil.joinList(stringList, ",");

    assertEquals(output, "String1");
  }

  @Test
  public void testJointListNull() {
    String output = JsonListUtil.joinList(null, null);

    assertEquals(output, "");
  }
}
