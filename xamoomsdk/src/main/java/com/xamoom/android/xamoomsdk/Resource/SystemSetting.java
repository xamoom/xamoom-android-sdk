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

import java.util.ArrayList;

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
  @SerializedName("is-cookie-warning-enabled")
  private Boolean cookieWarningEnabled;
  @SerializedName("is-recommendations-active")
  private Boolean recommandationEnabled;
  @SerializedName("is-event-package-active")
  private Boolean eventPackageEnabled;
  @SerializedName("is-language-switcher-enabled")
  private Boolean isLanguageSwitcherEnabled;
  @SerializedName("languages")
  private ArrayList<String> languages;
  @SerializedName("is-forms-active")
  private Boolean isFormsActive;
  @SerializedName("forms-base-url")
  private String formsBaseUrl;

  public SystemSetting() {
  }

  protected SystemSetting(Parcel in) {
    this.setId(in.readString());
    googlePlayAppId = in.readString();
    itunesAppId = in.readString();
    socialSharingEnabled = (Boolean) in.readSerializable();
    cookieWarningEnabled = (Boolean) in.readSerializable();
    recommandationEnabled = (Boolean) in.readSerializable();
    eventPackageEnabled = (Boolean) in.readSerializable();
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
    dest.writeSerializable(cookieWarningEnabled);
    dest.writeSerializable(recommandationEnabled);
    dest.writeSerializable(eventPackageEnabled);
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

  public Boolean getCookieWarningEnabled() {
    return cookieWarningEnabled;
  }

  public void setCookieWarningEnabled(Boolean cookieWarningEnabled) {
    this.cookieWarningEnabled = cookieWarningEnabled;
  }

  public Boolean getRecommandationEnabled() {
    return recommandationEnabled;
  }

  public void setRecommandationEnabled(Boolean recommandationEnabled) {
    this.recommandationEnabled = recommandationEnabled;
  }

  public Boolean getEventPackageEnabled() {
    return eventPackageEnabled;
  }

  public void setEventPackageEnabled(Boolean eventPackageEnabled) {
    this.eventPackageEnabled = eventPackageEnabled;
  }

  public Boolean isLanguageSwitcherEnabled() {
    return isLanguageSwitcherEnabled;
  }

  public void setIsLanguageSwitcherEnabled(Boolean isLanguageSwitcherEnabled) {
    this.isLanguageSwitcherEnabled = isLanguageSwitcherEnabled;
  }

  public ArrayList<String> getLanguages() {
    return languages;
  }

  public void setLanguages(ArrayList<String> languages) {
    this.languages = languages;
  }


  public Boolean isFormsActive() {
    return isFormsActive;
  }

  public void setFormsActive(Boolean formsActive) {
    isFormsActive = formsActive;
  }

  public String getFormsBaseUrl() {
    return formsBaseUrl;
  }

  public void setFormsBaseUrl(String formsBaseUrl) {
    this.formsBaseUrl = formsBaseUrl;
  }
}
