package com.xamoom.android.xamoomsdk.PushDevice;

import android.annotation.SuppressLint;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import at.rags.morpheus.Resource;

public class PushDevice extends Resource {
  @SerializedName("id")
  private String uid;
  @SerializedName("os")
  private String os;
  @SerializedName("app-version")
  private String appVersion;
  @SerializedName("app-id")
  private String appId;
  @SerializedName("location")
  private Map<String, Float> location;
  @SerializedName("last-app-open")
  private String lastOpen;
  @SerializedName("updated-at")
  private String updatedAt;
  @SerializedName("created-at")
  private String createdAt;
  @SerializedName("language")
  private String language;
  @SerializedName("sdk-version")
  private String sdkVersion;
  @SerializedName("with-sound")
  private boolean sound;

  public PushDevice() {

  }

  public PushDevice(String token, Map<String, Float> location, String appVersion, String appId, String sdkVersion, boolean sound) {
    this.uid = token;
    this.os = "Android";
    this.appVersion = appVersion;
    this.appId = appId;
    this.location = location;
    this.sdkVersion = sdkVersion;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    String formattedDate = sdf.format(new Date());

    this.lastOpen = formattedDate;
    this.updatedAt = "";
    this.createdAt = "";
    this.language = Locale.getDefault().getLanguage();
    this.sound = sound;
  }

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public String getOs() {
    return os;
  }

  public void setOs(String os) {
    this.os = os;
  }

  public String getAppVersion() {
    return appVersion;
  }

  public void setAppVersion(String appVersion) {
    this.appVersion = appVersion;
  }

  public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public Map<String, Float> getLocation() {
    return location;
  }

  public void setLocation(Map<String, Float> location) {
    this.location = location;
  }

  public String getLastOpen() {
    return lastOpen;
  }

  public void setLastOpen(String lastOpen) {
    this.lastOpen = lastOpen;
  }

  public String getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public boolean isSound() {
    return sound;
  }

  public void setSound(boolean sound) {
    this.sound = sound;
  }
}