package com.xamoom.android.xamoomsdk.Resource.Attributes;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BlockMessage {
  @SerializedName("data")
  private List<ContentBlockAttributeMessage> blockData;

  public List<ContentBlockAttributeMessage> getBlockData() {
    return blockData;
  }
}
