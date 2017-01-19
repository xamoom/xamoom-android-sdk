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
 * xamoom Marker model.
 */
public class Marker extends Resource implements Parcelable {
  private String qr;
  private String nfc;
  @SerializedName("ibeacon-region-uid")
  private String beaconUUID;
  @SerializedName("ibeacon-major")
  private String beaconMajor;
  @SerializedName("ibeacon-minor")
  private String beaconMinor;
  @SerializedName("eddystone-url")
  private String eddystoneUrl;

  public Marker() {
  }

  protected Marker(Parcel in) {
    this.setId(in.readString());
    qr = in.readString();
    nfc = in.readString();
    beaconUUID = in.readString();
    beaconMajor = in.readString();
    beaconMinor = in.readString();
    eddystoneUrl = in.readString();
  }

  public static final Creator<Marker> CREATOR = new Creator<Marker>() {
    @Override
    public Marker createFromParcel(Parcel in) {
      return new Marker(in);
    }

    @Override
    public Marker[] newArray(int size) {
      return new Marker[size];
    }
  };

  public String getQr() {
    return qr;
  }

  public void setQr(String qr) {
    this.qr = qr;
  }

  public String getNfc() {
    return nfc;
  }

  public void setNfc(String nfc) {
    this.nfc = nfc;
  }

  public String getBeaconUUID() {
    return beaconUUID;
  }

  public void setBeaconUUID(String beaconUUID) {
    this.beaconUUID = beaconUUID;
  }

  public String getBeaconMajor() {
    return beaconMajor;
  }

  public void setBeaconMajor(String beaconMajor) {
    this.beaconMajor = beaconMajor;
  }

  public String getBeaconMinor() {
    return beaconMinor;
  }

  public void setBeaconMinor(String beaconMinor) {
    this.beaconMinor = beaconMinor;
  }

  public String getEddystoneUrl() {
    return eddystoneUrl;
  }

  public void setEddystoneUrl(String eddystoneUrl) {
    this.eddystoneUrl = eddystoneUrl;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.getId());
    dest.writeString(qr);
    dest.writeString(nfc);
    dest.writeString(beaconUUID);
    dest.writeString(beaconMajor);
    dest.writeString(beaconMinor);
    dest.writeString(eddystoneUrl);
  }
}
