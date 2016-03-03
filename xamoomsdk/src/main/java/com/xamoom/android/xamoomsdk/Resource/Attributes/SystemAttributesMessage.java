package com.xamoom.android.xamoomsdk.Resource.Attributes;

import com.google.gson.annotations.SerializedName;

public class SystemAttributesMessage {
  @SerializedName("display-name")
  private String name;
  @SerializedName("url")
  private String url;
  @SerializedName("is-demo")
  private boolean demo;

  public String getName() {
    return name;
  }

  public String getUrl() {
    return url;
  }

  public boolean isDemo() {
    return demo;
  }
}
