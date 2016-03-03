package com.xamoom.android.xamoomsdk.Resource;

import android.util.Log;

import com.xamoom.android.xamoomsdk.Resource.Attributes.SystemAttributesMessage;
import com.xamoom.android.xamoomsdk.Resource.Base.DataMessage;
import com.xamoom.android.xamoomsdk.Resource.Relationships.SystemRelationships;

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

  public System(String ID, String name, String url, boolean demo, Object style, Object settings,
                Object menu) {
    this.ID = ID;
    this.name = name;
    this.url = url;
    this.demo = demo;
    this.style = style;
    this.settings = settings;
    this.menu = menu;
  }

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
