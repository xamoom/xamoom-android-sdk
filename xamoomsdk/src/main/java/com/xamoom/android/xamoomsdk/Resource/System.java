package com.xamoom.android.xamoomsdk.Resource;

/**
 * Localized system from xamoom-cloud.
 */
public class System {
  private String ID;
  private String name;
  private String url;
  private boolean demo;
  private Object style;
  private Object settings;
  private Object menu;

  public String getID() {
    return ID;
  }

  public String getName() {
    return name;
  }

  public String getUrl() {
    return url;
  }

  public boolean isDemo() {
    return demo;
  }

  public Object getStyle() {
    return style;
  }

  public Object getSettings() {
    return settings;
  }

  public Object getMenu() {
    return menu;
  }
}
