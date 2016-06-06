package com.xamoom.android.xamoomsdk.Resource;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import at.rags.morpheus.Resource;


/**
 *  xamoom Style model.
 */
public class Style extends Resource implements Parcelable {
  @SerializedName("background-color")
  private String backgroundColor;
  @SerializedName("highlight-color")
  private String highlightFontColor;
  @SerializedName("foreground-color")
  private String foregroundFontColor;
  @SerializedName("chrome-header-color")
  private String chromeHeaderColor;
  @SerializedName("map-pin")
  private String customMarker;
  private String icon;

  public Style() {
  }

  protected Style(Parcel in) {
    this.setId(in.readString());
    backgroundColor = in.readString();
    highlightFontColor = in.readString();
    foregroundFontColor = in.readString();
    chromeHeaderColor = in.readString();
    customMarker = in.readString();
    icon = in.readString();
  }

  public static final Creator<Style> CREATOR = new Creator<Style>() {
    @Override
    public Style createFromParcel(Parcel in) {
      return new Style(in);
    }

    @Override
    public Style[] newArray(int size) {
      return new Style[size];
    }
  };

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

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.getId());
    dest.writeString(backgroundColor);
    dest.writeString(highlightFontColor);
    dest.writeString(foregroundFontColor);
    dest.writeString(chromeHeaderColor);
    dest.writeString(customMarker);
    dest.writeString(icon);
  }
}
