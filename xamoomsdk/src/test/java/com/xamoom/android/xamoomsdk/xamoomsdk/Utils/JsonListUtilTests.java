/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
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
