package com.xamoom.android.xamoomsdk;

import com.xamoom.android.xamoomsdk.Utils.ListUtil;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class ListUtilTests {
  @Test
  public void testListUtilWithArray() throws Exception {
    List<String> stringList = new ArrayList<>();
    stringList.add("String1");
    stringList.add("String2");

    String output = ListUtil.joinStringList(stringList, ",");

    assertEquals(output, "String1,String2");
  }

  @Test
  public void testListUtilWithOneItem() throws Exception {
    List<String> stringList = new ArrayList<>();
    stringList.add("String1");

    String output = ListUtil.joinStringList(stringList, ",");

    assertEquals(output, "String1");
  }

  @Test
  public void testListUtilWithNullList() throws Exception {
    String output = ListUtil.joinStringList(null, ",");

    assertEquals(output, "");
  }
}
