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

import at.rags.morpheus.Annotations.Relationship;
import at.rags.morpheus.Resource;

/**
 *  xamoom System model.
 */
public class System extends Resource implements Parcelable {
  @SerializedName("display-name")
  private String name;
  private String url;
  @Relationship("style")
  private Style style;
  @Relationship("setting")
  private SystemSetting systemSetting;
  @Relationship("menu")
  private Menu menu;

  public System() {
  }

  protected System(Parcel in) {
    this.setId(in.readString());
    name = in.readString();
    url = in.readString();
    style = in.readParcelable(Style.class.getClassLoader());
    systemSetting = in.readParcelable(SystemSetting.class.getClassLoader());
    menu = in.readParcelable(Menu.class.getClassLoader());
  }

  public static final Creator<System> CREATOR = new Creator<System>() {
    @Override
    public System createFromParcel(Parcel in) {
      return new System(in);
    }

    @Override
    public System[] newArray(int size) {
      return new System[size];
    }
  };

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

  public Style getStyle() {
    return style;
  }

  public void setStyle(Style style) {
    this.style = style;
  }

  public SystemSetting getSystemSetting() {
    return systemSetting;
  }

  public void setSystemSetting(SystemSetting systemSetting) {
    this.systemSetting = systemSetting;
  }

  public Menu getMenu() {
    return menu;
  }

  public void setMenu(Menu menu) {
    this.menu = menu;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.getId());
    dest.writeString(name);
    dest.writeString(url);
    dest.writeParcelable(style, flags);
    dest.writeParcelable(systemSetting, flags);
    dest.writeParcelable(menu, flags);
  }
}
