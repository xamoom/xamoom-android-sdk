package com.xamoom.android.xamoomsdk.Resource;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import at.rags.morpheus.Resource;

public class NewVoucherStatusMessage extends Resource implements Parcelable {

  @SerializedName("is-redeemable")
  private Boolean isRedeemable;

  public NewVoucherStatusMessage() { }

  protected NewVoucherStatusMessage(Parcel in) {
    this.setId(in.readString());
    isRedeemable = in.readBoolean();
  }

  public static final Creator<NewVoucherStatusMessage> CREATOR = new Creator<NewVoucherStatusMessage>() {
    @Override
    public NewVoucherStatusMessage createFromParcel(Parcel in) {
      return new NewVoucherStatusMessage(in);
    }

    @Override
    public NewVoucherStatusMessage[] newArray(int size) {
      return new NewVoucherStatusMessage[size];
    }
  };

  public Boolean getRedeemable() {
    return isRedeemable;
  }

  public void setRedeemable(Boolean redeemable) {
    isRedeemable = redeemable;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.getId());
    dest.writeBoolean(isRedeemable);
  }
}