package com.xamoom.android.xamoomsdk.Resource;

import at.rags.morpheus.Annotations.SerializeName;
import at.rags.morpheus.Resource;

/**
 *  xamoom SystemSetting model.
 */
public class SystemSetting extends Resource {
  @SerializeName("app-id-google-play")
  private String googlePlayAppId;
  @SerializeName("app-id-itunes")
  private String itunesAppId;

  public String getGooglePlayAppId() {
    return googlePlayAppId;
  }

  public void setGooglePlayAppId(String googlePlayAppId) {
    this.googlePlayAppId = googlePlayAppId;
  }

  public String getItunesAppId() {
    return itunesAppId;
  }

  public void setItunesAppId(String itunesAppId) {
    this.itunesAppId = itunesAppId;
  }
}
