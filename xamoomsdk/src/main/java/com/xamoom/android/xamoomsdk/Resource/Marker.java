package com.xamoom.android.xamoomsdk.Resource;

import at.rags.morpheus.Annotations.SerializeName;
import at.rags.morpheus.Resource;

/**
 * xamoom Marker model.
 */
public class Marker extends Resource {
  private String qr;
  private String nfc;
  @SerializeName("ibacon-region-uid")
  private String beaconUUID;
  @SerializeName("ibacon-major")
  private String beaconMajor;
  @SerializeName("ibacon-minor")
  private String beaconMinor;
  @SerializeName("eddystone-url")
  private String eddystoneUrl;

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
}
