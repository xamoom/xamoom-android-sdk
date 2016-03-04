package com.xamoom.android.xamoomsdk;

import com.xamoom.android.xamoomsdk.Resource.*;
import com.xamoom.android.xamoomsdk.Resource.Attributes.ContentAttributesMessage;
import com.xamoom.android.xamoomsdk.Resource.Attributes.ContentBlockAttributeMessage;
import com.xamoom.android.xamoomsdk.Resource.Attributes.SystemAttributesMessage;
import com.xamoom.android.xamoomsdk.Resource.Base.DataMessage;
import com.xamoom.android.xamoomsdk.Resource.Base.EmptyMessage;
import com.xamoom.android.xamoomsdk.Resource.Base.JsonApiMessage;
import com.xamoom.android.xamoomsdk.Resource.Meta.PagingMeta;
import com.xamoom.android.xamoomsdk.Resource.Relationships.ContentRelationships;
import com.xamoom.android.xamoomsdk.Resource.Relationships.SystemRelationships;
import com.xamoom.android.xamoomsdk.Resource.System;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is really bad, but at the moment the best solution to
 * make clean objects from {@link JsonApiMessage}.
 */
public class JsonApiObjectGenerator {

  public static Content jsonToContent(JsonApiMessage<EmptyMessage,
      DataMessage<ContentAttributesMessage, ContentRelationships>,
      List<DataMessage<ContentBlockAttributeMessage, EmptyMessage>>> jsonApiMessage) {
    String ID = null;
    try {
      ID = jsonApiMessage.getData().getId();
    } catch (NullPointerException e) {
      return null;
    }
    String title;
    String description;
    String language;
    int category;
    List<String> tags;
    String publicImageUrl;
    List<ContentBlock> contentBlocks;
    com.xamoom.android.xamoomsdk.Resource.System system;

    try {
      title = jsonApiMessage.getData().getAttributes().getTitle();
      description = jsonApiMessage.getData().getAttributes().getDescription();
      language = jsonApiMessage.getData().getAttributes().getLanguage();
      category = jsonApiMessage.getData().getAttributes().getCategory();
      tags = jsonApiMessage.getData().getAttributes().getTags();
      publicImageUrl = jsonApiMessage.getData().getAttributes().getPublicImageUrl();
      contentBlocks = JsonApiObjectGenerator.jsonToContentBlocks(jsonApiMessage.getIncluded());
      system = JsonApiObjectGenerator.relationToSystem(jsonApiMessage.getData().getRelationships()
          .getSystem().getRelationshipData());
    } catch (NullPointerException e) {
      return null;
    }
    return new Content(ID, title, description, language, category, tags, publicImageUrl,
        contentBlocks, system);
  }

  public static List<Content> jsonToContents(JsonApiMessage<PagingMeta, List<DataMessage<ContentAttributesMessage, ContentRelationships>>,
      List<DataMessage<ContentBlockAttributeMessage, EmptyMessage>>> jsonApiMessage) {
    List<Content> contents = new ArrayList<Content>();

    try {
      for (DataMessage<ContentAttributesMessage, ContentRelationships> dataMessage : jsonApiMessage.getData()) {
        String ID = null;
        try {
          ID = dataMessage.getId();
        } catch (NullPointerException e) {
          e.printStackTrace();
        }
        String title = null;
        String description = null;
        String language = null;
        int category = 0;
        List<String> tags = null;
        String publicImageUrl = null;
        List<ContentBlock> contentBlocks = null;
        System system = null;

        try {
          title = dataMessage.getAttributes().getTitle();
          description = dataMessage.getAttributes().getDescription();
          language = dataMessage.getAttributes().getLanguage();
          category = dataMessage.getAttributes().getCategory();
          tags = dataMessage.getAttributes().getTags();
          publicImageUrl = dataMessage.getAttributes().getPublicImageUrl();
          contentBlocks = JsonApiObjectGenerator.jsonToContentBlocks(dataMessage.getRelationships()
              .getBlocks().getRelationshipData());
          system = JsonApiObjectGenerator.relationToSystem(dataMessage.getRelationships()
              .getSystem().getRelationshipData());
        } catch (NullPointerException e) {
          e.printStackTrace();
        }

        contents.add(new Content(ID, title, description, language, category, tags, publicImageUrl,
            contentBlocks, system));
      }
    } catch (NullPointerException e) {
      return contents;
    }

    return contents;
  }

  public static List<ContentBlock> jsonToContentBlocks(List<DataMessage<ContentBlockAttributeMessage,
      EmptyMessage>> message) {
    List<ContentBlock> contentBlocks = new ArrayList<ContentBlock>();

    for (DataMessage<ContentBlockAttributeMessage, EmptyMessage> blockDataMessage : message) {
      String ID = blockDataMessage.getId();
      try {
        int blockType = blockDataMessage.getAttributes().getBlockType();
        boolean publicStatus = blockDataMessage.getAttributes().isPublic();
        String title = blockDataMessage.getAttributes().getTitle();
        String text = blockDataMessage.getAttributes().getText();
        String artists = blockDataMessage.getAttributes().getArtists();
        String fileId = blockDataMessage.getAttributes().getFileId();
        String soundcloudUrl = blockDataMessage.getAttributes().getSoundcloudUrl();
        int linkType = blockDataMessage.getAttributes().getLinkType();
        String linkUrl = blockDataMessage.getAttributes().getLinkUrl();
        String contentId = blockDataMessage.getAttributes().getContentId();
        int downloadType = blockDataMessage.getAttributes().getDownloadType();
        List<String> spotMapTags = blockDataMessage.getAttributes().getSpotMapTags();
        float scaleX = blockDataMessage.getAttributes().getScaleX();
        String videoUrl = blockDataMessage.getAttributes().getVideoUrl();
        boolean showContentOnSpotmap = blockDataMessage.getAttributes().isShowContentOnSpotmap();
        String altText = blockDataMessage.getAttributes().getAltText();
        contentBlocks.add(new ContentBlock(ID, blockType, publicStatus, title, text, artists, fileId, soundcloudUrl, linkType, linkUrl, contentId, downloadType, spotMapTags, scaleX, videoUrl, showContentOnSpotmap, altText));
      } catch (NullPointerException e) {
        contentBlocks.add(new ContentBlock(ID, 0, true, null, null, null, null, null, 0, null,
            null, 0, null, 100, null, false, null));
      }
    }

    return contentBlocks;
  }

  public static System jsonToSystem(DataMessage<SystemAttributesMessage,
      SystemRelationships> jsonApiMessage) {
    String ID = null;
    try {
      ID = jsonApiMessage.getId();
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    String name;
    String url;
    boolean demo;
    Object style;
    Object settings;
    Object menu;

    try {
      name = jsonApiMessage.getAttributes().getName();
      url = jsonApiMessage.getAttributes().getUrl();
      demo = jsonApiMessage.getAttributes().isDemo();
      style = jsonApiMessage.getRelationships(); //TODO set style
      settings = jsonApiMessage.getRelationships(); //TODO set settings
      menu = jsonApiMessage.getRelationships(); //TODO set menu
    } catch (NullPointerException e) {
      return null;
    }

    return new System(ID, name, url, demo, style, settings, menu);
  }

  public static System relationToSystem(DataMessage<EmptyMessage, EmptyMessage> jsonApiMessage) {
    String ID = null;
    try {
      ID = jsonApiMessage.getId();
    } catch (NullPointerException e) {
      return null;
    }

    return new System(ID, null, null, false, null, null, null);
  }

}
