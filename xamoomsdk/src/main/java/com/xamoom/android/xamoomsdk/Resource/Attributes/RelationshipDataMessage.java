package com.xamoom.android.xamoomsdk.Resource.Attributes;

import com.google.gson.annotations.SerializedName;

public class RelationshipDataMessage<T> {
  @SerializedName("data")
  private T relationshipData;

  public T getRelationshipData() {
    return relationshipData;
  }
}
