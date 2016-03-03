package com.xamoom.android.xamoomsdk.Resource;

import com.xamoom.android.xamoomsdk.Resource.Attributes.ContentAttributesMessage;
import com.xamoom.android.xamoomsdk.Resource.Attributes.ContentBlockAttributeMessage;
import com.xamoom.android.xamoomsdk.Resource.Base.DataMessage;
import com.xamoom.android.xamoomsdk.Resource.Base.EmptyMessage;
import com.xamoom.android.xamoomsdk.Resource.Base.JsonApiMessage;
import com.xamoom.android.xamoomsdk.Resource.Relationships.ContentRelationships;

import java.util.List;

/**
 * Localized content from xamoom-cloud.
 */
public class Content {
  private String ID;
  private String title;
  private String description;
  private String language;
  private int category;
  private List<String> tags;
  private String publicImageUrl;
  private List<ContentBlock> contentBlocks;
  private System system;

  public Content(String ID, String title, String description, String language, int category,
                 List<String> tags, String publicImageUrl, List<ContentBlock> contentBlocks,
                 System system) {
    this.ID = ID;
    this.title = title;
    this.description = description;
    this.language = language;
    this.category = category;
    this.tags = tags;
    this.publicImageUrl = publicImageUrl;
    this.contentBlocks = contentBlocks;
    this.system = system;
  }

  public String getID() {
    return ID;
  }

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

  public List<ContentBlock> getContentBlocks() {
    return contentBlocks;
  }

  public System getSystem() {
    return system;
  }
}
