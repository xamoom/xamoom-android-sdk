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
