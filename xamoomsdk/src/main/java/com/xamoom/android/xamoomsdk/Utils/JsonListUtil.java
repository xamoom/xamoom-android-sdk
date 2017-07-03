/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomsdk.Utils;

import java.util.List;

/**
 * JsonListUtil to join lists of string.
 * (Because TextUtil gets mocked in tests)
 */
public class JsonListUtil {
  /**
   * Will return a json-array of strings.
   *
   * @param list List of Strings.
   * @param seperator String to seperate them.
   * @return Joined list.
   */
  public static String listToJsonArray(List<String> list, String seperator) {
    if (list == null || seperator == null) {
      return "";
    }

    StringBuilder sb = new StringBuilder();
    sb.append("[");
    int counter = 0;
    for (String item : list) {
      counter++;
      sb.append("'");
      sb.append(item);
      sb.append("'");
      if (counter != list.size()) {
        sb.append(seperator);
      }
    }
    sb.append("]");
    return sb.toString();
  }

  public static String joinList(List<String> list, String seperator) {
    if (list == null || seperator == null) {
      return "";
    }

    StringBuilder sb = new StringBuilder();
    int counter = 0;
    for (String item : list) {
      counter++;
      sb.append(item);
      if (counter != list.size()) {
        sb.append(seperator);
      }
    }
    return sb.toString();
  }
}
