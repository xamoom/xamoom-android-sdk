package com.xamoom.android.xamoomsdk.Resource;

import java.util.List;

import at.rags.morpheus.Annotations.Relationship;
import at.rags.morpheus.Annotations.SerializeName;
import at.rags.morpheus.Resource;

/**
 *  xamoom Content model.
 */
public class Content extends Resource {
  @SerializeName("display-name")
  private String title;
  private String description;
  private String language;
  private int category;
  private List<String> tags;
  @SerializeName("cover-image-url")
  private String publicImageUrl;
  @Relationship("blocks")
  private List<ContentBlock> contentBlocks;
  @Relationship("system")
  private System system;

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public String getLanguage() {
    return language;
  }

  public int getCategory() {
    return category;
  }

  public List<String> getTags() {
    return tags;
  }

  public String getPublicImageUrl() {
    return publicImageUrl;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public void setCategory(int category) {
    this.category = category;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public void setPublicImageUrl(String publicImageUrl) {
    this.publicImageUrl = publicImageUrl;
  }

  public System getSystem() {
    return system;
  }

  public void setSystem(System system) {
    this.system = system;
  }

  public List<ContentBlock> getContentBlocks() {
    return contentBlocks;
  }

  public void setContentBlocks(List<ContentBlock> contentBlocks) {
    this.contentBlocks = contentBlocks;
  }
}
