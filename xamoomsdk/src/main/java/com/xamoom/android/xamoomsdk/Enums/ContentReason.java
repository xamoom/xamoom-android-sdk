package com.xamoom.android.xamoomsdk.Enums;

public enum ContentReason {
  LINKED_CONTENT(3),
  NOTIFICATION(4),
  NOTIFICATION_OPEN(5),
  BEACON(6),
  OFFLINE(7),
  MENU(8);

  private final int value;

  ContentReason(final int newValue) {
    value = newValue;
  }

  public int getValue() { return value; }
}
