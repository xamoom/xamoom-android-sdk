/*
* Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
*
* Licensed under the MIT License (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at the root of this project.
*/

package com.xamoom.android.xamoomsdk.Resource;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import at.rags.morpheus.Resource;

/**
 *  xamoom SystemSetting model.
 */
public class SystemSetting extends Resource implements Parcelable {
  @SerializedName("app-id-google-play")
  private String googlePlayAppId;
  @SerializedName("app-id-itunes")
  private String itunesAppId;
  @SerializedName("is-social-sharing-active")
  private Boolean socialSharingEnabled;

  public SystemSetting() {
  }

  protected SystemSetting(Parcel in) {
    this.setId(in.readString());
    googlePlayAppId = in.readString();
    itunesAppId = in.readString();
    socialSharingEnabled = (Boolean) in.readSerializable();
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

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.getId());
    dest.writeString(googlePlayAppId);
    dest.writeString(itunesAppId);
    dest.writeSerializable(socialSharingEnabled);
  }

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

  public Boolean getSocialSharingEnabled() {
    return socialSharingEnabled;
  }

  public void setSocialSharingEnabled(Boolean socialSharingEnabled) {
    this.socialSharingEnabled = socialSharingEnabled;
  }


}
