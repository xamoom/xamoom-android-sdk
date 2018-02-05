package com.xamoom.android.xamoomsdk.Utils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {
  private static DateFormat dateFormat;

  public static String format(Date date) {
    setupDateformat();

    return dateFormat.format(date);
  }

  public static Date parse(String dateString) {
    setupDateformat();

    try {
      return dateFormat.parse(dateString);
    } catch (ParseException e) {
      return null;
    }
  }

  private static void setupDateformat() {
    if (dateFormat == null) {
      dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
      dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }
  }
}
