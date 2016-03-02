package com.xamoom.android.xamoomsdk.Resource.Attributes;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ContentAttributesMessage {
  @SerializedName("display-name")
  private String title;
  @SerializedName("description")
  private String description;
  @SerializedName("language")
  private String language;
  @SerializedName("cover-image-url")
  private String publicImageUrl;
  @SerializedName("tags")
  private List<String> tags;
  @SerializedName("category")
  private int category;

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public String getLanguage() {
    return language;
  }

  public String getPublicImageUrl() {
    return publicImageUrl;
  }

  public List<String> getTags() {
    return tags;
  }

  public int getCategory() {
    return category;
  }
}
