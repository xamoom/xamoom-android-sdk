package com.xamoom.android.xamoomsdk.Resource.Relationships;

import com.google.gson.annotations.SerializedName;
import com.xamoom.android.xamoomsdk.Resource.Attributes.BlockMessage;
import com.xamoom.android.xamoomsdk.Resource.Base.EmptyMessage;


public class ContentRelationships {
  @SerializedName("blocks")
  private BlockMessage blocks;
  @SerializedName("system")
  private EmptyMessage system;

  public BlockMessage getBlocks() {
    return blocks;
  }

  public EmptyMessage getSystem() {
    return system;
  }
}

