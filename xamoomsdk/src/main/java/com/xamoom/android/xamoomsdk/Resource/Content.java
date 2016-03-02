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
 *
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

  public Content(String ID, String title, String description, String language, int category, List<String> tags, String publicImageUrl, List<ContentBlock> contentBlocks) {
    this.ID = ID;
    this.title = title;
    this.description = description;
    this.language = language;
    this.category = category;
    this.tags = tags;
    this.publicImageUrl = publicImageUrl;
    this.contentBlocks = contentBlocks;
  }

  public static Content createFromJsonApiMessage(JsonApiMessage<EmptyMessage, DataMessage<ContentAttributesMessage, ContentRelationships>,
      List<DataMessage<ContentBlockAttributeMessage, EmptyMessage>>> jsonApiMessage) {
    String ID;
    String title;
    String description;
    String language;
    int category;
    List<String> tags;
    String publicImageUrl;
    List<ContentBlock> contentBlocks;

    try {
      ID = jsonApiMessage.getData().getId();
      title = jsonApiMessage.getData().getAttributes().getTitle();
      description = jsonApiMessage.getData().getAttributes().getDescription();
      language = jsonApiMessage.getData().getAttributes().getLanguage();
      category = jsonApiMessage.getData().getAttributes().getCategory();
      tags = jsonApiMessage.getData().getAttributes().getTags();
      publicImageUrl = jsonApiMessage.getData().getAttributes().getPublicImageUrl();
      contentBlocks = ContentBlock.jsonToContentBlocks(jsonApiMessage.getIncluded());
    } catch (NullPointerException e) {
      return null;
    }
    return new Content(ID, title, description, language, category, tags, publicImageUrl, contentBlocks);
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
}
