package com.xamoom.android.xamoomsdk.Resource;

import android.os.Parcel;
import android.os.Parcelable;

import at.rags.morpheus.Annotations.SerializeName;
import at.rags.morpheus.Resource;

/**
 *  xamoom SystemSetting model.
 */
public class SystemSetting extends Resource implements Parcelable {
  @SerializeName("app-id-google-play")
  private String googlePlayAppId;
  @SerializeName("app-id-itunes")
  private String itunesAppId;

  public SystemSetting() {
  }

  protected SystemSetting(Parcel in) {
    googlePlayAppId = in.readString();
    itunesAppId = in.readString();
  }

  public static final Creator<SystemSetting> CREATOR = new Creator<SystemSetting>() {
    @Override
    public SystemSetting createFromParcel(Parcel in) {
      return new SystemSetting(in);
    }

    @Override
    public SystemSetting[] newArray(int size) {
      return new SystemSetting[size];
    }
  };

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

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(googlePlayAppId);
    dest.writeString(itunesAppId);
  }
}
