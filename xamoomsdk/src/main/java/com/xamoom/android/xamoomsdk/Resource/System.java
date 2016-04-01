package com.xamoom.android.xamoomsdk.Resource;

import at.rags.morpheus.Annotations.Relationship;
import at.rags.morpheus.Annotations.SerializeName;
import at.rags.morpheus.Resource;

/**
 *  xamoom System model.
 */
public class System extends Resource {
  @SerializeName("display-name")
  private String name;
  private String url;
  private Object style;
  private Object settings;
  @Relationship("menu")
  private Menu menu;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Object getStyle() {
    return style;
  }

  public void setStyle(Object style) {
    this.style = style;
  }

  public Object getSettings() {
    return settings;
  }

  public void setSettings(Object settings) {
    this.settings = settings;
  }

  public Menu getMenu() {
    return menu;
  }

  public void setMenu(Menu menu) {
    this.menu = menu;
  }
}
