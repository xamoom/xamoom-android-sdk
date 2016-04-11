package com.xamoom.android.xamoomsdk.Utils;

import java.util.List;

/**
 * JsonListUtil to join lists of string.
 * (Because TextUtil gets mocked in tests)
 */
public class JsonListUtil {
  /**
   * Will return joined list with seperator.
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
}
