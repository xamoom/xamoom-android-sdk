package com.xamoom.android.xamoomsdk.Resource;

import at.rags.morpheus.Annotations.SerializeName;
import at.rags.morpheus.Resource;

/**
 *  xamoom Style model.
 */
public class Style extends Resource {
  @SerializeName("background-color")
  private String backgroundColor;
  @SerializeName("highlight-color")
  private String highlightFontColor;
  @SerializeName("foreground-color")
  private String foregroundFontColor;
  @SerializeName("chrome-header-color")
  private String chromeHeaderColor;
  @SerializeName("map-pin")
  private String customMarker;
  private String icon;

  public String getBackgroundColor() {
    return backgroundColor;
  }

  public void setBackgroundColor(String backgroundColor) {
    this.backgroundColor = backgroundColor;
  }

  public String getHighlightFontColor() {
    return highlightFontColor;
  }

  public void setHighlightFontColor(String highlightFontColor) {
    this.highlightFontColor = highlightFontColor;
  }

  public String getForegroundFontColor() {
    return foregroundFontColor;
  }

  public void setForegroundFontColor(String foregroundFontColor) {
    this.foregroundFontColor = foregroundFontColor;
  }

  public String getChromeHeaderColor() {
    return chromeHeaderColor;
  }

  public void setChromeHeaderColor(String chromeHeaderColor) {
    this.chromeHeaderColor = chromeHeaderColor;
  }

  public String getCustomMarker() {
    return customMarker;
  }

  public void setCustomMarker(String customMarker) {
    this.customMarker = customMarker;
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }
}
