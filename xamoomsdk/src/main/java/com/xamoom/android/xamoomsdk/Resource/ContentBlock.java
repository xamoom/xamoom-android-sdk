package com.xamoom.android.xamoomsdk.Resource;

import java.util.List;

import at.rags.morpheus.Resource;

public class ContentBlock extends Resource {
  private int blockType;
  private boolean publicStatus;
  private String title;
  private String text;
  private String artists;
  private String fileId;
  private String soundcloudUrl;
  private int linkType;
  private String linkUrl;
  private String contentId;
  private int downloadType;
  private List<String> spotMapTags;
  private float scaleX;
  private String videoUrl;
  private boolean showContentOnSpotmap;
  private String altText;

  public int getBlockType() {
    return blockType;
  }

  public boolean isPublicStatus() {
    return publicStatus;
  }

  public String getTitle() {
    return title;
  }

  public String getText() {
    return text;
  }

  public String getArtists() {
    return artists;
  }

  public String getFileId() {
    return fileId;
  }

  public String getSoundcloudUrl() {
    return soundcloudUrl;
  }

  public int getLinkType() {
    return linkType;
  }

  public String getLinkUrl() {
    return linkUrl;
  }

  public String getContentId() {
    return contentId;
  }

  public int getDownloadType() {
    return downloadType;
  }

  public List<String> getSpotMapTags() {
    return spotMapTags;
  }

  public float getScaleX() {
    return scaleX;
  }

  public String getVideoUrl() {
    return videoUrl;
  }

  public boolean isShowContentOnSpotmap() {
    return showContentOnSpotmap;
  }

  public String getAltText() {
    return altText;
  }
}
