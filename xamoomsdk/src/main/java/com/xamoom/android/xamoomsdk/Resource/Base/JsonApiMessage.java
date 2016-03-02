package com.xamoom.android.xamoomsdk.Resource.Base;

import com.google.gson.annotations.SerializedName;

public class JsonApiMessage<T, U, V> {
  @SerializedName("meta")
  private T meta;
  @SerializedName("data")
  private U data;
  @SerializedName("included")
  private V included;


  public T getMeta() {
    return meta;
  }

  public U getData() {
    return data;
  }

  public V getIncluded() {
    return included;
  }

}
