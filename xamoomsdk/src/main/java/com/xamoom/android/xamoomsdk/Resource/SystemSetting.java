/*
* Copyright 2017 by xamoom GmbH <apps@xamoom.com>
*
* This file is part of some open source application.
*
* Some open source application is free software: you can redistribute
* it and/or modify it under the terms of the GNU General Public
* License as published by the Free Software Foundation, either
* version 2 of the License, or (at your option) any later version.
*
* Some open source application is distributed in the hope that it will
* be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
* of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with xamoom-android-sdk. If not, see <http://www.gnu.org/licenses/>.
*
* author: Raphael Seher <raphael@xamoom.com>
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

  public SystemSetting() {
  }

  protected SystemSetting(Parcel in) {
    this.setId(in.readString());
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
    dest.writeString(this.getId());
    dest.writeString(googlePlayAppId);
    dest.writeString(itunesAppId);
  }
}
